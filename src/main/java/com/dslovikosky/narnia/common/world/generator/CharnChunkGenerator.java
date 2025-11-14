package com.dslovikosky.narnia.common.world.generator;

import com.dslovikosky.narnia.common.constants.Constants;
import com.dslovikosky.narnia.common.constants.ModBiomes;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.biome.FixedBiomeSource;
import net.minecraft.world.level.block.Blocks;
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
import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class CharnChunkGenerator extends ChunkGenerator {
    public static final MapCodec<CharnChunkGenerator> CODEC = RecordCodecBuilder.mapCodec(
            it -> it.group(RegistryOps.retrieveElement(ModBiomes.DARK_CITY_RUINS)).apply(it, it.stable(CharnChunkGenerator::new)));
    private static final ResourceLocation RANDOM = Constants.modLocation("charn_noise");
    private static final ResourceLocation TERRAIN = Constants.modLocation("charn_noise_terrain");
    private static final int CITY_CELL_SIZE = 128;
    private static final double ROAD_WIDTH = 8.0;
    private static final double ALLEY_WIDTH = 5.0;

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
            final SimplexNoise noise = new SimplexNoise(randomFactory.fromHashOf(TERRAIN));

            for (int x = startX; x < startX + 16; x++) {
                for (int z = startZ; z < startZ + 16; z++) {
                    final double baseHeight = computeBaseTerrain(x, z, noise, 64.0);

                    final RiverInfo river = computeRiver(x, z, noise);

                    final double riverMask = river.mask();
                    final double riverDepth = river.depth();

                    final double roadMask = computeRoadMask(x, z);

                    final LocalMaxima alleyCenter = findLocalMaxima(x, z, 3, (xPos, zPos) -> computeAlleyMask(xPos, zPos, randomFactory, river));
                    final double centerAlleyMask = alleyCenter.value();

                    final PlotInfo plotInfo = getPlotAt(x, z, randomFactory);

                    final boolean isRoadCenter = roadMask > 0.4;
                    final boolean isAlley = centerAlleyMask > 0.4 && !isRoadCenter;
                    final boolean isRoadEdge = roadMask > 0 && !isRoadCenter && !isAlley;
                    final boolean isRiver = riverMask > 0.1;
                    final boolean isPlot = plotInfo.contains(x, z) && !isRoadCenter && !isAlley && !isRoadEdge && !isRiver;

                    final int groundHeight;
                    if (isRoadCenter || isRoadEdge) {
                        // "Flatten" the height around roads so roads are flat
                        final LocalMaxima roadCenter = findLocalMaxima(x, z, 4, this::computeRoadMask);
                        final double roadHeight = computeBaseTerrain(roadCenter.x(), roadCenter.z(), noise, 64.0);
                        groundHeight = (int) Math.floor(roadHeight);
                    } else {
                        groundHeight = (int) Math.floor(baseHeight);
                    }

                    // Sometimes a flattened road leads to a base height that is actually lower than the river
                    final int riverHeight = Math.min(groundHeight, (int) Math.floor(baseHeight - riverDepth));

                    final BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();

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

                    // If we're generating a road, set the top layer to stone bricks or cobblestone
                    if (isRoadCenter) {
                        chunk.setBlockState(mutablePos.set(x, groundHeight - 1, z), Blocks.STONE_BRICKS.defaultBlockState());
                        chunk.setBlockState(mutablePos.set(x, groundHeight, z), Blocks.STONE_BRICKS.defaultBlockState());
                    } else if (isRoadEdge) {
                        chunk.setBlockState(mutablePos.set(x, groundHeight - 1, z), Blocks.COBBLESTONE.defaultBlockState());
                        chunk.setBlockState(mutablePos.set(x, groundHeight, z), Blocks.COBBLESTONE.defaultBlockState());
                        chunk.setBlockState(mutablePos.set(x, groundHeight + 1, z), Blocks.COBBLESTONE.defaultBlockState());
                    } else if (isAlley) {
                        chunk.setBlockState(mutablePos.set(x, groundHeight, z), Blocks.GRAVEL.defaultBlockState());
                    } else if (isPlot) {
                        chunk.setBlockState(mutablePos.set(x, groundHeight, z), Blocks.WHITE_WOOL.defaultBlockState());
                    }
                }
            }

            return chunk;
        });
    }

    @Override
    public int getBaseHeight(final int x, final int z, final Heightmap.Types type, final LevelHeightAccessor level, final RandomState random) {
        final SimplexNoise detailNoise = new SimplexNoise(random.getOrCreateRandomFactory(RANDOM).fromHashOf(TERRAIN));

        final double baseHeight = computeBaseTerrain(x, z, detailNoise, 64.0);

        final RiverInfo river = computeRiver(x, z, detailNoise);

        final double riverDepth = river.depth();
        return (int) (baseHeight - riverDepth);
    }

    private double computeBaseTerrain(final int x, final int z, final SimplexNoise noise, final double baseY) {
        double mountainHeight = computeMountainHeight(x, z, noise);
        double detail = computeTerrainDetail(x, z, noise);
        return baseY + mountainHeight + detail;
    }

    private double computeTerrainDetail(final int x, final int z, final SimplexNoise noise) {
        // Multi-frequency noise for fractal detail
        double baseNoise = noise.getValue(x * 0.01, z * 0.01);
        double midNoise = noise.getValue(x * 0.03, z * 0.03);
        return baseNoise * 3.0 + midNoise * 1.2;
    }

    private double computeMountainHeight(final int x, final int z, final SimplexNoise noise) {
        // Base frequency controls how far apart mountains are (lower = more spaced out)
        double mountainFrequency = 1.0 / 1000.0;
        double mountainHeight = 40.0;

        // Multi-octave gentle variation
        double n1 = noise.getValue(x * mountainFrequency, z * mountainFrequency);
        double n2 = noise.getValue(x * mountainFrequency * 2.0, z * mountainFrequency * 2.0) * 0.5;
        double n3 = noise.getValue(x * mountainFrequency * 4.0, z * mountainFrequency * 4.0) * 0.25;

        double combined = (n1 + n2 + n3) / 1.75;

        // Use a smooth "ridge" function — peaks are more rounded, not sharp
        double height = Math.pow(Math.abs(combined), 1.3) * mountainHeight;

        // Add slow global slope variation (adds more natural continental feel)
        double slope = noise.getValue(x * 0.0002, z * 0.0002) * 10.0;
        return height + slope;
    }

    private RiverInfo computeRiver(final int x, final int z, final SimplexNoise noise) {
        // --- Step 1: Base smooth noise field ---
        // Very low frequency → broad, continent-scale curves
        double riverBase = noise.getValue(x * 0.0015, z * 0.0015);

        // Slight warp to avoid uniform sine-wave patterns
        double riverWarp = noise.getValue((x + 2000) * 0.006, (z - 2000) * 0.006) * 0.5;

        // Combine base and warp to create gentle direction bending
        double riverCombined = riverBase + riverWarp * 0.4;

        // --- Step 2: Detect “near-zero” crossings for riverbeds ---
        // The closer riverCombined is to 0, the closer we are to a river
        double riverVal = Math.abs(riverCombined);

        // Sharper falloff = thinner rivers
        double riverMask = 1.0 - Mth.clamp((riverVal - 0.03) * 10.0, 0.0, 1.0);

        // Make them rare and thinner with a power curve
        riverMask = Math.pow(riverMask, 7.0);

        // --- Step 4: Depth scaling ---
        double riverDepth = riverMask * 6.0;

        return new RiverInfo(riverMask, riverDepth);
    }

    private double computeRoadMask(final int x, final int z) {
        // Find distance to the nearest vertical and horizontal road centerlines
        double distToXCenter = Math.abs(Mth.positiveModulo(x + CITY_CELL_SIZE / 2.0, CITY_CELL_SIZE) - CITY_CELL_SIZE / 2.0);
        double distToZCenter = Math.abs(Mth.positiveModulo(z + CITY_CELL_SIZE / 2.0, CITY_CELL_SIZE) - CITY_CELL_SIZE / 2.0);

        // Distance to the closest road (either along X or Z)
        double dist = Math.min(distToXCenter, distToZCenter);

        // Convert distance to mask using a smooth falloff:
        // 1.0 at dist = 0, fades smoothly to 0.0 at dist = roadHalfWidth
        if (dist >= ROAD_WIDTH / 2) {
            return 0.0;
        } else {
            return 0.5 * (Math.cos(Math.PI * dist / (ROAD_WIDTH / 2)) + 1.0);
        }
    }

    private double computeAlleyMask(final int x, final int z, final PositionalRandomFactory randomFactory, final RiverInfo river) {
        double alleyMask = computeAlleyMaskRaw(x, z, randomFactory);

        // Fade out alleys around rivers
        double fade = 1.0 - Mth.clamp(river.mask() * 20.0, 0.0, 1.0);

        return alleyMask * fade;
    }

    /**
     * Computes a "mask" for alleys at a given world position.
     * 1.0 = center of alley, 0.0 = outside.
     */
    private double computeAlleyMaskRaw(final int x, final int z, final PositionalRandomFactory randomFactory) {
        // Which city cell are we in
        final int cellX = Math.floorDiv(x, CITY_CELL_SIZE);
        final int cellZ = Math.floorDiv(z, CITY_CELL_SIZE);

        final double[] verticals = computeAlleyVerticals(x, z, CITY_CELL_SIZE, 16, randomFactory);
        final double[] horizontals = computeAlleyHorizontals(x, z, CITY_CELL_SIZE, 16, randomFactory);

        // Convert to local coordinates
        final double localX = x - cellX * CITY_CELL_SIZE;
        final double localZ = z - cellZ * CITY_CELL_SIZE;

        // Distance to nearest vertical/horizontal alleys
        double nearestVertical = Double.POSITIVE_INFINITY;
        for (double vertical : verticals) {
            nearestVertical = Math.min(nearestVertical, Math.abs(vertical - localX));
        }
        double nearestHorizontal = Double.POSITIVE_INFINITY;
        for (double horizontal : horizontals) {
            nearestHorizontal = Math.min(nearestHorizontal, Math.abs(horizontal - localZ));
        }

        // Smooth cosine falloff
        final double falloffScale = 0.56; // computed for threshold 0.4 to give ~5 block width
        final double halfWidth = ALLEY_WIDTH / 2.0;

        final double maskV = nearestVertical < halfWidth * falloffScale
                ? 0.5 * (Math.cos(Math.PI * nearestVertical / (halfWidth * falloffScale)) + 1.0)
                : 0.0;

        final double maskH = nearestHorizontal < halfWidth * falloffScale
                ? 0.5 * (Math.cos(Math.PI * nearestHorizontal / (halfWidth * falloffScale)) + 1.0)
                : 0.0;

        return Math.max(maskV, maskH);
    }

    private double[] computeAlleyVerticals(final int x, final int z, final int cellSize, final int minSpacing, final PositionalRandomFactory randomFactory) {
        // Which city cell are we in
        final int cellX = Math.floorDiv(x, cellSize);
        final int cellZ = Math.floorDiv(z, cellSize);
        final RandomSource rand = randomFactory.at(cellX, 0, cellZ);

        // Evenly space them but add random jitter.
        // Choose how many alleys to spawn in this cell (1–3 each direction)
        final int numVerticals = 1 + rand.nextInt(3);
        final double[] verticals = new double[numVerticals];
        final double spacingVertical = (double) cellSize / (numVerticals + 1);
        for (int i = 0; i < numVerticals; i++) {
            verticals[i] = (i + 1) * spacingVertical + rand.nextDouble() * (spacingVertical - minSpacing);
            // Snap all alley lines to half-block centers for consistency
            verticals[i] = Math.floor(verticals[i]) + 0.5;
        }

        return verticals;
    }

    private double[] computeAlleyHorizontals(final int x, final int z, final int cellSize, final int minSpacing, final PositionalRandomFactory randomFactory) {
        // Which city cell are we in
        final int cellX = Math.floorDiv(x, cellSize);
        final int cellZ = Math.floorDiv(z, cellSize);
        final RandomSource rand = randomFactory.at(cellX, 0, cellZ);

        // Evenly space them but add random jitter.
        // Choose how many alleys to spawn in this cell (1–3 each direction)
        final int numHorizontals = 1 + rand.nextInt(3);
        final double[] horizontals = new double[numHorizontals];
        final double spacingHorizontal = (double) cellSize / (numHorizontals + 1);
        for (int i = 0; i < numHorizontals; i++) {
            horizontals[i] = (i + 1) * spacingHorizontal + rand.nextDouble() * (spacingHorizontal - minSpacing);
            // Snap all alley lines to half-block centers for consistency
            horizontals[i] = Math.floor(horizontals[i]) + 0.5;
        }
        return horizontals;
    }

    private PlotInfo getPlotAt(final int x, final int z, final PositionalRandomFactory randomFactory) {
        int cellX = Math.floorDiv(x, CITY_CELL_SIZE);
        int cellZ = Math.floorDiv(z, CITY_CELL_SIZE);

        final double[] alleyVerticals = computeAlleyVerticals(x, z, CITY_CELL_SIZE, 16, randomFactory);
        final double[] alleyHorizontals = computeAlleyHorizontals(x, z, CITY_CELL_SIZE, 16, randomFactory);

        // Convert to local coordinates
        final int localX = x - cellX * CITY_CELL_SIZE;
        final int localZ = z - cellZ * CITY_CELL_SIZE;

        // Compute consistent “effective” half-widths (to be on block center)
        final double effectiveRoadHalfWidth = ROAD_WIDTH / 2.0;
        final double effectiveAlleyHalfWidth = ALLEY_WIDTH / 2.0;
        final int roadOrAlleyMargin = 1;

        double minX = 0.0;
        double maxX = 0.0;
        for (int i = 0; i < alleyVerticals.length + 1; i++) {
            double potentialMinX = i == 0 ? effectiveRoadHalfWidth : alleyVerticals[i - 1] + effectiveAlleyHalfWidth;
            double potentialMaxX = i == alleyVerticals.length ? CITY_CELL_SIZE - effectiveRoadHalfWidth : alleyVerticals[i] - effectiveAlleyHalfWidth;
            if (potentialMinX <= localX && localX <= potentialMaxX) {
                minX = potentialMinX + roadOrAlleyMargin;
                maxX = potentialMaxX - roadOrAlleyMargin + 0.5;
                break;
            }
        }

        double minZ = 0.0;
        double maxZ = 0.0;
        for (int i = 0; i < alleyHorizontals.length + 1; i++) {
            double potentialMinZ = i == 0 ? effectiveRoadHalfWidth : alleyHorizontals[i - 1] + effectiveAlleyHalfWidth;
            double potentialMaxZ = i == alleyHorizontals.length ? CITY_CELL_SIZE - effectiveRoadHalfWidth : alleyHorizontals[i] - effectiveAlleyHalfWidth;
            if (potentialMinZ <= localZ && localZ <= potentialMaxZ) {
                minZ = potentialMinZ + roadOrAlleyMargin;
                maxZ = potentialMaxZ - roadOrAlleyMargin + 0.5;
                break;
            }
        }

        return new PlotInfo((int) minX + cellX * CITY_CELL_SIZE, (int) minZ + cellZ * CITY_CELL_SIZE, maxX - minX, maxZ - minZ);
    }

    private LocalMaxima findLocalMaxima(int x, int z, final int maxSteps, final BiFunction<Integer, Integer, Double> func) {
        double maxValue = 0.0;
        for (int step = 0; step < maxSteps; step++) {
            double currentValue = func.apply(x, z);
            double xPositive = func.apply(x + 1, z);
            double xNegative = func.apply(x - 1, z);
            double zPositive = func.apply(x, z + 1);
            double zNegative = func.apply(x, z - 1);

            // move toward steepest ascent
            maxValue = currentValue;
            int dx = 0, dz = 0;
            if (xPositive > maxValue) {
                maxValue = xPositive;
                dx = 1;
                dz = 0;
            }
            if (xNegative > maxValue) {
                maxValue = xNegative;
                dx = -1;
                dz = 0;
            }
            if (zPositive > maxValue) {
                maxValue = zPositive;
                dx = 0;
                dz = 1;
            }
            if (zNegative > maxValue) {
                maxValue = zNegative;
                dx = 0;
                dz = -1;
            }

            if (dx == 0 && dz == 0) {
                // Found the peak
                break;
            }
            x += dx;
            z += dz;
        }
        return new LocalMaxima(x, z, maxValue);
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

    private record RiverInfo(double mask, double depth) {
    }

    private record LocalMaxima(int x, int z, double value) {
    }

    private record PlotInfo(int originX, int originZ, double sizeX, double sizeZ) {
        public boolean contains(int x, int z) {
            return x >= originX && x < originX + sizeX
                    && z >= originZ && z < originZ + sizeZ;
        }
    }
}
