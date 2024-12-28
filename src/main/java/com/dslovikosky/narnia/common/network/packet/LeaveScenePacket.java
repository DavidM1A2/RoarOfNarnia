package com.dslovikosky.narnia.common.network.packet;

import com.dslovikosky.narnia.common.constants.Constants;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public record LeaveScenePacket() implements CustomPacketPayload {
    public static final StreamCodec<RegistryFriendlyByteBuf, LeaveScenePacket> STREAM_CODEC = StreamCodec.unit(new LeaveScenePacket());
    public static final Type<LeaveScenePacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "leave_scene"));

    @Override
    public @NotNull Type<LeaveScenePacket> type() {
        return TYPE;
    }
}
