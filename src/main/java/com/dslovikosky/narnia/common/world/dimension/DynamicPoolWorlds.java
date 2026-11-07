package com.dslovikosky.narnia.common.world.dimension;

import com.dslovikosky.narnia.common.constants.Constants;
import com.dslovikosky.narnia.common.constants.ModDimensions;
import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;

import java.util.Optional;

/**
 * The mapping between a water pool in the Wood Between The Worlds and the world it leads to.
 */
public class DynamicPoolWorlds {
    public static final BiMap<ChunkPos, ResourceKey<Level>> FIXED_POOL_LEVEL_MAP = ImmutableBiMap.<ChunkPos, ResourceKey<Level>>builder()
            .put(new ChunkPos(0, 0), Level.OVERWORLD)
            .put(new ChunkPos(1, 0), Level.NETHER)
            .put(new ChunkPos(1, 1), Level.END)
            .put(new ChunkPos(0, 1), ModDimensions.DARK_CITY_RUINS)
            .build();

    private static final String POOL_PATH_PREFIX = "pool_";

    public static ResourceKey<Level> dimensionOf(final ChunkPos poolChunk) {
        final ResourceKey<Level> fixed = FIXED_POOL_LEVEL_MAP.get(poolChunk);
        return fixed != null ? fixed : dynamicDimensionOf(poolChunk);
    }

    public static ResourceKey<Level> dynamicDimensionOf(final ChunkPos poolChunk) {
        return ResourceKey.create(Registries.DIMENSION, Constants.modLocation(POOL_PATH_PREFIX + poolChunk.x() + "_" + poolChunk.z()));
    }

    public static Optional<ChunkPos> poolChunkOf(final ResourceKey<Level> dimension) {
        final ChunkPos fixed = FIXED_POOL_LEVEL_MAP.inverse().get(dimension);
        return fixed != null ? Optional.of(fixed) : dynamicPoolChunkOf(dimension);
    }

    public static Optional<ChunkPos> dynamicPoolChunkOf(final ResourceKey<Level> dimension) {
        final Identifier identifier = dimension.identifier();
        if (!identifier.getNamespace().equals(Constants.MOD_ID) || !identifier.getPath().startsWith(POOL_PATH_PREFIX)) {
            return Optional.empty();
        }

        final String[] coordinates = identifier.getPath().substring(POOL_PATH_PREFIX.length()).split("_");
        if (coordinates.length != 2) {
            return Optional.empty();
        }

        try {
            return Optional.of(new ChunkPos(Integer.parseInt(coordinates[0]), Integer.parseInt(coordinates[1])));
        } catch (final NumberFormatException e) {
            return Optional.empty();
        }
    }

    public static boolean isDynamic(final ResourceKey<Level> dimension) {
        return dynamicPoolChunkOf(dimension).isPresent();
    }

    public static long seedOf(final long worldSeed, final ChunkPos poolChunk) {
        long seed = worldSeed;
        seed = mix(seed ^ (poolChunk.x() * 0x9E3779B97F4A7C15L));
        seed = mix(seed ^ (poolChunk.z() * 0xC2B2AE3D27D4EB4FL));
        return seed;
    }

    private static long mix(long value) {
        value ^= value >>> 30;
        value *= 0xBF58476D1CE4E5B9L;
        value ^= value >>> 27;
        value *= 0x94D049BB133111EBL;
        return value ^ (value >>> 31);
    }
}
