package com.dslovikosky.narnia.common.constants;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;

public class ModBiomeTags {
    public static final TagKey<Biome> HAS_UNCLE_ANDREWS_HOUSE = create("has_structure/uncle_andrews_house");

    private static TagKey<Biome> create(final String name) {
        return TagKey.create(Registries.BIOME, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, name));
    }
}
