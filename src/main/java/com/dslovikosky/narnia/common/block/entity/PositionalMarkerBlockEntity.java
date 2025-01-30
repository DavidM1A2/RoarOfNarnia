package com.dslovikosky.narnia.common.block.entity;

import com.dslovikosky.narnia.common.constants.ModBlockEntities;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class PositionalMarkerBlockEntity extends BlockEntity {
    private String name = "";
    private Vec3 offset = Vec3.ZERO;

    public PositionalMarkerBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ModBlockEntities.POSITIONAL_MARKER.get(), blockPos, blockState);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider provider) {
        final CompoundTag tag = new CompoundTag();
        saveAdditional(tag, provider);
        return tag;
    }

    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.saveAdditional(tag, provider);
        tag.putString("name", name);
        tag.putDouble("offset_x", offset.x);
        tag.putDouble("offset_y", offset.y);
        tag.putDouble("offset_z", offset.z);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.loadAdditional(tag, provider);
        name = tag.getString("name");
        offset = new Vec3(tag.getDouble("offset_x"), tag.getDouble("offset_y"), tag.getDouble("offset_z"));
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public Vec3 getOffset() {
        return offset;
    }

    public void setOffset(final Vec3 offset) {
        this.offset = offset;
    }
}
