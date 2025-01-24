package com.dslovikosky.narnia.common.model.scene;

import com.dslovikosky.narnia.common.utils.CustomStreamCodec;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public record Setting(ResourceKey<Level> dimension, Vec3 spawnPosition) {
    public static final StreamCodec<RegistryFriendlyByteBuf, Setting> STREAM_CODEC = StreamCodec.composite(
            ResourceKey.streamCodec(Registries.DIMENSION), Setting::dimension,
            CustomStreamCodec.VEC3, Setting::spawnPosition,
            Setting::new);
    public static final Codec<Setting> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ResourceKey.codec(Registries.DIMENSION).fieldOf("dimension").forGetter(Setting::dimension),
            Vec3.CODEC.fieldOf("spawn_position").forGetter(Setting::spawnPosition)
    ).apply(instance, Setting::new));
}
