package com.dslovikosky.narnia.common.world.generator;

import com.dslovikosky.narnia.common.constants.Constants;
import com.dslovikosky.narnia.common.constants.ModBiomes;
import com.dslovikosky.narnia.common.constants.ModBlocks;
import com.dslovikosky.narnia.common.world.generator.charn.CharnBuildings;
import com.dslovikosky.narnia.common.world.generator.charn.CharnRivers;
import com.dslovikosky.narnia.common.world.generator.charn.CharnRoads;
import com.dslovikosky.narnia.common.world.generator.charn.CharnTerrain;
import com.dslovikosky.narnia.common.world.generator.util.FieldTraversal;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.biome.FixedBiomeSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.PositionalRandomFactory;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.blending.Blender;
import net.minecraft.world.level.levelgen.synth.SimplexNoise;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class CharnChunkGenerator extends ChunkGenerator {
    public static final MapCodec<CharnChunkGenerator> CODEC = RecordCodecBuilder.mapCodec(
            it -> it.group(RegistryOps.retrieveElement(ModBiomes.DARK_CITY_RUINS)).apply(it, it.stable(CharnChunkGenerator::new)));
    private static final ResourceLocation RANDOM = Constants.modLocation("charn_noise");

    private static final int CENTER_X = 64;
    private static final int CENTER_Z = 64;

    private final CharnTerrain charnTerrain = new CharnTerrain();
    private final CharnRivers charnRivers = new CharnRivers();
    private final CharnRoads charnRoads = new CharnRoads(CENTER_X, CENTER_Z);
    private final CharnBuildings charnBuildings = new CharnBuildings(CENTER_X, CENTER_Z, charnTerrain, charnRivers, charnRoads);

    public CharnChunkGenerator(final Holder<Biome> biome) {
        super(new FixedBiomeSource(biome));
    }

    @Override
    public CompletableFuture<ChunkAccess> fillFromNoise(Blender blender, RandomState randomState, StructureManager structureManager, ChunkAccess chunk) {
        return CompletableFuture.supplyAsync(() -> {
            final ChunkPos pos = chunk.getPos();
            final int startX = pos.getMinBlockX();
            final int startZ = pos.getMinBlockZ();

            final PositionalRandomFactory randomFactory = randomState.getOrCreateRandomFactory(RANDOM);
            final SimplexNoise noise = new SimplexNoise(randomFactory.fromHashOf(RANDOM));

            for (int x = startX; x < startX + 16; x++) {
                for (int z = startZ; z < startZ + 16; z++) {
                    final int finalX = x;
                    final int finalZ = z;

                    // 1. Compute height, rivers, roads, and buildings

                    final double baseHeight = charnTerrain.computeBaseHeight(x, z, noise);
                    final double riverDepth = charnRivers.computeRiverDepth(x, z, noise);
                    final double roadMask = charnRoads.computeRoadMask(x, z);
                    final double centerAlleyMask = FieldTraversal.findLocalMaxima(x, z, 3,
                            (xPos, zPos) -> charnRoads.computeAlleyMask(xPos, zPos, randomFactory)).value();
                    final CharnRoads.Plot plot = charnRoads.getPlotAt(x, z, randomFactory);
                    final Optional<CharnBuildings.BuildingPlacement> building = charnBuildings.placeBuildingsInPlot(plot, noise, randomFactory)
                            .stream()
                            .filter(placement -> placement.contains(finalX, finalZ))
                            .findFirst();

                    // 2. Compute some useful flags based on our masks

                    final boolean isRiver = riverDepth > 1.0;
                    final boolean isRoadCenter = roadMask > 0.4;
                    final boolean isAlley = centerAlleyMask > 0.4 && !isRoadCenter && !isRiver;
                    final boolean isRoadEdge = roadMask > 0 && !isRoadCenter && !isAlley;
                    final boolean isBridge = isRiver && (isRoadEdge || isRoadCenter);

                    // 3. Determine the ground height based on our flags

                    final int groundHeight;
                    if (isRoadCenter || isRoadEdge) {
                        // "Flatten" the height around roads so roads are flat
                        final FieldTraversal.LocalMaxima roadCenter = FieldTraversal.findLocalMaxima(x, z, 4, charnRoads::computeRoadMask);
                        final double roadHeight = charnTerrain.computeBaseHeight(roadCenter.x(), roadCenter.z(), noise);
                        groundHeight = (int) Math.floor(roadHeight);
                    } else {
                        groundHeight = (int) Math.floor(baseHeight);
                    }
                    // Sometimes a flattened road leads to a base height that is actually lower than the river
                    final int riverHeight = Math.min(groundHeight, (int) Math.floor(baseHeight - riverDepth));

                    final BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();

                    // 4. Set all the blocks

                    // Create bedrock layer
                    chunk.setBlockState(mutablePos.set(x, chunk.getMinY(), z), Blocks.BEDROCK.defaultBlockState());

                    // Fill from bedrock to surface with sandstone
                    for (int y = chunk.getMinY() + 1; y < (isRiver ? riverHeight : groundHeight); y++) {
                        chunk.setBlockState(mutablePos.set(x, y, z), Blocks.SANDSTONE.defaultBlockState());
                    }

                    // If we're generating a river, set the top 3 layers to dirt. If not, set the top 3 layers to sand
                    for (int y = (isRiver ? riverHeight : groundHeight) - 2; y <= (isRiver ? riverHeight : groundHeight); y++) {
                        chunk.setBlockState(mutablePos.set(x, y, z), isRiver ? Blocks.COARSE_DIRT.defaultBlockState() : Blocks.SAND.defaultBlockState());
                    }

                    if (isRoadCenter) {
                        // If we're generating a road center, set the top 2 blocks to smooth stone
                        final BlockState verticalDarkCitySmoothStone = ModBlocks.DARK_CITY_SMOOTH_STONE.get()
                                .defaultBlockState()
                                .setValue(RotatedPillarBlock.AXIS, Direction.Axis.Y);
                        chunk.setBlockState(mutablePos.set(x, groundHeight - 1, z), verticalDarkCitySmoothStone);
                        chunk.setBlockState(mutablePos.set(x, groundHeight, z), verticalDarkCitySmoothStone);
                    } else if (isRoadEdge) {
                        // If we're generating a road edge, set the top 2 blocks to cobblestone
                        chunk.setBlockState(mutablePos.set(x, groundHeight - 1, z), ModBlocks.DARK_CITY_COBBLESTONE.get().defaultBlockState());
                        chunk.setBlockState(mutablePos.set(x, groundHeight, z), ModBlocks.DARK_CITY_COBBLESTONE.get().defaultBlockState());
                        if (isBridge) {
                            // If we're generating an edge as part of a bridge, set the top block to a wall
                            chunk.setBlockState(mutablePos.set(x, groundHeight + 1, z), ModBlocks.DARK_CITY_STONE_BRICK_WALL.get().defaultBlockState());
                            chunk.markPosForPostprocessing(mutablePos.set(x, groundHeight + 1, z));
                        }
                    } else if (isAlley) {
                        // If we're generating an alley, set the top block to cobblestone
                        chunk.setBlockState(mutablePos.set(x, groundHeight, z), ModBlocks.DARK_CITY_COBBLESTONE.get().defaultBlockState());
                    } else if (building.isPresent()) {
                        // If we're generating a building, paste the building
                        building.get().pasteColumn(chunk, x, z);
                    }
                }
            }

            return chunk;
        });
    }

    @Override
    public int getBaseHeight(final int x, final int z, final Heightmap.Types type, final LevelHeightAccessor level, final RandomState random) {
        final SimplexNoise detailNoise = new SimplexNoise(random.getOrCreateRandomFactory(RANDOM).fromHashOf(RANDOM));

        final double baseHeight = charnTerrain.computeBaseHeight(x, z, detailNoise);
        final double riverDepth = charnRivers.computeRiverDepth(x, z, detailNoise);

        // Roads, alleys, and buildings don't affect base height
        return (int) (baseHeight - riverDepth);
    }

    @Override
    public NoiseColumn getBaseColumn(int x, int z, LevelHeightAccessor level, RandomState random) {
        final int height = getBaseHeight(x, z, Heightmap.Types.WORLD_SURFACE_WG, level, random);
        final BlockState[] states = new BlockState[height];
        states[0] = Blocks.BEDROCK.defaultBlockState();
        for (int i = 1; i < height - 1; i++) {
            states[i] = Blocks.SANDSTONE.defaultBlockState();
        }
        states[height - 1] = Blocks.SAND.defaultBlockState();
        return new NoiseColumn(level.getMinY(), states);
    }

    @Override
    protected MapCodec<? extends ChunkGenerator> codec() {
        return CODEC;
    }

    @Override
    public void applyCarvers(WorldGenRegion level, long seed, RandomState random, BiomeManager biomeManager, StructureManager structureManager, ChunkAccess chunk) {
    }

    @Override
    public void buildSurface(WorldGenRegion level, StructureManager structureManager, RandomState random, ChunkAccess chunk) {
    }

    @Override
    public void spawnOriginalMobs(WorldGenRegion level) {
    }

    @Override
    public int getGenDepth() {
        return 256;
    }

    @Override
    public int getSeaLevel() {
        return 63;
    }

    @Override
    public int getMinY() {
        return 0;
    }

    @Override
    public void addDebugScreenInfo(List<String> info, RandomState random, BlockPos pos) {
        info.add("Charn City Generator");
    }
}
