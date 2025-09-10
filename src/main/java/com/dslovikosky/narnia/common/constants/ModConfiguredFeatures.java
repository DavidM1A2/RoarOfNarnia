package com.dslovikosky.narnia.common.constants;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;

public class ModConfiguredFeatures {
    public static final ResourceKey<ConfiguredFeature<?, ?>> WORLD_WOOD_TREE = ResourceKey.create(Registries.CONFIGURED_FEATURE, Constants.modLocation("world_wood_tree"));
    public static final ResourceKey<ConfiguredFeature<?, ?>> SMALL_WATER_POOL = ResourceKey.create(Registries.CONFIGURED_FEATURE, Constants.modLocation("small_water_pool"));
}

