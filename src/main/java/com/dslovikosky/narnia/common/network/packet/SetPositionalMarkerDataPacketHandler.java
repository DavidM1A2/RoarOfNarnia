package com.dslovikosky.narnia.common.network.packet;

import com.dslovikosky.narnia.common.block.entity.PositionalMarkerBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;
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
            final String currentName = marker.getName();
            final Vec3 currentOffset = marker.getOffset();
            final String newName = payload.name();
            final Vec3 newOffset = payload.offset();
            boolean changed = false;
            if (!newName.equals(currentName)) {
                marker.setName(newName);
                changed = true;
            }
            if (!newOffset.equals(currentOffset)) {
                marker.setOffset(newOffset);
                changed = true;
            }
            if (changed) {
                blockEntity.setChanged();
            }
        }
    }
}
