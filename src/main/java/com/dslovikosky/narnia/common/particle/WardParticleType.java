package com.dslovikosky.narnia.common.particle;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public class WardParticleType extends ParticleType<WardParticleData> {
    public WardParticleType() {
        super(false);
    }

    @Override
    public MapCodec<WardParticleData> codec() {
        return WardParticleData.CODEC;
    }

    @Override
    public StreamCodec<? super RegistryFriendlyByteBuf, WardParticleData> streamCodec() {
        return WardParticleData.STREAM_CODEC;
    }
}
