package com.dslovikosky.narnia.common.world.structure;

import com.dslovikosky.narnia.common.constants.ModBiomeTags;
import com.dslovikosky.narnia.common.constants.ModSchematics;
import com.dslovikosky.narnia.common.constants.ModStructureTypes;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.QuartPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Optional;

@ParametersAreNonnullByDefault
public class UncleAndrewsHouseStructure extends Structure {
    public static final MapCodec<UncleAndrewsHouseStructure> CODEC = simpleCodec(UncleAndrewsHouseStructure::new);

    protected UncleAndrewsHouseStructure(final StructureSettings pSettings) {
        super(pSettings);
    }

    @Override
    protected @NotNull Optional<GenerationStub> findGenerationPoint(final GenerationContext pContext) {
        final ChunkPos chunkPos = pContext.chunkPos();
        final int xPos = chunkPos.getMinBlockX();
        final int zPos = chunkPos.getMinBlockZ();
        final RandomState randomState = pContext.randomState();
        final Climate.Sampler sampler = randomState.sampler();

        final Direction direction = Direction.Plane.HORIZONTAL.getRandomDirection(pContext.random());
        final int width;
        final int length;
        if (direction == Direction.EAST || direction == Direction.WEST) {
            length = ModSchematics.UNCLE_ANDREWS_HOUSE.width();
            width = ModSchematics.UNCLE_ANDREWS_HOUSE.length();
        } else {
            length = ModSchematics.UNCLE_ANDREWS_HOUSE.length();
            width = ModSchematics.UNCLE_ANDREWS_HOUSE.width();
        }

        final List<Integer> heights = estimateHeights(xPos, zPos, width, length, Heightmap.Types.WORLD_SURFACE, pContext.chunkGenerator(), pContext.heightAccessor(), randomState);
        final Integer minY = heights.stream().min(Integer::compareTo).get();
        final Integer maxY = heights.stream().max(Integer::compareTo).get();

        if (maxY - minY > 8) {
            return Optional.empty();
        }
        final int yPos = minY + 1;

        final List<Holder<Biome>> biomes = estimateBiomes(xPos, yPos, zPos, width, length, pContext.biomeSource(), sampler);
        if (biomes.stream().anyMatch(it -> !it.is(ModBiomeTags.HAS_UNCLE_ANDREWS_HOUSE))) {
            return Optional.empty();
        }

        final BlockPos blockPos = new BlockPos(xPos, yPos, zPos);
        return Optional.of(new GenerationStub(blockPos, builder ->
                builder.addPiece(new SchematicStructurePiece(blockPos.getX(), blockPos.getY(), blockPos.getZ(), ModSchematics.UNCLE_ANDREWS_HOUSE, direction))));
    }

    private List<Integer> estimateHeights(final int xPos, final int zPos, final int width, final int length, final Heightmap.Types heightmap,
                                          final ChunkGenerator generator, final LevelHeightAccessor levelHeightAccessor, final RandomState random) {
        return List.of(
                generator.getFirstOccupiedHeight(xPos, zPos, heightmap, levelHeightAccessor, random),
                generator.getFirstOccupiedHeight(xPos + width - 1, zPos, heightmap, levelHeightAccessor, random),
                generator.getFirstOccupiedHeight(xPos, zPos + length - 1, heightmap, levelHeightAccessor, random),
                generator.getFirstOccupiedHeight(xPos + width - 1, zPos + length - 1, heightmap, levelHeightAccessor, random),
                generator.getFirstOccupiedHeight(xPos + width / 2, zPos + length / 2, heightmap, levelHeightAccessor, random)
        );
    }

    private List<Holder<Biome>> estimateBiomes(final int xPos, final int yPos, final int zPos, final int width, final int length, final BiomeSource biomeSource, final Climate.Sampler sampler) {
        return List.of(
                biomeSource.getNoiseBiome(QuartPos.fromBlock(xPos), QuartPos.fromBlock(yPos), QuartPos.fromBlock(zPos), sampler),
                biomeSource.getNoiseBiome(QuartPos.fromBlock(xPos + width - 1), QuartPos.fromBlock(yPos), QuartPos.fromBlock(zPos), sampler),
                biomeSource.getNoiseBiome(QuartPos.fromBlock(xPos), QuartPos.fromBlock(yPos), QuartPos.fromBlock(zPos + length - 1), sampler),
                biomeSource.getNoiseBiome(QuartPos.fromBlock(xPos + width - 1), QuartPos.fromBlock(yPos), QuartPos.fromBlock(zPos + length - 1), sampler),
                biomeSource.getNoiseBiome(QuartPos.fromBlock(xPos + width / 2), QuartPos.fromBlock(yPos), QuartPos.fromBlock(zPos + length / 2), sampler)
        );
    }

    @Override
    public @NotNull StructureType<?> type() {
        return ModStructureTypes.UNCLE_ANDREWS_HOUSE.get();
    }
}
