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
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.blending.Blender;
import net.minecraft.world.level.levelgen.synth.SimplexNoise;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class CharnChunkGenerator extends ChunkGenerator {
    public static final MapCodec<CharnChunkGenerator> CODEC = RecordCodecBuilder.mapCodec(
            it -> it.group(RegistryOps.retrieveElement(ModBiomes.DARK_CITY_RUINS)).apply(it, it.stable(CharnChunkGenerator::new)));
    private static final ResourceLocation RANDOM = Constants.modLocation("charn_noise");
    private static final ResourceLocation TERRAIN = Constants.modLocation("charn_noise_terrain");

    public CharnChunkGenerator(final Holder<Biome> biome) {
        super(new FixedBiomeSource(biome));
    }

    @Override
    public CompletableFuture<ChunkAccess> fillFromNoise(Blender blender, RandomState randomState, StructureManager structureManager, ChunkAccess chunk) {
        return CompletableFuture.supplyAsync(() -> {
            final ChunkPos pos = chunk.getPos();
            final int startX = pos.getMinBlockX();
            final int startZ = pos.getMinBlockZ();
            final SimplexNoise noise = new SimplexNoise(randomState.getOrCreateRandomFactory(RANDOM).fromHashOf(TERRAIN));

            for (int x = startX; x < startX + 16; x++) {
                for (int z = startZ; z < startZ + 16; z++) {
                    final double baseHeight = computeBaseTerrain(x, z, noise, 64.0);

                    final RiverInfo river = computeRiver(x, z, noise, 200.0);

                    final double riverMask = river.mask();
                    final double riverDepth = river.depth();

                    final double roadMask = computeRoadMask(x, z, 0, 0);

                    final boolean isRoadCenter = roadMask > 0.4;
                    final boolean isRoadEdge = roadMask > 0;
                    final boolean isRiver = riverMask > 0.1;

                    final int groundHeight;
                    if (isRoadCenter || isRoadEdge) {
                        // "Flatten" the height around roads so roads are flat
                        final Pair<Double, Double> roadCenter = findRoadCenter(x, z, 4, 0, 0);
                        final double roadHeight = computeBaseTerrain(roadCenter.getLeft(), roadCenter.getRight(), noise, 64.0);
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
                    }
                }
            }

            return chunk;
        });
    }

    @Override
    public int getBaseHeight(int x, int z, Heightmap.Types type, LevelHeightAccessor level, RandomState random) {
        final SimplexNoise detailNoise = new SimplexNoise(random.getOrCreateRandomFactory(RANDOM).fromHashOf(TERRAIN));

        final double baseHeight = computeBaseTerrain(x, z, detailNoise, 64.0);

        final RiverInfo river = computeRiver(x, z, detailNoise, 200.0);

        final double riverDepth = river.depth();
        return (int) (baseHeight - riverDepth);
    }

    private double computeBaseTerrain(final double x, final double z, final SimplexNoise noise, final double baseY) {
        double mountainHeight = computeMountainHeight(x, z, noise);
        double detail = computeTerrainDetail(x, z, noise);
        return baseY + mountainHeight + detail;
    }

    private double computeTerrainDetail(final double x, final double z, final SimplexNoise noise) {
        // Multi-frequency noise for fractal detail
        double baseNoise = noise.getValue(x * 0.01, z * 0.01);
        double midNoise = noise.getValue(x * 0.03, z * 0.03);
        return baseNoise * 3.0 + midNoise * 1.2;
    }

    private double computeMountainHeight(final double x, final double z, final SimplexNoise noise) {
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

    private RiverInfo computeRiver(final double x, final double z, final SimplexNoise noise, final double domeRadius) {
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

        // --- Step 3: Plateau suppression ---
        // Keep rivers out of the central plateau region
        double dist = Math.sqrt(x * x + z * z);
        double plateauInfluence = Mth.clamp(dist / (domeRadius * 0.6), 0.0, 1.0);
        riverMask *= plateauInfluence;

        // --- Step 4: Depth scaling ---
        double riverDepth = riverMask * 6.0;

        return new RiverInfo(riverMask, riverDepth);
    }

    private Pair<Double, Double> findRoadCenter(double x, double z, int maxSteps, double cityCenterX, double cityCenterZ) {
        for (int step = 0; step < maxSteps; step++) {
            double mask = computeRoadMask(x, z, cityCenterX, cityCenterZ);
            if (mask > 0.96) break; // early exit if we're at the center

            double maskXPlus = computeRoadMask(x + 1, z, cityCenterX, cityCenterZ);
            double maskXMinus = computeRoadMask(x - 1, z, cityCenterX, cityCenterZ);
            double maskZPlus = computeRoadMask(x, z + 1, cityCenterX, cityCenterZ);
            double maskZMinus = computeRoadMask(x, z - 1, cityCenterX, cityCenterZ);

            // move toward steepest ascent
            double maxMask = mask;
            double dx = 0, dz = 0;
            if (maskXPlus > maxMask) {
                maxMask = maskXPlus;
                dx = 1;
                dz = 0;
            }
            if (maskXMinus > maxMask) {
                maxMask = maskXMinus;
                dx = -1;
                dz = 0;
            }
            if (maskZPlus > maxMask) {
                maxMask = maskZPlus;
                dx = 0;
                dz = 1;
            }
            if (maskZMinus > maxMask) {
                maxMask = maskZMinus;
                dx = 0;
                dz = -1;
            }

            if (dx == 0 && dz == 0) break; // no improvement, we're at peak
            x += dx;
            z += dz;
        }
        return Pair.of(x, z);
    }

    private double computeRoadMask(final double x, final double z, final double cityCenterX, final double cityCenterZ) {
        final double roadSpacing = 128.0;
        final double roadHalfWidth = 4.0;

        double dx = x - cityCenterX;
        double dz = z - cityCenterZ;

        // Find distance to the nearest vertical and horizontal road centerlines
        double distToXCenter = Math.abs(Mth.positiveModulo(dx + roadSpacing / 2.0, roadSpacing) - roadSpacing / 2.0);
        double distToZCenter = Math.abs(Mth.positiveModulo(dz + roadSpacing / 2.0, roadSpacing) - roadSpacing / 2.0);

        // Distance to the closest road (either along X or Z)
        double dist = Math.min(distToXCenter, distToZCenter);

        // Convert distance to mask using a smooth falloff:
        // 1.0 at dist = 0, fades smoothly to 0.0 at dist = roadHalfWidth
        if (dist >= roadHalfWidth) {
            return 0.0;
        } else {
            return 0.5 * (Math.cos(Math.PI * dist / roadHalfWidth) + 1.0);
        }
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

    private record RoadMask(double total, double center, double edge) {
    }
}
