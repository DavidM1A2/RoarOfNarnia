package com.dslovikosky.narnia.common.model.chapter.datacomponent;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.StreamCodec;

public record UncleAndrewsHouseLocation(BlockPos location) {
    public static final Codec<UncleAndrewsHouseLocation> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    BlockPos.CODEC.fieldOf("location").forGetter(UncleAndrewsHouseLocation::location)
            ).apply(instance, UncleAndrewsHouseLocation::new)
    );
    public static final StreamCodec<ByteBuf, UncleAndrewsHouseLocation> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, UncleAndrewsHouseLocation::location,
            UncleAndrewsHouseLocation::new
    );
}
