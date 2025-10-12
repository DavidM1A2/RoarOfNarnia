package com.dslovikosky.narnia.common.particle;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public class ShieldParticleType extends ParticleType<ShieldParticleData> {
    public ShieldParticleType() {
        super(false);
    }

    @Override
    public MapCodec<ShieldParticleData> codec() {
        return ShieldParticleData.CODEC;
    }

    @Override
    public StreamCodec<? super RegistryFriendlyByteBuf, ShieldParticleData> streamCodec() {
        return ShieldParticleData.STREAM_CODEC;
    }
}
