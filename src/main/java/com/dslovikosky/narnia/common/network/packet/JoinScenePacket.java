package com.dslovikosky.narnia.common.network.packet;

import com.dslovikosky.narnia.common.constants.Constants;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public record JoinScenePacket() implements CustomPacketPayload {
    public static final StreamCodec<RegistryFriendlyByteBuf, JoinScenePacket> STREAM_CODEC = StreamCodec.unit(new JoinScenePacket());
    public static final CustomPacketPayload.Type<JoinScenePacket> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "join_scene"));

    @Override
    public @NotNull Type<JoinScenePacket> type() {
        return TYPE;
    }
}
