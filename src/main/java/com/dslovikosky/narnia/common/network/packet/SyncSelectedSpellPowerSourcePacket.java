package com.dslovikosky.narnia.common.network.packet;

import com.dslovikosky.narnia.common.constants.Constants;
import com.dslovikosky.narnia.common.constants.ModRegistries;
import com.dslovikosky.narnia.common.spell.component.powerSource.base.SpellPowerSource;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public record SyncSelectedSpellPowerSourcePacket(SpellPowerSource<?> selectedSpellPowerSource) implements CustomPacketPayload {
    public static final StreamCodec<RegistryFriendlyByteBuf, SyncSelectedSpellPowerSourcePacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.registry(ModRegistries.SPELL_POWER_SOURCES_KEY),
            SyncSelectedSpellPowerSourcePacket::selectedSpellPowerSource,
            SyncSelectedSpellPowerSourcePacket::new
    );
    public static final CustomPacketPayload.Type<SyncSelectedSpellPowerSourcePacket> TYPE =
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "sync_selected_spell_power_source"));

    @Override
    public @NotNull Type<SyncSelectedSpellPowerSourcePacket> type() {
        return TYPE;
    }
}
