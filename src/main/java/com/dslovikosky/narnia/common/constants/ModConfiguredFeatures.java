package com.dslovikosky.narnia.common.constants;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;

public class ModConfiguredFeatures {
    public static final ResourceKey<ConfiguredFeature<?, ?>> WORLD_WOOD_TREE = ResourceKey.create(Registries.CONFIGURED_FEATURE, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "world_wood_tree"));
}

