package com.dslovikosky.narnia.common.network.packet;

import com.dslovikosky.narnia.common.block.entity.PositionalMarkerBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.handling.IPayloadHandler;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class SetPositionalMarkerDataPacketHandler implements IPayloadHandler<SetPositionalMarkerDataPacket> {
    @Override
    public void handle(SetPositionalMarkerDataPacket payload, IPayloadContext context) {
        final Level level = context.player().level();
        final BlockPos blockPos = payload.blockPos();
        final BlockEntity blockEntity = level.getBlockEntity(blockPos);
        if (blockEntity instanceof PositionalMarkerBlockEntity marker) {
            marker.setName(payload.name());
            marker.setOffset(payload.offset());
            blockEntity.setChanged();
            level.sendBlockUpdated(blockPos, blockEntity.getBlockState(), blockEntity.getBlockState(), 2);
        }
    }
}
