package com.dslovikosky.narnia.common.block.worldwood;

import com.dslovikosky.narnia.common.constants.ModBlockSetTypes;
import com.dslovikosky.narnia.common.constants.ModBlocks;
import net.minecraft.world.level.block.PressurePlateBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.PushReaction;

public class WorldWoodPressurePlateBlock extends PressurePlateBlock {
    public WorldWoodPressurePlateBlock() {
        super(ModBlockSetTypes.WORLD_WOOD, BlockBehaviour.Properties.of()
                .mapColor(ModBlocks.WORLD_WOOD_PLANKS.get().defaultMapColor())
                .forceSolidOn()
                .instrument(NoteBlockInstrument.BASS)
                .noCollission()
                .strength(0.5F)
                .ignitedByLava()
                .pushReaction(PushReaction.DESTROY));
    }
}
