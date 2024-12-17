package com.dslovikosky.narnia.common.constants;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.properties.BlockSetType;

public class ModBlockSetTypes {
    public static final BlockSetType WORLD_WOOD = BlockSetType.register(new BlockSetType(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "world_wood").toString()));
}
