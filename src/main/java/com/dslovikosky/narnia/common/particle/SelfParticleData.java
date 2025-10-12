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

public record SelfParticleData(int entityId, float offsetDegrees) implements ParticleOptions {
    public static final MapCodec<SelfParticleData> CODEC = RecordCodecBuilder.mapCodec(it ->
            it.group(
                    Codec.INT.fieldOf("entity_id").forGetter(SelfParticleData::entityId),
                    Codec.FLOAT.fieldOf("offset_degrees").forGetter(SelfParticleData::offsetDegrees)
            ).apply(it, it.stable(SelfParticleData::new)));

    public static final StreamCodec<? super RegistryFriendlyByteBuf, SelfParticleData> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT,
            SelfParticleData::entityId,
            ByteBufCodecs.FLOAT,
            SelfParticleData::offsetDegrees,
            SelfParticleData::new
    );

    @Override
    public ParticleType<?> getType() {
        return ModParticleTypes.SELF.get();
    }
}
