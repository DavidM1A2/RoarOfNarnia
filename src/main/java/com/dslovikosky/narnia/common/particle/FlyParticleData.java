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

public record FlyParticleData(int entityId, int delayTicks) implements ParticleOptions {
    public static final MapCodec<FlyParticleData> CODEC = RecordCodecBuilder.mapCodec(it ->
            it.group(
                    Codec.INT.fieldOf("entity_id").forGetter(FlyParticleData::entityId),
                    Codec.INT.fieldOf("delay_ticks").forGetter(FlyParticleData::delayTicks)
            ).apply(it, it.stable(FlyParticleData::new)));

    public static final StreamCodec<? super RegistryFriendlyByteBuf, FlyParticleData> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT,
            FlyParticleData::entityId,
            ByteBufCodecs.INT,
            FlyParticleData::delayTicks,
            FlyParticleData::new
    );

    @Override
    public ParticleType<?> getType() {
        return ModParticleTypes.FLY.get();
    }
}
