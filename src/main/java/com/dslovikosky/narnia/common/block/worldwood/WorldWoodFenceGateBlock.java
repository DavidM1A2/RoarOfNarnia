package com.dslovikosky.narnia.common.block.worldwood;

import com.dslovikosky.narnia.common.constants.ModBlocks;
import com.dslovikosky.narnia.common.constants.ModWoodTypes;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;

public class WorldWoodFenceGateBlock extends FenceGateBlock {
    public WorldWoodFenceGateBlock() {
        super(ModWoodTypes.WORLD_WOOD, BlockBehaviour.Properties.of()
                .mapColor(ModBlocks.WORLD_WOOD_PLANKS.get().defaultMapColor())
                .forceSolidOn()
                .instrument(NoteBlockInstrument.BASS)
                .strength(2.0F, 3.0F)
                .ignitedByLava());
    }
}
