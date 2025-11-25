package com.dslovikosky.narnia.common.block;

import com.dslovikosky.narnia.common.block_entity.CharnBellBlockEntity;
import com.dslovikosky.narnia.common.constants.Constants;
import com.mojang.serialization.MapCodec;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class CharnBellBlock extends HorizontalDirectionalBlock implements EntityBlock {
    private static final VoxelShape SHAPE_NS = Shapes.or(
            box(2.0, 1.0, 7.5, 14.0, 16.0, 8.5),
            box(0.0, 0.0, 0.0, 16.0, 1.0, 16.0)
    );
    private static final VoxelShape SHAPE_EW = Shapes.or(
            box(7.5, 1.0, 2.0, 8.5, 16.0, 14.0),
            box(0.0, 0.0, 0.0, 16.0, 1.0, 16.0)
    );

    private static final MapCodec<CharnBellBlock> CODEC = simpleCodec(CharnBellBlock::new);

    public CharnBellBlock() {
        this(Properties.of()
                .noOcclusion()
                .mapColor(MapColor.COLOR_BLACK)
                .instrument(NoteBlockInstrument.BASEDRUM)
                .strength(50.0F, 1200.0F)
                .setId(ResourceKey.create(Registries.BLOCK, Constants.modLocation("charn_bell"))));
    }

    private CharnBellBlock(final Properties properties) {
        super(properties);
    }

    @Override
    protected InteractionResult useWithoutItem(final BlockState state, final Level level, final BlockPos pos, final Player player, final BlockHitResult hitResult) {
        final BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof CharnBellBlockEntity charnBellBlockEntity) {
            final Direction direction = state.getValue(FACING);
            final double dx = player.getX() - pos.getX() - 0.5;
            final double dz = player.getZ() - pos.getZ() - 0.5;
            final boolean hitNorth = switch (direction) {
                case NORTH -> dz < 0;
                case SOUTH -> dz > 0;
                case WEST -> dx < 0;
                case EAST -> dx > 0;
                default -> false;
            };

            charnBellBlockEntity.setHitNorth(hitNorth);
            charnBellBlockEntity.setLastHitTime(System.currentTimeMillis());
            if (!level.isClientSide()) {
                // Trigger an update to sync other clients
                level.sendBlockUpdated(pos, state, state, Block.UPDATE_ALL);
            }
            return InteractionResult.SUCCESS;
        }
        return super.useWithoutItem(state, level, pos, player, hitResult);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(final BlockPos pos, final BlockState state) {
        return new CharnBellBlockEntity(pos, state);
    }

    @Override
    protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(final StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        return defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        final Direction direction = state.getValue(FACING);
        return direction == Direction.WEST || direction == Direction.EAST ? SHAPE_EW : SHAPE_NS;
    }
}
