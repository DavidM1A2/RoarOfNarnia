package com.dslovikosky.narnia.common.network.packet;

import com.dslovikosky.narnia.common.constants.Constants;
import com.dslovikosky.narnia.common.utils.CustomStreamCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public record SetPositionalMarkerDataPacket(BlockPos blockPos, String name, Vec3 offset) implements CustomPacketPayload {
    public static final StreamCodec<RegistryFriendlyByteBuf, SetPositionalMarkerDataPacket> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC,
            SetPositionalMarkerDataPacket::blockPos,
            ByteBufCodecs.STRING_UTF8,
            SetPositionalMarkerDataPacket::name,
            CustomStreamCodec.VEC3,
            SetPositionalMarkerDataPacket::offset,
            SetPositionalMarkerDataPacket::new
    );
    public static final CustomPacketPayload.Type<SetPositionalMarkerDataPacket> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "set_positional_marker_data"));

    @Override
    public @NotNull Type<SetPositionalMarkerDataPacket> type() {
        return TYPE;
    }
}
