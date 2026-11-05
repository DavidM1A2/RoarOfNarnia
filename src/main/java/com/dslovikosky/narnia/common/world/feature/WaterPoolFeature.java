package com.dslovikosky.narnia.common.world.feature;

import com.dslovikosky.narnia.common.utils.EllipsoidUtils;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.util.valueproviders.IntProviders;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public record WaterPoolFeature(
        Holder<BlockStateProvider> fluid,
        IntProvider width,
        IntProvider length,
        IntProvider depth
) implements Feature {
    public static final MapCodec<WaterPoolFeature> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                            BlockStateProvider.CODEC.fieldOf("fluid").forGetter(WaterPoolFeature::fluid),
                            IntProviders.CODEC.fieldOf("width").forGetter(WaterPoolFeature::width),
                            IntProviders.CODEC.fieldOf("length").forGetter(WaterPoolFeature::length),
                            IntProviders.CODEC.fieldOf("depth").forGetter(WaterPoolFeature::depth)
                    )
                    .apply(instance, WaterPoolFeature::new)
    );

    @Override
    public MapCodec<WaterPoolFeature> codec() {
        return CODEC;
    }

    @Override
    public boolean place(final WorldGenLevel level, final ChunkGenerator chunkGenerator, final RandomSource random, final BlockPos origin) {
        final BlockPos waterLevelPos = origin.below();

        final BlockStateProvider fluidProvider = fluid.value();
        final int width = width().sample(random);
        final int length = length().sample(random);
        final int depth = depth().sample(random);

        if (waterLevelPos.getY() <= level.getMinY() + depth + 1) {
            return false;
        }

        final int widthBoundingBox = width / 2 + 2;
        final int lengthBoundingBox = length / 2 + 2;
        for (int x = -widthBoundingBox; x <= widthBoundingBox; x++) {
            for (int z = -lengthBoundingBox; z <= lengthBoundingBox; z++) {
                for (int y = -depth; y < 0; y++) {
                    final BlockPos currentBlockPos = waterLevelPos.offset(x, y, z);

                    if (!EllipsoidUtils.isInsideEllipsoid(0.0, 0.0, 0.0, width / 2.0, depth, length / 2.0, x, y, z)) {
                        continue;
                    }

                    final BlockState newBlockState = fluidProvider.getState(level, random, currentBlockPos);
                    final BlockState oldBlockState = level.getBlockState(currentBlockPos);
                    if (!oldBlockState.is(BlockTags.FEATURES_CANNOT_REPLACE) && !oldBlockState.isAir()) {
                        level.setBlock(currentBlockPos, newBlockState, 2);
                    }
                }
                final BlockPos currentBlockPos = waterLevelPos.offset(x, 0, z);

                if (!EllipsoidUtils.isInsideEllipsoid(0.0, 0.0, 0.0, widthBoundingBox, depth, lengthBoundingBox, x, 0, z)) {
                    continue;
                }

                final BlockState oldBlockState = level.getBlockState(currentBlockPos);
                if (!oldBlockState.is(BlockTags.FEATURES_CANNOT_REPLACE) && !oldBlockState.isAir()) {
                    level.setBlock(currentBlockPos, Blocks.AIR.defaultBlockState(), 2);
                    if (level.getBlockState(currentBlockPos.below()).is(Blocks.DIRT)) {
                        level.setBlock(currentBlockPos.below(), Blocks.GRASS_BLOCK.defaultBlockState(), 2);
                    }
                }
            }
        }

        return false;
    }
}
