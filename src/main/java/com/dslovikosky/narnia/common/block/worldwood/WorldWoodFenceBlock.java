package com.dslovikosky.narnia.common.block.worldwood;

import com.dslovikosky.narnia.common.constants.Constants;
import com.dslovikosky.narnia.common.constants.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;

public class WorldWoodFenceBlock extends FenceBlock {
    public WorldWoodFenceBlock() {
        super(BlockBehaviour.Properties.of()
                .mapColor(ModBlocks.WORLD_WOOD_PLANKS.get().defaultMapColor())
                .instrument(NoteBlockInstrument.BASS)
                .strength(2.0F, 3.0F)
                .ignitedByLava()
                .sound(SoundType.WOOD)
                .setId(ResourceKey.create(Registries.BLOCK, Constants.modLocation("world_wood_fence"))));
    }
}
