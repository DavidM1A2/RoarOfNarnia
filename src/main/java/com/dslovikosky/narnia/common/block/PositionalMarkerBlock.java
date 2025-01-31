package com.dslovikosky.narnia.common.block;

import com.dslovikosky.narnia.client.proxy.ClientProxy;
import com.dslovikosky.narnia.common.block.entity.PositionalMarkerBlockEntity;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class PositionalMarkerBlock extends Block implements EntityBlock {
    public PositionalMarkerBlock() {
        super(BlockBehaviour.Properties.of()
                .noCollission()
                .noLootTable()
                .noTerrainParticles()
                .pushReaction(PushReaction.DESTROY));
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(final BlockPos blockPos, final BlockState blockState) {
        return new PositionalMarkerBlockEntity(blockPos, blockState);
    }

    @Override
    protected InteractionResult useWithoutItem(final BlockState blockState, final Level level, final BlockPos blockPos, final Player player, final BlockHitResult hitResult) {
        final BlockEntity blockEntity = level.getBlockEntity(blockPos);
        if (level.isClientSide() && blockEntity instanceof PositionalMarkerBlockEntity marker) {
            ClientProxy.openEditPositionalMarkerScreen(blockPos, marker.getName(), marker.getOffset());
        }
        return InteractionResult.SUCCESS;
    }
}
