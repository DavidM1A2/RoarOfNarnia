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

public record FeedParticleData(int entityId, float offsetDegrees, float radius) implements ParticleOptions {
    public static final MapCodec<FeedParticleData> CODEC = RecordCodecBuilder.mapCodec(it ->
            it.group(
                    Codec.INT.fieldOf("entity_id").forGetter(FeedParticleData::entityId),
                    Codec.FLOAT.fieldOf("offset_degrees").forGetter(FeedParticleData::offsetDegrees),
                    Codec.FLOAT.fieldOf("radius").forGetter(FeedParticleData::radius)
            ).apply(it, it.stable(FeedParticleData::new)));

    public static final StreamCodec<? super RegistryFriendlyByteBuf, FeedParticleData> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT,
            FeedParticleData::entityId,
            ByteBufCodecs.FLOAT,
            FeedParticleData::offsetDegrees,
            ByteBufCodecs.FLOAT,
            FeedParticleData::radius,
            FeedParticleData::new
    );

    @Override
    public ParticleType<?> getType() {
        return ModParticleTypes.FEED.get();
    }
}
