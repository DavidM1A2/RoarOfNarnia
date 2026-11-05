package com.dslovikosky.narnia.common.constants;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.feature.Feature;

public class ModFeatures {
    public static final ResourceKey<Feature> WORLD_WOOD_TREE = ResourceKey.create(Registries.FEATURE, Constants.modLocation("world_wood_tree"));
    public static final ResourceKey<Feature> SMALL_WATER_POOL = ResourceKey.create(Registries.FEATURE, Constants.modLocation("small_water_pool"));
}
