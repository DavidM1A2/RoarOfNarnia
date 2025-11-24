package com.dslovikosky.narnia.common.block.dark_city;

import com.dslovikosky.narnia.common.constants.Constants;
import com.dslovikosky.narnia.common.constants.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;

public class DarkCitySlateStairsBlock extends StairBlock {
    public DarkCitySlateStairsBlock() {
        super(ModBlocks.DARK_CITY_SLATE.get().defaultBlockState(), Properties.of()
                .mapColor(MapColor.STONE)
                .instrument(NoteBlockInstrument.BASEDRUM)
                .requiresCorrectToolForDrops()
                .strength(1.5F, 6.0F)
                .sound(SoundType.STONE)
                .setId(ResourceKey.create(Registries.BLOCK, Constants.modLocation("dark_city_slate_stairs"))));
    }
}
