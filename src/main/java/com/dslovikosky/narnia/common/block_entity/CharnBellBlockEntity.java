package com.dslovikosky.narnia.common.block_entity;

import com.dslovikosky.narnia.common.constants.ModBlockEntities;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class CharnBellBlockEntity extends BlockEntity {
    private boolean hitNorth = true;
    private long lastHitTime = 0;

    public CharnBellBlockEntity(final BlockPos pos, final BlockState blockState) {
        super(ModBlockEntities.CHARN_BELL.get(), pos, blockState);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return this.saveWithoutMetadata(registries);
    }

    @Override
    public void saveWithoutMetadata(ValueOutput output) {
        super.saveWithoutMetadata(output);
        output.putBoolean("hitNorth", hitNorth);
        output.putLong("lastHitTime", lastHitTime);
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void onDataPacket(Connection net, ValueInput valueInput) {
        super.onDataPacket(net, valueInput);
        this.hitNorth = valueInput.getBooleanOr("hitNorth", true);
        this.lastHitTime = valueInput.getLongOr("lastHitTime", 0);
    }

    public boolean isHitNorth() {
        return hitNorth;
    }

    public void setHitNorth(final boolean hitNorth) {
        this.hitNorth = hitNorth;
    }

    public long getLastHitTime() {
        return lastHitTime;
    }

    public void setLastHitTime(final long lastHitTime) {
        this.lastHitTime = lastHitTime;
    }
}
