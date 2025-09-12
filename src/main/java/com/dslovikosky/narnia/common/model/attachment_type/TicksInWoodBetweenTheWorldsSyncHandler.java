package com.dslovikosky.narnia.common.model.attachment_type;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.attachment.AttachmentSyncHandler;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Objects;

@ParametersAreNonnullByDefault
public class TicksInWoodBetweenTheWorldsSyncHandler implements AttachmentSyncHandler<Integer> {
    @Override
    public boolean sendToPlayer(IAttachmentHolder holder, ServerPlayer to) {
        return holder == to;
    }

    @Override
    public void write(RegistryFriendlyByteBuf buf, Integer attachment, boolean initialSync) {
        if (initialSync) {
            ByteBufCodecs.INT.encode(buf, attachment);
        }
    }

    @Override
    public @Nullable Integer read(IAttachmentHolder holder, RegistryFriendlyByteBuf buf, @Nullable Integer previousValue) {
        return Objects.requireNonNullElseGet(previousValue, () -> ByteBufCodecs.INT.decode(buf));
    }
}
