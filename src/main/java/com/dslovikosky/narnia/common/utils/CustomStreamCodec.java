package com.dslovikosky.narnia.common.utils;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class CustomStreamCodec {
    public static final StreamCodec<ByteBuf, Vec3> VEC3 = ByteBufCodecs.DOUBLE
            .apply(ByteBufCodecs.list(3))
            .map(list -> new Vec3(list.get(0), list.get(1), list.get(2)), vec3 -> List.of(vec3.x(), vec3.y(), vec3.z()));
}
