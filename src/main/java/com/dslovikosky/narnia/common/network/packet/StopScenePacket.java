package com.dslovikosky.narnia.common.network.packet;

import com.dslovikosky.narnia.common.constants.Constants;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public record StopScenePacket() implements CustomPacketPayload {
    public static final StreamCodec<RegistryFriendlyByteBuf, StopScenePacket> STREAM_CODEC = StreamCodec.unit(new StopScenePacket());
    public static final Type<StopScenePacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "stop_scene"));

    @Override
    public @NotNull Type<StopScenePacket> type() {
        return TYPE;
    }
}
