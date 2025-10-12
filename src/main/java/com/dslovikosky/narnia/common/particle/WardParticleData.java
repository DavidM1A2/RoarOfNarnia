package com.dslovikosky.narnia.common.particle;

import com.dslovikosky.narnia.common.constants.ModParticleTypes;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record WardParticleData(Direction direction, float scale) implements ParticleOptions {
    public static final MapCodec<WardParticleData> CODEC = RecordCodecBuilder.mapCodec(it ->
            it.group(
                    Codec.INT.fieldOf("direction").xmap(integer -> Direction.values()[integer], Enum::ordinal).forGetter(WardParticleData::direction),
                    Codec.FLOAT.fieldOf("scale").forGetter(WardParticleData::scale)
            ).apply(it, it.stable(WardParticleData::new)));

    public static final StreamCodec<? super RegistryFriendlyByteBuf, WardParticleData> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT.map(integer -> Direction.values()[integer], Enum::ordinal),
            WardParticleData::direction,
            ByteBufCodecs.FLOAT,
            WardParticleData::scale,
            WardParticleData::new
    );

    @Override
    public ParticleType<?> getType() {
        return ModParticleTypes.WARD.get();
    }
}
