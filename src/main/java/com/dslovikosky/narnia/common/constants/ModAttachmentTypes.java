package com.dslovikosky.narnia.common.constants;

import com.dslovikosky.narnia.common.model.attachment_type.PreRingTeleportData;
import com.dslovikosky.narnia.common.model.attachment_type.SelectedSpellPowerSourceSyncHandler;
import com.dslovikosky.narnia.common.model.attachment_type.TicksInWoodBetweenTheWorldsSyncHandler;
import com.dslovikosky.narnia.common.model.attachment_type.VitaeSyncHandler;
import com.dslovikosky.narnia.common.spell.component.powerSource.base.SpellPowerSource;
import com.mojang.serialization.Codec;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class ModAttachmentTypes {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, Constants.MOD_ID);

    public static final DeferredHolder<AttachmentType<?>, AttachmentType<PreRingTeleportData>> PRE_RING_TELEPORT_DATA = ATTACHMENT_TYPES
            .register("pre_ring_teleport_data", () -> AttachmentType.builder(PreRingTeleportData::new)
                    .serialize(PreRingTeleportData.CODEC.fieldOf("value"))
                    .build());
    public static final DeferredHolder<AttachmentType<?>, AttachmentType<Integer>> TICKS_IN_WOOD_BETWEEN_THE_WORLDS = ATTACHMENT_TYPES
            .register("ticks_in_wood_between_the_worlds", () -> AttachmentType.builder(() -> 0)
                    .serialize(Codec.INT.fieldOf("value"))
                    .sync(new TicksInWoodBetweenTheWorldsSyncHandler())
                    .build());
    public static final DeferredHolder<AttachmentType<?>, AttachmentType<SpellPowerSource<?>>> SELECTED_SPELL_POWER_SOURCE = ATTACHMENT_TYPES
            .register("selected_spell_power_source", () -> AttachmentType.<SpellPowerSource<?>>builder(ModSpellPowerSources.ALCHEMY::get)
                    .serialize(ModRegistries.SPELL_POWER_SOURCES.byNameCodec().fieldOf("value"))
                    .sync(new SelectedSpellPowerSourceSyncHandler())
                    .copyOnDeath()
                    .build());
    public static final DeferredHolder<AttachmentType<?>, AttachmentType<Double>> INNATE_VITAE = ATTACHMENT_TYPES
            .register("innate_vitae", () -> AttachmentType.builder(() -> 0.0)
                    .serialize(Codec.DOUBLE.fieldOf("value"))
                    .sync(new VitaeSyncHandler())
                    .build());
    public static final DeferredHolder<AttachmentType<?>, AttachmentType<Double>> LUNAR_VITAE = ATTACHMENT_TYPES
            .register("lunar_vitae", () -> AttachmentType.builder(() -> 0.0)
                    .serialize(Codec.DOUBLE.fieldOf("value"))
                    .sync(new VitaeSyncHandler())
                    .build());
}
