package com.dslovikosky.narnia.common.constants;

import com.dslovikosky.narnia.common.model.attachment_type.DelayedDeliveryEntry;
import com.dslovikosky.narnia.common.model.attachment_type.PreRingTeleportData;
import com.dslovikosky.narnia.common.model.attachment_type.SelectedSpellPowerSourceSyncHandler;
import com.dslovikosky.narnia.common.model.attachment_type.SpellCharmData;
import com.dslovikosky.narnia.common.model.attachment_type.SpellFreezeData;
import com.dslovikosky.narnia.common.model.attachment_type.ThermalData;
import com.dslovikosky.narnia.common.model.attachment_type.TicksInWoodBetweenTheWorldsSyncHandler;
import com.dslovikosky.narnia.common.spell.component.powerSource.base.SpellPowerSource;
import com.mojang.serialization.Codec;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

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
                    .sync(ByteBufCodecs.DOUBLE)
                    .build());
    public static final DeferredHolder<AttachmentType<?>, AttachmentType<Double>> LUNAR_VITAE = ATTACHMENT_TYPES
            .register("lunar_vitae", () -> AttachmentType.builder(() -> 0.0)
                    .serialize(Codec.DOUBLE.fieldOf("value"))
                    .sync(ByteBufCodecs.DOUBLE)
                    .build());
    public static final DeferredHolder<AttachmentType<?>, AttachmentType<Double>> SOLAR_VITAE = ATTACHMENT_TYPES
            .register("solar_vitae", () -> AttachmentType.builder(() -> 0.0)
                    .serialize(Codec.DOUBLE.fieldOf("value"))
                    .sync(ByteBufCodecs.DOUBLE)
                    .build());
    public static final DeferredHolder<AttachmentType<?>, AttachmentType<ThermalData>> THERMAL_DATA = ATTACHMENT_TYPES
            .register("thermal_vitae", () -> AttachmentType.builder(() -> new ThermalData(0.0, 0.0))
                    .serialize(ThermalData.CODEC.fieldOf("value"))
                    .sync(ThermalData.STREAM_CODEC)
                    .build());
    public static final DeferredHolder<AttachmentType<?>, AttachmentType<List<DelayedDeliveryEntry>>> DELAYED_DELIVERY_ENTRIES = ATTACHMENT_TYPES
            .register("delayed_delivery_entries", () -> AttachmentType.<List<DelayedDeliveryEntry>>builder(() -> new ArrayList<>())
                    .serialize(mutableListOf(DelayedDeliveryEntry.CODEC).fieldOf("value"))
                    .build());
    public static final DeferredHolder<AttachmentType<?>, AttachmentType<SpellCharmData>> SPELL_CHARM_DATA = ATTACHMENT_TYPES
            .register("spell_charm_data", () -> AttachmentType.builder(() -> new SpellCharmData(0, UUID.randomUUID()))
                    .serialize(SpellCharmData.CODEC.fieldOf("value"))
                    .sync(SpellCharmData.STREAM_CODEC)
                    .build());
    public static final DeferredHolder<AttachmentType<?>, AttachmentType<SpellFreezeData>> SPELL_FREEZE_DATA = ATTACHMENT_TYPES
            .register("spell_freeze_data", () -> AttachmentType.builder(() -> new SpellFreezeData(0, Vec3.ZERO, 0f, 0f))
                    .serialize(SpellFreezeData.CODEC.fieldOf("value"))
                    .sync(SpellFreezeData.STREAM_CODEC)
                    .build());

    private static <T> Codec<List<T>> mutableListOf(Codec<T> elementCodec) {
        return elementCodec.listOf().xmap(ArrayList::new, Function.identity());
    }
}
