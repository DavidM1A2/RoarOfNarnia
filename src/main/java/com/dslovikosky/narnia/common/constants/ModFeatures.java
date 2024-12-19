package com.dslovikosky.narnia.common.constants;

import com.dslovikosky.narnia.common.world.feature.WaterPoolFeature;
import com.dslovikosky.narnia.common.world.feature.WorldWoodTreeConfiguration;
import com.dslovikosky.narnia.common.world.feature.WorldWoodTreeFeature;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModFeatures {
    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(Registries.FEATURE, Constants.MOD_ID);

    public static final DeferredHolder<Feature<?>, WaterPoolFeature> WATER_POOL = FEATURES.register("water_pool", it -> new WaterPoolFeature(WaterPoolFeature.Configuration.CODEC));
    public static final DeferredHolder<Feature<?>, WorldWoodTreeFeature> WORLD_WOOD_TREE = FEATURES.register("world_wood_tree", it -> new WorldWoodTreeFeature(WorldWoodTreeConfiguration.CODEC));
}
