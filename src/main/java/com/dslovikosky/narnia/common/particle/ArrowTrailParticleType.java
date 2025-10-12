package com.dslovikosky.narnia.common.particle;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public class ArrowTrailParticleType extends ParticleType<ArrowTrailParticleData> {
    public ArrowTrailParticleType() {
        super(false);
    }

    @Override
    public MapCodec<ArrowTrailParticleData> codec() {
        return ArrowTrailParticleData.CODEC;
    }

    @Override
    public StreamCodec<? super RegistryFriendlyByteBuf, ArrowTrailParticleData> streamCodec() {
        return ArrowTrailParticleData.STREAM_CODEC;
    }
}
