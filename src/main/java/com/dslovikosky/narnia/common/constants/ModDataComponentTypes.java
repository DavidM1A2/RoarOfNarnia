package com.dslovikosky.narnia.common.constants;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponentType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModDataComponentTypes {
    public static final DeferredRegister.DataComponents DATA_COMPONENT_TYPES = DeferredRegister.createDataComponents(Constants.MOD_ID);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<BlockPos>> UNCLE_ANDREWS_HOUSE_LOCATION = DATA_COMPONENT_TYPES.registerComponentType(
            "uncle_andrews_house_location", builder -> builder.persistent(BlockPos.CODEC).networkSynchronized(BlockPos.STREAM_CODEC));
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Direction>> UNCLE_ANDREWS_HOUSE_DIRECTION = DATA_COMPONENT_TYPES.registerComponentType(
            "uncle_andrews_house_direction", builder -> builder.persistent(Direction.CODEC).networkSynchronized(Direction.STREAM_CODEC));
}
