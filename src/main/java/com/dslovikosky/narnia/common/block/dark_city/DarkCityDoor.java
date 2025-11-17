package com.dslovikosky.narnia.common.block.dark_city;

import com.dslovikosky.narnia.common.constants.Constants;
import com.dslovikosky.narnia.common.constants.ModBlockSetTypes;
import com.dslovikosky.narnia.common.constants.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.PushReaction;

public class DarkCityDoor extends DoorBlock {
    public DarkCityDoor() {
        super(ModBlockSetTypes.DARK_CITY, BlockBehaviour.Properties.of()
                .mapColor(ModBlocks.DARK_CITY_STONE.get().defaultMapColor())
                .instrument(NoteBlockInstrument.BASEDRUM)
                .requiresCorrectToolForDrops()
                .strength(1.5F, 6.0F)
                .sound(SoundType.STONE)
                .pushReaction(PushReaction.DESTROY)
                .setId(ResourceKey.create(Registries.BLOCK, Constants.modLocation("dark_city_door"))));
    }
}
