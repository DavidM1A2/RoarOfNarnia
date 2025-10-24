package com.dslovikosky.narnia.common.constants;

import com.dslovikosky.narnia.common.model.data_serializer.ColorDataSerializer;
import com.dslovikosky.narnia.common.model.data_serializer.Vec3DataSerializer;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class ModEntityDataSerializers {
    public static final DeferredRegister<EntityDataSerializer<?>> ENTITY_DATA_SERIALIZERS = DeferredRegister.create(NeoForgeRegistries.ENTITY_DATA_SERIALIZERS, Constants.MOD_ID);

    public static final DeferredHolder<EntityDataSerializer<?>, ColorDataSerializer> COLOR = ENTITY_DATA_SERIALIZERS.register("color", ColorDataSerializer::new);
    public static final DeferredHolder<EntityDataSerializer<?>, Vec3DataSerializer> VEC3 = ENTITY_DATA_SERIALIZERS.register("vec3", Vec3DataSerializer::new);
}
