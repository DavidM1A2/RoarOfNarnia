package com.dslovikosky.narnia.common.world.dimension;

import com.dslovikosky.narnia.common.constants.Constants;
import com.dslovikosky.narnia.common.constants.ModDimensionTypes;
import com.mojang.logging.LogUtils;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.PlayerSpawnFinder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.biome.MultiNoiseBiomeSource;
import net.minecraft.world.level.biome.MultiNoiseBiomeSourceParameterList;
import net.minecraft.world.level.biome.MultiNoiseBiomeSourceParameterLists;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.storage.DerivedLevelData;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.level.LevelEvent;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.OptionalLong;
import java.util.stream.Stream;

/**
 * Creates, restores and unloads the dimensions that the Wood Between The Worlds' pools lead to.
 */
public class DynamicPoolWorldManager {
    private static final Logger LOG = LogUtils.getLogger();
    /**
     * How long a pool world with nobody in it is kept alive.
     */
    private static final int UNLOAD_AFTER_EMPTY_TICKS = 20 * 60 * 5;
    /**
     * How far out from the origin to hunt for somewhere safe to drop an arriving player.
     */
    private static final int ARRIVAL_SEARCH_CHUNK_RADIUS = 4;

    private static final Object2IntMap<ResourceKey<Level>> EMPTY_TICKS = new Object2IntOpenHashMap<>();

    public static @Nullable ServerLevel levelFor(final MinecraftServer server, final ChunkPos poolChunk) {
        final ResourceKey<Level> dimension = DynamicPoolWorlds.dimensionOf(poolChunk);

        final ServerLevel existing = server.getLevel(dimension);
        if (existing != null) {
            EMPTY_TICKS.removeInt(dimension);
            return existing;
        }

        if (!DynamicPoolWorlds.isDynamic(dimension)) {
            return null;
        }

        return create(server, dimension, poolChunk);
    }

    /**
     * Re-creates every pool world that has been visited before in this save.
     */
    public static void restoreVisitedWorlds(final MinecraftServer server) {
        final Path poolWorldDirectory = server.storageSource
                .getDimensionPath(DynamicPoolWorlds.dynamicDimensionOf(ChunkPos.ZERO))
                .getParent();
        if (poolWorldDirectory == null || !Files.isDirectory(poolWorldDirectory)) {
            return;
        }

        final List<ChunkPos> visitedPools = new ArrayList<>();
        try (final Stream<Path> children = Files.list(poolWorldDirectory)) {
            children.filter(Files::isDirectory).forEach(child -> {
                // The mod's hand-authored dimensions live in this same directory, so only the strict parse will do
                final ResourceKey<Level> dimension = ResourceKey.create(Registries.DIMENSION, Constants.modLocation(child.getFileName().toString()));
                DynamicPoolWorlds.dynamicPoolChunkOf(dimension).ifPresent(visitedPools::add);
            });
        } catch (final IOException e) {
            LOG.error("Could not read the previously visited pool worlds in {}", poolWorldDirectory, e);
            return;
        }

        visitedPools.forEach(poolChunk -> create(server, DynamicPoolWorlds.dynamicDimensionOf(poolChunk), poolChunk));
        LOG.info("Restored {} previously visited pool worlds", visitedPools.size());
    }

    /**
     * Unloads pool worlds that have had nobody in them for {@link #UNLOAD_AFTER_EMPTY_TICKS}.
     */
    public static void unloadEmptyWorlds(final MinecraftServer server, final int elapsedTicks) {
        // A copy, because unloading mutates the map this is a view of
        for (final ResourceKey<Level> dimension : List.copyOf(server.levelKeys())) {
            if (!DynamicPoolWorlds.isDynamic(dimension)) {
                continue;
            }

            final ServerLevel level = server.getLevel(dimension);
            if (level == null) {
                continue;
            }

            // Force loaded chunks are an explicit request to keep a world running, so honour it
            if (!level.players().isEmpty() || !level.getForceLoadedChunks().isEmpty()) {
                EMPTY_TICKS.removeInt(dimension);
                continue;
            }

            final int emptyTicks = EMPTY_TICKS.getOrDefault(dimension, 0) + elapsedTicks;
            if (emptyTicks < UNLOAD_AFTER_EMPTY_TICKS) {
                EMPTY_TICKS.put(dimension, emptyTicks);
                continue;
            }

            unload(server, dimension, level);
        }
    }

    /**
     * Drops the idle tracking, which is per server session rather than per save.
     */
    public static void clear() {
        EMPTY_TICKS.clear();
    }

    /**
     * Finds somewhere safe for a player arriving in a pool world for the first time.
     */
    public static Vec3 findArrivalPoint(final ServerLevel level) {
        for (int radius = 0; radius <= ARRIVAL_SEARCH_CHUNK_RADIUS; radius++) {
            for (int chunkX = -radius; chunkX <= radius; chunkX++) {
                for (int chunkZ = -radius; chunkZ <= radius; chunkZ++) {
                    // Only the ring this radius just added, the inner ones were covered by an earlier pass
                    if (Math.max(Math.abs(chunkX), Math.abs(chunkZ)) != radius) {
                        continue;
                    }

                    final BlockPos arrival = PlayerSpawnFinder.getSpawnPosInChunk(level, new ChunkPos(chunkX, chunkZ));
                    if (arrival != null) {
                        return Vec3.upFromBottomCenterOf(arrival, 1);
                    }
                }
            }
        }

        // Nowhere in range was habitable - standing on the surface still beats falling through the void
        final int surface = level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, 8, 8);
        LOG.warn("Found nowhere safe to arrive in {}, falling back to the surface at 8, {}, 8", level.dimension().identifier(), surface);
        return new Vec3(8.5, surface + 1, 8.5);
    }

    private static ServerLevel create(final MinecraftServer server, final ResourceKey<Level> dimension, final ChunkPos poolChunk) {
        final ServerLevel existing = server.getLevel(dimension);
        if (existing != null) {
            LOG.warn("Asked to create the pool world {}, which already exists", dimension.identifier());
            return existing;
        }

        final long seed = DynamicPoolWorlds.seedOf(server.getWorldGenSettings().options().seed(), poolChunk);
        final LevelStem levelStem = new LevelStem(dimensionType(server), generator(server), OptionalLong.of(seed));

        final ServerLevel level = new ServerLevel(
                server,
                server.executor,
                server.storageSource,
                new DerivedLevelData(server.getWorldData(), server.getWorldData().overworldData()),
                dimension,
                levelStem,
                server.getWorldData().isDebugWorld(),
                BiomeManager.obfuscateSeed(seed),
                List.of(),
                false);

        server.levels.put(dimension, level);
        level.getWorldBorder().setAbsoluteMaxSize(server.getAbsoluteMaxWorldSize());
        server.getPlayerList().addWorldborderListener(level);
        level.setSpawnSettings(level.isSpawningMonsters());
        NeoForge.EVENT_BUS.post(new LevelEvent.Load(level));

        LOG.info("Created the pool world {} for the pool in chunk {} with seed {}", dimension.identifier(), poolChunk, seed);
        return level;
    }

    private static void unload(final MinecraftServer server, final ResourceKey<Level> dimension, final ServerLevel level) {
        EMPTY_TICKS.removeInt(dimension);
        level.save(null, true, false);
        // Out of the map first, so nothing can tick a level that is being torn down
        server.levels.remove(dimension);
        NeoForge.EVENT_BUS.post(new LevelEvent.Unload(level));

        try {
            level.close();
        } catch (final IOException e) {
            LOG.error("Could not close the pool world {}", dimension.identifier(), e);
        }

        LOG.info("Unloaded the empty pool world {}", dimension.identifier());
    }

    /**
     * The world every pool leads to, for now.
     */
    private static ChunkGenerator generator(final MinecraftServer server) {
        final RegistryAccess registries = server.registryAccess();
        final Holder<MultiNoiseBiomeSourceParameterList> biomePreset = registries
                .lookupOrThrow(Registries.MULTI_NOISE_BIOME_SOURCE_PARAMETER_LIST)
                .getOrThrow(MultiNoiseBiomeSourceParameterLists.OVERWORLD);
        final Holder<NoiseGeneratorSettings> noiseSettings = registries
                .lookupOrThrow(Registries.NOISE_SETTINGS)
                .getOrThrow(NoiseGeneratorSettings.OVERWORLD);
        return new NoiseBasedChunkGenerator(MultiNoiseBiomeSource.createFromPreset(biomePreset), noiseSettings);
    }

    private static Holder<DimensionType> dimensionType(final MinecraftServer server) {
        return server.registryAccess()
                .lookupOrThrow(Registries.DIMENSION_TYPE)
                .getOrThrow(ModDimensionTypes.DYNAMIC_POOL_WORLD);
    }
}
