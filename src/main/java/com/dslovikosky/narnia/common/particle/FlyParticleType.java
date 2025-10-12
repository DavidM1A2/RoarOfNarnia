package com.dslovikosky.narnia.common.particle;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public class FlyParticleType extends ParticleType<FlyParticleData> {
    public FlyParticleType() {
        super(false);
    }

    @Override
    public MapCodec<FlyParticleData> codec() {
        return FlyParticleData.CODEC;
    }

    @Override
    public StreamCodec<? super RegistryFriendlyByteBuf, FlyParticleData> streamCodec() {
        return FlyParticleData.STREAM_CODEC;
    }
}
