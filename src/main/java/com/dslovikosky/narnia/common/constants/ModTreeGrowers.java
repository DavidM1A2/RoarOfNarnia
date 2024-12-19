package com.dslovikosky.narnia.common.constants;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.grower.TreeGrower;

import java.util.Optional;

public class ModTreeGrowers {
    public static final TreeGrower WORLD_WOOD = new TreeGrower(
            ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "world_wood").toString(),
            Optional.empty(),
            Optional.of(ModConfiguredFeatures.WORLD_WOOD_TREE),
            Optional.empty());
}
