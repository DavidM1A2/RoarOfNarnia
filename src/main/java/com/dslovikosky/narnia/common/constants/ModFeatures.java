package com.dslovikosky.narnia.common.constants;

import com.dslovikosky.narnia.common.world.feature.WaterPoolFeature;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModFeatures {
    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(Registries.FEATURE, Constants.MOD_ID);

    public static final DeferredHolder<Feature<?>, WaterPoolFeature> WATER_POOL_FEATURE = FEATURES.register("water_pool", it -> new WaterPoolFeature(WaterPoolFeature.Configuration.CODEC));
}
