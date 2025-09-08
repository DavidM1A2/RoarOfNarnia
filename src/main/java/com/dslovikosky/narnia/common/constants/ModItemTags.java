package com.dslovikosky.narnia.common.constants;

import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class ModItemTags {
    public static final TagKey<Item> WORLD_WOOD = TagKey.create(Registries.ITEM, Constants.modLocation("world_wood"));
}
