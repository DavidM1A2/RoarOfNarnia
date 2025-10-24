package com.dslovikosky.narnia.common.model.data_serializer;

import io.netty.buffer.ByteBuf;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.world.phys.Vec3;

@MethodsReturnNonnullByDefault
public class Vec3DataSerializer implements EntityDataSerializer<Vec3> {
    private static final StreamCodec<ByteBuf, Vec3> CODEC = StreamCodec.composite(
            ByteBufCodecs.DOUBLE,
            Vec3::x,
            ByteBufCodecs.DOUBLE,
            Vec3::y,
            ByteBufCodecs.DOUBLE,
            Vec3::z,
            Vec3::new
    );

    @Override
    public StreamCodec<? super RegistryFriendlyByteBuf, Vec3> codec() {
        return CODEC;
    }

    @Override
    public Vec3 copy(Vec3 value) {
        return new Vec3(value.x(), value.y(), value.z());
    }
}
