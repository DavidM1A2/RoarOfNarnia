package com.dslovikosky.narnia.common.constants;

import com.dslovikosky.narnia.common.model.attachment_type.PreTeleportLocation;
import com.dslovikosky.narnia.common.model.attachment_type.TicksInWoodBetweenTheWorldsSyncHandler;
import com.mojang.serialization.Codec;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class ModAttachmentTypes {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, Constants.MOD_ID);

    public static final DeferredHolder<AttachmentType<?>, AttachmentType<PreTeleportLocation>> PRE_YELLOW_RING_TELEPORT_LOCATION = ATTACHMENT_TYPES.register("pre_yellow_ring_teleport_location",
            () -> AttachmentType.builder(PreTeleportLocation::new).serialize(PreTeleportLocation.CODEC.fieldOf("pre_yellow_ring_teleport_location")).build());
    public static final DeferredHolder<AttachmentType<?>, AttachmentType<Integer>> TICKS_IN_WOOD_BETWEEN_THE_WORLDS = ATTACHMENT_TYPES
            .register("ticks_in_wood_between_the_worlds", () -> AttachmentType.builder(() -> 0)
                    .serialize(Codec.INT.fieldOf("ticks_in_wood_between_the_worlds"))
                    .sync(new TicksInWoodBetweenTheWorldsSyncHandler())
                    .build());
}
