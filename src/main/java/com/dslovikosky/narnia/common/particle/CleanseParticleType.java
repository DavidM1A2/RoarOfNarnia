package com.dslovikosky.narnia.common.particle;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public class CleanseParticleType extends ParticleType<CleanseParticleData> {
    public CleanseParticleType() {
        super(false);
    }

    @Override
    public MapCodec<CleanseParticleData> codec() {
        return CleanseParticleData.CODEC;
    }

    @Override
    public StreamCodec<? super RegistryFriendlyByteBuf, CleanseParticleData> streamCodec() {
        return CleanseParticleData.STREAM_CODEC;
    }
}
