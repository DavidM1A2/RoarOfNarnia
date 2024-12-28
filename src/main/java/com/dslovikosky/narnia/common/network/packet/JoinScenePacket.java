package com.dslovikosky.narnia.common.network.packet;

import com.dslovikosky.narnia.common.constants.Constants;
import com.dslovikosky.narnia.common.model.chapter.PlayableCharacter;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public record JoinScenePacket(ResourceLocation characterId) implements CustomPacketPayload {
    public static final StreamCodec<RegistryFriendlyByteBuf, JoinScenePacket> STREAM_CODEC = StreamCodec.composite(
            ResourceLocation.STREAM_CODEC, JoinScenePacket::characterId,
            JoinScenePacket::new
    );
    public static final CustomPacketPayload.Type<JoinScenePacket> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "join_scene"));

    public JoinScenePacket(final PlayableCharacter character) {
        this(character.id());
    }

    @Override
    public @NotNull Type<JoinScenePacket> type() {
        return TYPE;
    }
}
