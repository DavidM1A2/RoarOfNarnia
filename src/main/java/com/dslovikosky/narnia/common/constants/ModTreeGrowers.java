package com.dslovikosky.narnia.common.constants;

import net.minecraft.util.random.WeightedList;
import net.minecraft.world.level.block.grower.TreeGrower;

public class ModTreeGrowers {
    public static final TreeGrower WORLD_WOOD = new TreeGrower(
            Constants.modLocation("world_wood").toString(),
            WeightedList.of(ModFeatures.WORLD_WOOD_TREE),
            WeightedList.of(),
            WeightedList.of(),
            ModFeatures.WORLD_WOOD_TREE);
}
