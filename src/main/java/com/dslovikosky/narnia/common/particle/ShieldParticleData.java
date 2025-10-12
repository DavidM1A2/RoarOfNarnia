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

public record ShieldParticleData(int entityId, float offsetDegrees, float radius, int duration) implements ParticleOptions {
    public static final MapCodec<ShieldParticleData> CODEC = RecordCodecBuilder.mapCodec(it ->
            it.group(
                    Codec.INT.fieldOf("entity_id").forGetter(ShieldParticleData::entityId),
                    Codec.FLOAT.fieldOf("offset_degrees").forGetter(ShieldParticleData::offsetDegrees),
                    Codec.FLOAT.fieldOf("radius").forGetter(ShieldParticleData::radius),
                    Codec.INT.fieldOf("duration").forGetter(ShieldParticleData::duration)
            ).apply(it, it.stable(ShieldParticleData::new)));

    public static final StreamCodec<? super RegistryFriendlyByteBuf, ShieldParticleData> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT,
            ShieldParticleData::entityId,
            ByteBufCodecs.FLOAT,
            ShieldParticleData::offsetDegrees,
            ByteBufCodecs.FLOAT,
            ShieldParticleData::radius,
            ByteBufCodecs.INT,
            ShieldParticleData::duration,
            ShieldParticleData::new
    );

    @Override
    public ParticleType<?> getType() {
        return ModParticleTypes.SHIELD.get();
    }
}
