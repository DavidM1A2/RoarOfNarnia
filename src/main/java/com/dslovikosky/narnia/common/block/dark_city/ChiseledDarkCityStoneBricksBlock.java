package com.dslovikosky.narnia.common.block.dark_city;

import com.dslovikosky.narnia.common.constants.Constants;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;

public class ChiseledDarkCityStoneBricksBlock extends RotatedPillarBlock {
    public ChiseledDarkCityStoneBricksBlock() {
        super(Properties.of()
                .mapColor(MapColor.STONE)
                .instrument(NoteBlockInstrument.BASEDRUM)
                .requiresCorrectToolForDrops()
                .strength(1.5F, 6.0F)
                .sound(SoundType.STONE)
                .setId(ResourceKey.create(Registries.BLOCK, Constants.modLocation("chiseled_dark_city_stone_bricks"))));
    }
}
