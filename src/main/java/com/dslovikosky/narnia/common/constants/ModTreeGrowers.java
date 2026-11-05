package com.dslovikosky.narnia.common.constants;

import net.minecraft.util.random.WeightedList;
import net.minecraft.world.level.block.grower.TreeGrower;

// 26.3 swapped TreeGrower's optional feature slots for weighted lists, and added the shortest tree type that
// getMinimumHeight reads. That only reports a height for vanilla TreeFeatures, so it is informational here.
public class ModTreeGrowers {
    public static final TreeGrower WORLD_WOOD = new TreeGrower(
            Constants.modLocation("world_wood").toString(),
            WeightedList.of(ModFeatures.WORLD_WOOD_TREE),
            WeightedList.of(),
            WeightedList.of(),
            ModFeatures.WORLD_WOOD_TREE);
}
