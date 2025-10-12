package com.dslovikosky.narnia.common.particle;

import com.dslovikosky.narnia.common.constants.ModParticleTypes;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record ProjectileParticleData(float scale, float red, float green, float blue) implements ParticleOptions {
    public static final MapCodec<ProjectileParticleData> CODEC = RecordCodecBuilder.mapCodec(it ->
            it.group(
                    Codec.FLOAT.fieldOf("scale").forGetter(ProjectileParticleData::scale),
                    Codec.FLOAT.fieldOf("red").forGetter(ProjectileParticleData::red),
                    Codec.FLOAT.fieldOf("green").forGetter(ProjectileParticleData::green),
                    Codec.FLOAT.fieldOf("blue").forGetter(ProjectileParticleData::blue)
            ).apply(it, it.stable(ProjectileParticleData::new)));

    public static final StreamCodec<? super RegistryFriendlyByteBuf, ProjectileParticleData> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.FLOAT,
            ProjectileParticleData::scale,
            ByteBufCodecs.FLOAT,
            ProjectileParticleData::red,
            ByteBufCodecs.FLOAT,
            ProjectileParticleData::green,
            ByteBufCodecs.FLOAT,
            ProjectileParticleData::blue,
            ProjectileParticleData::new
    );

    @Override
    public ParticleType<?> getType() {
        return ModParticleTypes.PROJECTILE.get();
    }
}
