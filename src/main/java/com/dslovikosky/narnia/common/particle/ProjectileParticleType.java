package com.dslovikosky.narnia.common.particle;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public class ProjectileParticleType extends ParticleType<ProjectileParticleData> {
    public ProjectileParticleType() {
        super(false);
    }

    @Override
    public MapCodec<ProjectileParticleData> codec() {
        return ProjectileParticleData.CODEC;
    }

    @Override
    public StreamCodec<? super RegistryFriendlyByteBuf, ProjectileParticleData> streamCodec() {
        return ProjectileParticleData.STREAM_CODEC;
    }
}
