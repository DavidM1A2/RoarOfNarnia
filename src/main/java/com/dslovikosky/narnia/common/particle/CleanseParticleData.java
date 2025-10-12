package com.dslovikosky.narnia.common.particle;

import com.dslovikosky.narnia.common.constants.ModParticleTypes;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record CleanseParticleData(int entityId, float offsetDegrees, float radius) implements ParticleOptions {
    public static final MapCodec<CleanseParticleData> CODEC = RecordCodecBuilder.mapCodec(it ->
            it.group(
                    Codec.INT.fieldOf("entity_id").forGetter(CleanseParticleData::entityId),
                    Codec.FLOAT.fieldOf("offset_degrees").forGetter(CleanseParticleData::offsetDegrees),
                    Codec.FLOAT.fieldOf("radius").forGetter(CleanseParticleData::radius)
            ).apply(it, it.stable(CleanseParticleData::new)));

    public static final StreamCodec<? super RegistryFriendlyByteBuf, CleanseParticleData> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT,
            CleanseParticleData::entityId,
            ByteBufCodecs.FLOAT,
            CleanseParticleData::offsetDegrees,
            ByteBufCodecs.FLOAT,
            CleanseParticleData::radius,
            CleanseParticleData::new
    );

    @Override
    public ParticleType<?> getType() {
        return ModParticleTypes.CLEANSE.get();
    }
}
