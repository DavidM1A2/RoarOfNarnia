package com.dslovikosky.narnia.common.block.worldwood;

import com.dslovikosky.narnia.common.constants.ModBlocks;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class WorldWoodStairBlock extends StairBlock {
    public WorldWoodStairBlock() {
        super(ModBlocks.WORLD_WOOD_PLANKS.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(ModBlocks.WORLD_WOOD_PLANKS.get()));
    }
}
