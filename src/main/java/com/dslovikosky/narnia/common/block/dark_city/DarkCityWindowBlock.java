package com.dslovikosky.narnia.common.block.dark_city;

import com.dslovikosky.narnia.common.constants.Constants;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.TransparentBlock;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;

public class DarkCityWindowBlock extends TransparentBlock {
    public DarkCityWindowBlock() {
        super(Properties.of()
                .instrument(NoteBlockInstrument.HAT)
                .requiresCorrectToolForDrops()
                .strength(0.3f)
                .sound(SoundType.GLASS)
                .noOcclusion()
                .isValidSpawn(Blocks::never)
                .isRedstoneConductor((ignoredA, ignoredB, ignoredC) -> false)
                .isSuffocating((ignoredA, ignoredB, ignoredC) -> false)
                .isViewBlocking((ignoredA, ignoredB, ignoredC) -> false)
                .setId(ResourceKey.create(Registries.BLOCK, Constants.modLocation("dark_city_window"))));
    }
}
