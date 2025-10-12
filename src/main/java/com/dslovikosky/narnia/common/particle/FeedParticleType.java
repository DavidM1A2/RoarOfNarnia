package com.dslovikosky.narnia.common.particle;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public class FeedParticleType extends ParticleType<FeedParticleData> {
    public FeedParticleType() {
        super(false);
    }

    @Override
    public MapCodec<FeedParticleData> codec() {
        return FeedParticleData.CODEC;
    }

    @Override
    public StreamCodec<? super RegistryFriendlyByteBuf, FeedParticleData> streamCodec() {
        return FeedParticleData.STREAM_CODEC;
    }
}
