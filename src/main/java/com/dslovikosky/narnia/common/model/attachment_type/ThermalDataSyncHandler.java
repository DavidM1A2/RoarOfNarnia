package com.dslovikosky.narnia.common.model.attachment_type;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.attachment.AttachmentSyncHandler;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class ThermalDataSyncHandler implements AttachmentSyncHandler<ThermalData> {
    @Override
    public boolean sendToPlayer(IAttachmentHolder holder, ServerPlayer to) {
        return holder == to;
    }

    @Override
    public void write(RegistryFriendlyByteBuf buf, ThermalData attachment, boolean initialSync) {
        ThermalData.STREAM_CODEC.encode(buf, attachment);
    }

    @Override
    public @Nullable ThermalData read(IAttachmentHolder holder, RegistryFriendlyByteBuf buf, @Nullable ThermalData previousValue) {
        return ThermalData.STREAM_CODEC.decode(buf);
    }
}
