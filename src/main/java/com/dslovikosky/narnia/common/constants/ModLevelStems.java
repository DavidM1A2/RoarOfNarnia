package com.dslovikosky.narnia.common.constants;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.dimension.LevelStem;

public class ModLevelStems {
    public static final ResourceKey<LevelStem> WOOD_BETWEEN_THE_WORLDS = ResourceKey.create(Registries.LEVEL_STEM, Constants.modLocation("wood_between_the_worlds"));
    public static final ResourceKey<LevelStem> DARK_CITY_RUINS = ResourceKey.create(Registries.LEVEL_STEM, Constants.modLocation("dark_city_ruins"));
}
