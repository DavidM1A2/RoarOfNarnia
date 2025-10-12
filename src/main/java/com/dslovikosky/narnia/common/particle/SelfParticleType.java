package com.dslovikosky.narnia.common.particle;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public class SelfParticleType extends ParticleType<SelfParticleData> {
    public SelfParticleType() {
        super(false);
    }

    @Override
    public MapCodec<SelfParticleData> codec() {
        return SelfParticleData.CODEC;
    }

    @Override
    public StreamCodec<? super RegistryFriendlyByteBuf, SelfParticleData> streamCodec() {
        return SelfParticleData.STREAM_CODEC;
    }
}
