package com.dslovikosky.narnia.common.constants;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.properties.WoodType;

public class ModWoodTypes {
    public static final WoodType WORLD_WOOD = WoodType.register(new WoodType(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "world_wood").toString(), ModBlockSetTypes.WORLD_WOOD));
}
