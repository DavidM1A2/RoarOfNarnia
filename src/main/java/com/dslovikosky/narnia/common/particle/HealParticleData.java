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

public record HealParticleData(int entityId, float offsetDegrees) implements ParticleOptions {
    public static final MapCodec<HealParticleData> CODEC = RecordCodecBuilder.mapCodec(it ->
            it.group(
                    Codec.INT.fieldOf("entity_id").forGetter(HealParticleData::entityId),
                    Codec.FLOAT.fieldOf("offset_degrees").forGetter(HealParticleData::offsetDegrees)
            ).apply(it, it.stable(HealParticleData::new)));

    public static final StreamCodec<? super RegistryFriendlyByteBuf, HealParticleData> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT,
            HealParticleData::entityId,
            ByteBufCodecs.FLOAT,
            HealParticleData::offsetDegrees,
            HealParticleData::new
    );

    @Override
    public ParticleType<?> getType() {
        return ModParticleTypes.HEAL.get();
    }
}
