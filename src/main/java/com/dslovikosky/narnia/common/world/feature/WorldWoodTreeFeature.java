package com.dslovikosky.narnia.common.world.feature;

import com.dslovikosky.narnia.common.utils.EllipsoidUtils;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelWriter;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.TreeFeature;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.phys.shapes.BitSetDiscreteVoxelShape;
import net.minecraft.world.phys.shapes.DiscreteVoxelShape;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Iterator;
import java.util.List;
import java.util.OptionalInt;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

@ParametersAreNonnullByDefault
public class WorldWoodTreeFeature extends Feature<WorldWoodTreeConfiguration> {
    public WorldWoodTreeFeature(Codec<WorldWoodTreeConfiguration> pCodec) {
        super(pCodec);
    }

    @Override
    public final boolean place(FeaturePlaceContext<WorldWoodTreeConfiguration> pContext) {
        final WorldGenLevel level = pContext.level();
        final RandomSource randomSource = pContext.random();
        final BlockPos originPos = pContext.origin();
        final WorldWoodTreeConfiguration configuration = pContext.config();

        final Set<BlockPos> rootPositions = Sets.newHashSet();
        final Set<BlockPos> trunkPositions = Sets.newHashSet();
        final Set<BlockPos> leafPositions = Sets.newHashSet();
        final Set<BlockPos> decorPositions = Sets.newHashSet();

        final BiConsumer<BlockPos, BlockState> rootSetter = (blockPos, blockState) -> {
            rootPositions.add(blockPos.immutable());
            setBlockKnownShape(level, blockPos, blockState);
        };
        final BiConsumer<BlockPos, BlockState> trunkSetter = (blockPos, blockState) -> {
            trunkPositions.add(blockPos.immutable());
            setBlockKnownShape(level, blockPos, blockState);
        };
        final FoliagePlacer.FoliageSetter foliageSetter = new FoliagePlacer.FoliageSetter() {
            @Override
            public void set(BlockPos blockPos, BlockState blockState) {
                leafPositions.add(blockPos.immutable());
                setBlockKnownShape(level, blockPos, blockState);
            }

            @Override
            public boolean isSet(BlockPos blockPos) {
                return leafPositions.contains(blockPos);
            }
        };
        boolean placedSuccessfully = this.doPlace(level, randomSource, originPos, rootSetter, trunkSetter, foliageSetter, configuration);
        if (placedSuccessfully && (!trunkPositions.isEmpty() || !leafPositions.isEmpty())) {
            return BoundingBox.encapsulatingPositions(Iterables.concat(rootPositions, trunkPositions, leafPositions, decorPositions)).map(boundingBox -> {
                DiscreteVoxelShape discretevoxelshape = updateLeaves(level, boundingBox, trunkPositions, decorPositions, rootPositions);
                StructureTemplate.updateShapeAtEdge(level, 3, discretevoxelshape, boundingBox.minX(), boundingBox.minY(), boundingBox.minZ());
                return true;
            }).orElse(false);
        } else {
            return false;
        }
    }

    private boolean doPlace(
            WorldGenLevel level,
            RandomSource random,
            BlockPos originPos,
            BiConsumer<BlockPos, BlockState> rootSetter,
            BiConsumer<BlockPos, BlockState> trunkSetter,
            FoliagePlacer.FoliageSetter foliageSetter,
            WorldWoodTreeConfiguration configuration
    ) {
        final int treeHeight = configuration.height().sample(random);
        final double baseTrunkWidth = treeHeight / 4.0;

        final double xCenterShift = -random.nextDouble();
        final double zCenterShift = -random.nextDouble();

        // Check that the trunk is valid
        final AtomicBoolean isValidPlace = new AtomicBoolean(true);
        loopOverTreeTrunkLayerBlocks(0, treeHeight, baseTrunkWidth, xCenterShift, zCenterShift, originPos, blockPos -> {
            if (level.getBlockState(blockPos.below()).liquid()) {
                isValidPlace.set(false);
            }
        });
        if (!isValidPlace.get()) {
            return false;
        }

        // Build the trunk
        final Set<BlockPos> branchTipPositions = Sets.newHashSet();
        for (int layer = 0; layer < treeHeight; layer++) {
            final int finalLayer = layer;
            loopOverTreeTrunkLayerBlocks(layer, treeHeight, baseTrunkWidth, xCenterShift, zCenterShift, originPos, blockPos -> {
                if (TreeFeature.validTreePos(level, blockPos)) {
                    trunkSetter.accept(blockPos, configuration.trunkProvider().getState(random, blockPos));
                }
                if (finalLayer == treeHeight - 1) {
                    branchTipPositions.add(blockPos);
                }
            });
        }

        // Build the branches (only if tree is bigger than 8 blocks
        if (treeHeight > 8) {
            final int branchCount = random.nextIntBetweenInclusive(treeHeight / 8 + 1, treeHeight / 8 + 4);
            final double angleBetweenBranches = 360.0 / branchCount;
            final double startAngle = random.nextDouble() * 360;
            for (int i = 0; i < branchCount; i++) {
                final int branchStartHeight = (int) Math.round(treeHeight * Mth.lerp(random.nextDouble(), 0.5, 0.7));
                final int branchEndHeight = (int) Math.round(treeHeight * Mth.lerp(random.nextDouble(), 0.9, 1.1));
                final int branchWidth = (int) Math.round((treeHeight - branchStartHeight) * Mth.lerp(random.nextDouble(), 0.6, 1.0));
                final double currentBranchAngle = startAngle + angleBetweenBranches * i;
                final Vec3i branchStart = new Vec3i(originPos.getX(), originPos.getY() + branchStartHeight, originPos.getZ());
                final Vec3i branchEnd = new Vec3i(
                        originPos.getX() + (int) Math.round(branchWidth * Math.cos(Math.toRadians(currentBranchAngle))),
                        originPos.getY() + branchEndHeight,
                        originPos.getZ() + (int) Math.round(branchWidth * Math.sin(Math.toRadians(currentBranchAngle))));

                final int numSteps = (int) Math.ceil(Math.sqrt(branchStart.distSqr(branchEnd)));
                double deltaX = (branchEnd.getX() - branchStart.getX()) / (double) numSteps;
                double deltaY = (branchEnd.getY() - branchStart.getY()) / (double) numSteps;
                double deltaZ = (branchEnd.getZ() - branchStart.getZ()) / (double) numSteps;

                for (int step = 1; step <= numSteps; step++) {
                    double x = branchStart.getX() + step * deltaX;
                    double y = branchStart.getY() + step * deltaY;
                    double z = branchStart.getZ() + step * deltaZ;

                    final BlockPos blockPos = new BlockPos((int) Math.round(x), (int) Math.round(y), (int) Math.round(z));
                    if (TreeFeature.validTreePos(level, blockPos)) {
                        trunkSetter.accept(blockPos, configuration.trunkProvider().getState(random, blockPos));
                    }
                    if (step == numSteps) {
                        branchTipPositions.add(blockPos);
                    }
                }
            }
        }

        // Build leaf clusters
        final int leafClusterWidth;
        final int leafClusterHeight = 4;
        if (treeHeight < 5) {
            leafClusterWidth = 5;
        } else if (treeHeight < 7) {
            leafClusterWidth = 6;
        } else if (treeHeight < 10) {
            leafClusterWidth = 7;
        } else {
            leafClusterWidth = 8;
        }
        for (final BlockPos branchTipPos : branchTipPositions) {
            for (int xOffset = -leafClusterWidth / 2; xOffset <= leafClusterWidth / 2; xOffset++) {
                for (int yOffset = -leafClusterHeight / 2; yOffset <= leafClusterHeight / 2; yOffset++) {
                    for (int zOffset = -leafClusterWidth / 2; zOffset <= leafClusterWidth / 2; zOffset++) {
                        if (EllipsoidUtils.isInsideEllipsoid(0, 0, 0, leafClusterWidth / 2.0, leafClusterHeight / 2.0, leafClusterWidth / 2.0, xOffset, yOffset, zOffset)) {
                            final BlockPos leafPos = branchTipPos.offset(xOffset, yOffset, zOffset);
                            if (TreeFeature.validTreePos(level, leafPos)) {
                                foliageSetter.set(leafPos, configuration.foliageProvider().getState(random, leafPos));
                            }
                        }
                    }
                }
            }
        }

        return true;
    }

    private void loopOverTreeTrunkLayerBlocks(final int layer, final int treeHeight, final double baseTrunkWidth, final double xCenterShift, final double zCenterShift, final BlockPos originPos, final Consumer<BlockPos> consumer) {
        double percentWidth = 1.0 - layer / (double) treeHeight;
        final double layerTrunkWidth = Math.max(Math.sqrt(2.0), percentWidth * baseTrunkWidth);
        for (double xOffset = -layerTrunkWidth / 2; xOffset <= layerTrunkWidth / 2; xOffset++) {
            for (double zOffset = -layerTrunkWidth / 2; zOffset <= layerTrunkWidth / 2; zOffset++) {
                final double distance = Math.sqrt(xOffset * xOffset + zOffset * zOffset);
                if (distance < layerTrunkWidth / 2) {
                    consumer.accept(originPos.offset((int) Math.round(xOffset + xCenterShift), layer, (int) Math.round(zOffset + zCenterShift)));
                }
            }
        }
    }

    @Override
    protected void setBlock(LevelWriter level, BlockPos blockPos, BlockState blockState) {
        setBlockKnownShape(level, blockPos, blockState);
    }

    private DiscreteVoxelShape updateLeaves(LevelAccessor level, BoundingBox boundingBox, Set<BlockPos> trunkPositions, Set<BlockPos> decorPositions, Set<BlockPos> rootPositions) {
        final DiscreteVoxelShape discreteVoxelShape = new BitSetDiscreteVoxelShape(boundingBox.getXSpan(), boundingBox.getYSpan(), boundingBox.getZSpan());

        final int iterationCount = 7;
        List<Set<BlockPos>> leafCheckIterations = Lists.newArrayList();
        for (int i = 0; i < iterationCount; i++) {
            leafCheckIterations.add(Sets.newHashSet());
        }

        for (BlockPos blockPos : Lists.newArrayList(Sets.union(decorPositions, rootPositions))) {
            if (boundingBox.isInside(blockPos)) {
                discreteVoxelShape.fill(blockPos.getX() - boundingBox.minX(), blockPos.getY() - boundingBox.minY(), blockPos.getZ() - boundingBox.minZ());
            }
        }

        final BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
        leafCheckIterations.getFirst().addAll(trunkPositions);
        int iteration = 0;
        while (true) {
            while (iteration >= iterationCount || !leafCheckIterations.get(iteration).isEmpty()) {
                if (iteration >= iterationCount) {
                    return discreteVoxelShape;
                }

                final Iterator<BlockPos> iterator = leafCheckIterations.get(iteration).iterator();
                final BlockPos iterationBlockPos = iterator.next();
                iterator.remove();
                if (boundingBox.isInside(iterationBlockPos)) {
                    if (iteration != 0) {
                        BlockState blockstate = level.getBlockState(iterationBlockPos);
                        setBlockKnownShape(level, iterationBlockPos, blockstate.setValue(BlockStateProperties.DISTANCE, iteration));
                    }

                    discreteVoxelShape.fill(iterationBlockPos.getX() - boundingBox.minX(), iterationBlockPos.getY() - boundingBox.minY(), iterationBlockPos.getZ() - boundingBox.minZ());

                    for (final Direction direction : Direction.values()) {
                        mutableBlockPos.setWithOffset(iterationBlockPos, direction);
                        if (boundingBox.isInside(mutableBlockPos)) {
                            int xOffset = mutableBlockPos.getX() - boundingBox.minX();
                            int yOffset = mutableBlockPos.getY() - boundingBox.minY();
                            int zOffset = mutableBlockPos.getZ() - boundingBox.minZ();
                            if (!discreteVoxelShape.isFull(xOffset, yOffset, zOffset)) {
                                final BlockState blockState = level.getBlockState(mutableBlockPos);
                                final OptionalInt leavesDistance = LeavesBlock.getOptionalDistanceAt(blockState);
                                if (leavesDistance.isPresent()) {
                                    final int distance = Math.min(leavesDistance.getAsInt(), iteration + 1);
                                    if (distance < iterationCount) {
                                        leafCheckIterations.get(distance).add(mutableBlockPos.immutable());
                                        iteration = Math.min(iteration, distance);
                                    }
                                }
                            }
                        }
                    }
                }
            }

            iteration++;
        }
    }

    private void setBlockKnownShape(LevelWriter level, BlockPos blockPos, BlockState blockState) {
        level.setBlock(blockPos, blockState, 0b10011);
    }
}
