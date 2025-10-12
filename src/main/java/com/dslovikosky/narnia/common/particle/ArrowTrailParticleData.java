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

public record ArrowTrailParticleData(int entityId, int delayTicks) implements ParticleOptions {
    public static final MapCodec<ArrowTrailParticleData> CODEC = RecordCodecBuilder.mapCodec(it ->
            it.group(
                    Codec.INT.fieldOf("entity_id").forGetter(ArrowTrailParticleData::entityId),
                    Codec.INT.fieldOf("delay_ticks").forGetter(ArrowTrailParticleData::delayTicks)
            ).apply(it, it.stable(ArrowTrailParticleData::new)));

    public static final StreamCodec<? super RegistryFriendlyByteBuf, ArrowTrailParticleData> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT,
            ArrowTrailParticleData::entityId,
            ByteBufCodecs.INT,
            ArrowTrailParticleData::delayTicks,
            ArrowTrailParticleData::new
    );

    @Override
    public ParticleType<?> getType() {
        return ModParticleTypes.ARROW_TRAIL.get();
    }
}
