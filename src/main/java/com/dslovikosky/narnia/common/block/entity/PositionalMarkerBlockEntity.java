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
    public static final String NBT_NAME = "name";
    public static final String NBT_OFFSET_X = "offset_x";
    public static final String NBT_OFFSET_Y = "offset_y";
    public static final String NBT_OFFSET_Z = "offset_z";

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
    public void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.saveAdditional(tag, provider);
        tag.putString(NBT_NAME, name);
        tag.putDouble(NBT_OFFSET_X, offset.x);
        tag.putDouble(NBT_OFFSET_Y, offset.y);
        tag.putDouble(NBT_OFFSET_Z, offset.z);
    }

    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.loadAdditional(tag, provider);
        name = tag.getString(NBT_NAME);
        offset = new Vec3(tag.getDouble(NBT_OFFSET_X), tag.getDouble(NBT_OFFSET_Y), tag.getDouble(NBT_OFFSET_Z));
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
