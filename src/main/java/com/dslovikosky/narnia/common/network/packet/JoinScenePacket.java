package com.dslovikosky.narnia.common.network.packet;

import com.dslovikosky.narnia.common.constants.Constants;
import com.dslovikosky.narnia.common.constants.ModRegistries;
import com.dslovikosky.narnia.common.model.scene.Character;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public record JoinScenePacket(Optional<Character> character) implements CustomPacketPayload {
    public static final StreamCodec<RegistryFriendlyByteBuf, JoinScenePacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.registry(ModRegistries.CHARACTER_KEY).apply(ByteBufCodecs::optional), JoinScenePacket::character,
            JoinScenePacket::new
    );
    public static final CustomPacketPayload.Type<JoinScenePacket> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "join_scene"));

    public JoinScenePacket(final Character character) {
        this(Optional.of(character));
    }

    public JoinScenePacket() {
        this(Optional.empty());
    }

    @Override
    public @NotNull Type<JoinScenePacket> type() {
        return TYPE;
    }
}
