package com.dslovikosky.narnia.common.block.dark_city;

import com.dslovikosky.narnia.common.constants.Constants;
import com.dslovikosky.narnia.common.constants.ModBlockSetTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;

public class DarkCityStoneBrickTrapDoorBlock extends TrapDoorBlock {
    public DarkCityStoneBrickTrapDoorBlock() {
        super(ModBlockSetTypes.DARK_CITY, BlockBehaviour.Properties.of()
                .mapColor(MapColor.METAL)
                .requiresCorrectToolForDrops()
                .strength(5.0F)
                .noOcclusion()
                .isValidSpawn(Blocks::never)
                .setId(ResourceKey.create(Registries.BLOCK, Constants.modLocation("dark_city_stone_brick_trapdoor"))));
    }
}
