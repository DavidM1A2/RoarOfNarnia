package com.dslovikosky.narnia.common.model.attachment_type;

import com.dslovikosky.narnia.common.constants.ModRegistries;
import com.dslovikosky.narnia.common.spell.component.powerSource.base.SpellPowerSource;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.attachment.AttachmentSyncHandler;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Objects;

@ParametersAreNonnullByDefault
public class SelectedSpellPowerSourceSyncHandler implements AttachmentSyncHandler<SpellPowerSource<?>> {
    @Override
    public boolean sendToPlayer(IAttachmentHolder holder, ServerPlayer to) {
        return holder == to;
    }

    @Override
    public void write(RegistryFriendlyByteBuf buf, SpellPowerSource<?> attachment, boolean initialSync) {
        if (initialSync) {
            ByteBufCodecs.registry(ModRegistries.SPELL_POWER_SOURCES_KEY).encode(buf, attachment);
        }
    }

    @Override
    public @Nullable SpellPowerSource<?> read(IAttachmentHolder holder, RegistryFriendlyByteBuf buf, @Nullable SpellPowerSource<?> previousValue) {
        return Objects.requireNonNullElseGet(previousValue, () -> ByteBufCodecs.registry(ModRegistries.SPELL_POWER_SOURCES_KEY).decode(buf));
    }
}
