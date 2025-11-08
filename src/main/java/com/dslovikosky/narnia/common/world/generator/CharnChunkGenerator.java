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
            final SimplexNoise detailNoise = new SimplexNoise(randomState.getOrCreateRandomFactory(RANDOM).fromHashOf(TERRAIN));

            for (int x = startX; x < startX + 16; x++) {
                for (int z = startZ; z < startZ + 16; z++) {
                    // ---- Distance & Plateau ----
                    final double dist = Math.sqrt(x * x + z * z);
                    final double radius = 400.0;
                    final double plateau = 35.0 * Math.pow(Math.max(0.0, 1.0 - (dist / radius)), 2.0);

                    // ---- Small-scale variation ----
                    final double baseNoise = detailNoise.getValue(x * 0.01, z * 0.01);
                    final double midNoise = detailNoise.getValue(x * 0.04, z * 0.04);
                    final double shaped = baseNoise * 4.0 + (midNoise * 1.2);

                    // ---- Flatten near center ----
                    final double flatness = Math.min(1.0, dist / (radius * 0.6));
                    final double detail = shaped * flatness;

                    // ---- Combine ----
                    final int height = (int) (64 + plateau + detail);

                    // ---- Place blocks ----
                    chunk.setBlockState(new BlockPos(x, chunk.getMinY(), z), Blocks.BEDROCK.defaultBlockState());
                    for (int y = chunk.getMinY() + 1; y < height; y++) {
                        chunk.setBlockState(new BlockPos(x, y, z), Blocks.SANDSTONE.defaultBlockState());
                    }
                    chunk.setBlockState(new BlockPos(x, height, z), Blocks.SAND.defaultBlockState());
                }
            }

            return chunk;
        });
    }

    @Override
    public int getBaseHeight(int x, int z, Heightmap.Types type, LevelHeightAccessor level, RandomState random) {
        final SimplexNoise detailNoise = new SimplexNoise(random.getOrCreateRandomFactory(RANDOM).fromHashOf(TERRAIN));
        final double dist = Math.sqrt(x * x + z * z);
        final double radius = 400.0;
        final double plateau = 35.0 * Math.pow(Math.max(0.0, 1.0 - (dist / radius)), 2.0);
        final double baseNoise = detailNoise.getValue(x * 0.01, z * 0.01);
        final double midNoise = detailNoise.getValue(x * 0.04, z * 0.04);
        final double shaped = baseNoise * 4.0 + (midNoise * 1.2);
        final double flatness = Math.min(1.0, dist / (radius * 0.6));
        final double detail = shaped * flatness;
        return (int) (64 + plateau + detail);
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
