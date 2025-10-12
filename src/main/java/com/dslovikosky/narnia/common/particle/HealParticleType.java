package com.dslovikosky.narnia.common.particle;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public class HealParticleType extends ParticleType<HealParticleData> {
    public HealParticleType() {
        super(false);
    }

    @Override
    public MapCodec<HealParticleData> codec() {
        return HealParticleData.CODEC;
    }

    @Override
    public StreamCodec<? super RegistryFriendlyByteBuf, HealParticleData> streamCodec() {
        return HealParticleData.STREAM_CODEC;
    }
}
