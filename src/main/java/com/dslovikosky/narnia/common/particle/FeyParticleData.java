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

public record FeyParticleData(float offsetDegrees, float red, float green, float blue) implements ParticleOptions {
    public static final MapCodec<FeyParticleData> CODEC = RecordCodecBuilder.mapCodec(it ->
            it.group(
                    Codec.FLOAT.fieldOf("offset_degrees").forGetter(FeyParticleData::offsetDegrees),
                    Codec.FLOAT.fieldOf("red").forGetter(FeyParticleData::red),
                    Codec.FLOAT.fieldOf("green").forGetter(FeyParticleData::green),
                    Codec.FLOAT.fieldOf("blue").forGetter(FeyParticleData::blue)
            ).apply(it, it.stable(FeyParticleData::new)));

    public static final StreamCodec<? super RegistryFriendlyByteBuf, FeyParticleData> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.FLOAT,
            FeyParticleData::offsetDegrees,
            ByteBufCodecs.FLOAT,
            FeyParticleData::red,
            ByteBufCodecs.FLOAT,
            FeyParticleData::green,
            ByteBufCodecs.FLOAT,
            FeyParticleData::blue,
            FeyParticleData::new
    );

    @Override
    public ParticleType<?> getType() {
        return ModParticleTypes.FEY.get();
    }
}
