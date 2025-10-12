package com.dslovikosky.narnia.common.particle;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public class FeyParticleType extends ParticleType<FeyParticleData> {
    public FeyParticleType() {
        super(false);
    }

    @Override
    public MapCodec<FeyParticleData> codec() {
        return FeyParticleData.CODEC;
    }

    @Override
    public StreamCodec<? super RegistryFriendlyByteBuf, FeyParticleData> streamCodec() {
        return FeyParticleData.STREAM_CODEC;
    }
}
