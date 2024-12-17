package com.dslovikosky.narnia.common.block.worldwood;

import com.dslovikosky.narnia.common.constants.ModBlocks;
import com.dslovikosky.narnia.common.constants.ModWoodTypes;
import net.minecraft.world.level.block.WallSignBlock;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;

public class WorldWoodWallSignBlock extends WallSignBlock {
    public WorldWoodWallSignBlock() {
        super(ModWoodTypes.WORLD_WOOD, Properties.of()
                .mapColor(ModBlocks.WORLD_WOOD_PLANKS.get().defaultMapColor())
                .forceSolidOn()
                .instrument(NoteBlockInstrument.BASS)
                .noCollission()
                .strength(1.0F)
                .ignitedByLava());
    }
}
