package com.dslovikosky.narnia.common.constants;

import com.dslovikosky.narnia.common.model.attachment_type.PreRingTeleportData;
import com.dslovikosky.narnia.common.model.attachment_type.TicksInWoodBetweenTheWorldsSyncHandler;
import com.mojang.serialization.Codec;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class ModAttachmentTypes {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, Constants.MOD_ID);

    public static final DeferredHolder<AttachmentType<?>, AttachmentType<PreRingTeleportData>> PRE_RING_TELEPORT_DATA = ATTACHMENT_TYPES
            .register("pre_ring_teleport_data", () -> AttachmentType.builder(PreRingTeleportData::new)
                    .serialize(PreRingTeleportData.CODEC.fieldOf("pre_ring_teleport_data"))
                    .build());
    public static final DeferredHolder<AttachmentType<?>, AttachmentType<Integer>> TICKS_IN_WOOD_BETWEEN_THE_WORLDS = ATTACHMENT_TYPES
            .register("ticks_in_wood_between_the_worlds_attachment_type", () -> AttachmentType.builder(() -> 0)
                    .serialize(Codec.INT.fieldOf("ticks_in_wood_between_the_worlds"))
                    .sync(new TicksInWoodBetweenTheWorldsSyncHandler())
                    .build());
}
