package com.dslovikosky.narnia.common.constants;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;

public class ModBiomes {
    public static final ResourceKey<Biome> WOOD_BETWEEN_THE_WORLDS = ResourceKey.create(Registries.BIOME, Constants.modLocation("wood_between_the_worlds"));
    public static final ResourceKey<Biome> DARK_CITY_RUINS = ResourceKey.create(Registries.BIOME, Constants.modLocation("dark_city_ruins"));
}
