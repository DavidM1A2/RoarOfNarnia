package com.dslovikosky.narnia.common.block.worldwood;

import com.dslovikosky.narnia.common.constants.Constants;
import com.dslovikosky.narnia.common.constants.ModBlockSetTypes;
import com.dslovikosky.narnia.common.constants.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
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
                .noCollision()
                .strength(0.5F)
                .ignitedByLava()
                .pushReaction(PushReaction.DESTROY)
                .setId(ResourceKey.create(Registries.BLOCK, Constants.modLocation("world_wood_pressure_plate"))));
    }
}
