package com.dslovikosky.narnia.common.model.attachment_type;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.attachment.AttachmentSyncHandler;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class VitaeSyncHandler implements AttachmentSyncHandler<Double> {
    @Override
    public boolean sendToPlayer(IAttachmentHolder holder, ServerPlayer to) {
        return holder == to;
    }

    @Override
    public void write(RegistryFriendlyByteBuf buf, Double attachment, boolean initialSync) {
        ByteBufCodecs.DOUBLE.encode(buf, attachment);
    }

    @Override
    public @Nullable Double read(IAttachmentHolder holder, RegistryFriendlyByteBuf buf, @Nullable Double previousValue) {
        return ByteBufCodecs.DOUBLE.decode(buf);
    }
}
