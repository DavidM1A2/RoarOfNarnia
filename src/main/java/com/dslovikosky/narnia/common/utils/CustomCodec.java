package com.dslovikosky.narnia.common.utils;

import com.mojang.serialization.Codec;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CustomCodec {
    public static final Codec<Vec3> VEC3 = Codec.DOUBLE
            .listOf(3, 3)
            .xmap(list -> new Vec3(list.get(0), list.get(1), list.get(2)), vec3 -> List.of(vec3.x(), vec3.y(), vec3.z()));

    public static <T> Codec<Set<T>> set(final Codec<T> codec) {
        return Codec.list(codec).xmap(HashSet::new, ArrayList::new);
    }
}
