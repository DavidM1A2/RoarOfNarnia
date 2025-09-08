package com.dslovikosky.narnia.common.block.worldwood;

import com.dslovikosky.narnia.common.constants.Constants;
import com.dslovikosky.narnia.common.constants.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class WorldWoodStairBlock extends StairBlock {
    public WorldWoodStairBlock() {
        super(ModBlocks.WORLD_WOOD_PLANKS.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(ModBlocks.WORLD_WOOD_PLANKS.get())
                .setId(ResourceKey.create(Registries.BLOCK, Constants.modLocation("world_wood_stair"))));
    }
}
