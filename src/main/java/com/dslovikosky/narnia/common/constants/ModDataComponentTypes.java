package com.dslovikosky.narnia.common.constants;

import com.dslovikosky.narnia.common.model.chapter.datacomponent.UncleAndrewsHouseLocation;
import net.minecraft.core.component.DataComponentType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModDataComponentTypes {
    public static final DeferredRegister.DataComponents DATA_COMPONENT_TYPES = DeferredRegister.createDataComponents(Constants.MOD_ID);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<UncleAndrewsHouseLocation>> UNCLE_ANDREWS_HOUSE_LOCATION = DATA_COMPONENT_TYPES.registerComponentType(
            "uncle_andrews_house_location", builder -> builder.persistent(UncleAndrewsHouseLocation.CODEC).networkSynchronized(UncleAndrewsHouseLocation.STREAM_CODEC));
}
