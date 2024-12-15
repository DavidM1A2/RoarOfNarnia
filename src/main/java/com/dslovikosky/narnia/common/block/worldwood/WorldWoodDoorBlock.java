package com.dslovikosky.narnia.common.block.worldwood;

import com.dslovikosky.narnia.common.constants.ModBlockSetTypes;
import com.dslovikosky.narnia.common.constants.ModBlocks;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.PushReaction;

public class WorldWoodDoorBlock extends DoorBlock {
    public WorldWoodDoorBlock() {
        super(ModBlockSetTypes.WORLD_WOOD, BlockBehaviour.Properties.of()
                .mapColor(ModBlocks.WORLD_WOOD_PLANKS.get().defaultMapColor())
                .instrument(NoteBlockInstrument.BASS)
                .strength(3.0F)
                .noOcclusion()
                .ignitedByLava()
                .pushReaction(PushReaction.DESTROY));
    }
}
