package com.dslovikosky.narnia.common.model.data_serializer;

import io.netty.buffer.ByteBuf;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.syncher.EntityDataSerializer;

import java.awt.Color;

@MethodsReturnNonnullByDefault
public class ColorDataSerializer implements EntityDataSerializer<Color> {
    private static final StreamCodec<ByteBuf, Color> CODEC = StreamCodec.composite(
            ByteBufCodecs.INT,
            Color::getRed,
            ByteBufCodecs.INT,
            Color::getGreen,
            ByteBufCodecs.INT,
            Color::getBlue,
            ByteBufCodecs.INT,
            Color::getAlpha,
            Color::new
    );

    @Override
    public StreamCodec<? super RegistryFriendlyByteBuf, Color> codec() {
        return CODEC;
    }

    @Override
    public Color copy(Color value) {
        return new Color(value.getRed(), value.getGreen(), value.getBlue(), value.getAlpha());
    }
}
