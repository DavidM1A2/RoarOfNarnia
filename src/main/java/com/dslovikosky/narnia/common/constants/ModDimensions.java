package com.dslovikosky.narnia.common.constants;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

public class ModDimensions {
    public static final ResourceKey<Level> WOOD_BETWEEN_THE_WORLDS = ResourceKey.create(Registries.DIMENSION, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "wood_between_the_worlds"));
    public static final ResourceKey<Level> LONDON = ResourceKey.create(Registries.DIMENSION, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "london"));
}
