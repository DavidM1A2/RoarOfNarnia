package com.dslovikosky.narnia.common.constants;

import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class ModBlockTags {
    public static final TagKey<Block> WORLD_WOOD = TagKey.create(Registries.BLOCK, Constants.modLocation("world_wood"));
    public static final TagKey<Block> GRASS_LIKE = TagKey.create(Registries.BLOCK, Constants.modLocation("grass_like"));
}
