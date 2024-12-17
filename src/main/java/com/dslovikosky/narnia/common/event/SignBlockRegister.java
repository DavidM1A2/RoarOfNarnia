package com.dslovikosky.narnia.common.event;

import com.dslovikosky.narnia.common.constants.ModBlocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.BlockEntityTypeAddBlocksEvent;

public class SignBlockRegister {
    @SubscribeEvent
    public void onBlockEntityTypeAddBlocksEvent(final BlockEntityTypeAddBlocksEvent event) {
        event.modify(BlockEntityType.SIGN, ModBlocks.WORLD_WOOD_STANDING_SIGN.get(), ModBlocks.WORLD_WOOD_WALL_SIGN.get());
        event.modify(BlockEntityType.HANGING_SIGN, ModBlocks.WORLD_WOOD_WALL_HANGING_SIGN.get(), ModBlocks.WORLD_WOOD_CEILING_HANGING_SIGN.get());
    }
}
