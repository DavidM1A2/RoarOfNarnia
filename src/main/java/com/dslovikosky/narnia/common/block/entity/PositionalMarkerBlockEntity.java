package com.dslovikosky.narnia.common.block.entity;

import com.dslovikosky.narnia.common.constants.ModBlockEntities;
import com.mojang.logging.LogUtils;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.TagValueOutput;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class PositionalMarkerBlockEntity extends BlockEntity {
    public static final String NBT_NAME = "name";
    public static final String NBT_OFFSET_X = "offset_x";
    public static final String NBT_OFFSET_Y = "offset_y";
    public static final String NBT_OFFSET_Z = "offset_z";
    private static final Logger LOGGER = LogUtils.getLogger();
    private String name = "";
    private Vec3 offset = Vec3.ZERO;

    public PositionalMarkerBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ModBlockEntities.POSITIONAL_MARKER.get(), blockPos, blockState);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider provider) {
        try (ProblemReporter.ScopedCollector problemReporter = new ProblemReporter.ScopedCollector(this.problemPath(), LOGGER)) {
            final TagValueOutput tagValueOutput = TagValueOutput.createWithContext(problemReporter, provider);
            return tagValueOutput.buildResult();
        }
    }

    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        output.putString(NBT_NAME, name);
        output.putDouble(NBT_OFFSET_X, offset.x);
        output.putDouble(NBT_OFFSET_Y, offset.y);
        output.putDouble(NBT_OFFSET_Z, offset.z);
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        name = input.getString(NBT_NAME).get();
        offset = new Vec3(input.getDoubleOr(NBT_OFFSET_X, 0), input.getDoubleOr(NBT_OFFSET_Y, 0), input.getDoubleOr(NBT_OFFSET_Z, 0));
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
