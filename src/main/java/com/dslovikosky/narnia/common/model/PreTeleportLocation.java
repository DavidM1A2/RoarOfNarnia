package com.dslovikosky.narnia.common.model;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

public record PreTeleportLocation(double posX, double posY, double posZ, ResourceKey<Level> level) {
    public static final Codec<PreTeleportLocation> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                            Codec.DOUBLE.fieldOf("pos_x").forGetter(PreTeleportLocation::posX),
                            Codec.DOUBLE.fieldOf("pos_y").forGetter(PreTeleportLocation::posY),
                            Codec.DOUBLE.fieldOf("pos_z").forGetter(PreTeleportLocation::posZ),
                            ResourceKey.codec(Registries.DIMENSION).fieldOf("level").forGetter(PreTeleportLocation::level)
                    )
                    .apply(instance, PreTeleportLocation::new)
    );

    public PreTeleportLocation() {
        this(0, 64, 0, Level.OVERWORLD);
    }
}
