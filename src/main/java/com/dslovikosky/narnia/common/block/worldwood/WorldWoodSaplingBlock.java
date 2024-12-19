package com.dslovikosky.narnia.common.block.worldwood;

import com.dslovikosky.narnia.common.constants.ModTreeGrowers;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

public class WorldWoodSaplingBlock extends SaplingBlock {
    public WorldWoodSaplingBlock() {
        super(ModTreeGrowers.WORLD_WOOD, BlockBehaviour.Properties.of()
                .mapColor(MapColor.PLANT)
                .noCollission()
                .randomTicks()
                .instabreak()
                .sound(SoundType.GRASS)
                .pushReaction(PushReaction.DESTROY));
    }
}
