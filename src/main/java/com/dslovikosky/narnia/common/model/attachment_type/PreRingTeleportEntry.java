package com.dslovikosky.narnia.common.model.attachment_type;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.phys.Vec3;

public record PreRingTeleportEntry(Vec3 position, float yaw, float pitch) {
    public static final Codec<PreRingTeleportEntry> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                            Vec3.CODEC.fieldOf("position").forGetter(PreRingTeleportEntry::position),
                            Codec.FLOAT.fieldOf("yaw").forGetter(PreRingTeleportEntry::yaw),
                            Codec.FLOAT.fieldOf("pitch").forGetter(PreRingTeleportEntry::pitch)
                    )
                    .apply(instance, PreRingTeleportEntry::new)
    );
}
