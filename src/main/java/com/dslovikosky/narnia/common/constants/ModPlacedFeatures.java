package com.dslovikosky.narnia.common.constants;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

public class ModPlacedFeatures {
    public static final ResourceKey<PlacedFeature> WOOD_BETWEEN_THE_WORLDS_TREES = ResourceKey.create(Registries.PLACED_FEATURE, Constants.modLocation("wood_between_the_worlds_trees"));
    public static final ResourceKey<PlacedFeature> SPARSE_WORLD_WOOD_TREES = ResourceKey.create(Registries.PLACED_FEATURE, Constants.modLocation("sparse_world_wood_trees"));
}

