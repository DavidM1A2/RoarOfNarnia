package com.dslovikosky.narnia.common.network.packet;

import com.dslovikosky.narnia.common.constants.Constants;
import com.dslovikosky.narnia.common.model.chapter.Scene;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public record SyncNarniaGlobalDataPacket(Optional<Scene> instance) implements CustomPacketPayload {
    public static final StreamCodec<RegistryFriendlyByteBuf, SyncNarniaGlobalDataPacket> STREAM_CODEC = StreamCodec.composite(
            Scene.STREAM_CODEC.apply(ByteBufCodecs::optional),
            SyncNarniaGlobalDataPacket::instance,
            SyncNarniaGlobalDataPacket::new
    );
    public static final CustomPacketPayload.Type<SyncNarniaGlobalDataPacket> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "sync_narnia_global_data"));

    @Override
    public @NotNull Type<SyncNarniaGlobalDataPacket> type() {
        return TYPE;
    }
}
