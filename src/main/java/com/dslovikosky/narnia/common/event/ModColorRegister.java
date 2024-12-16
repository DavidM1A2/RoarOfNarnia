package com.dslovikosky.narnia.common.event;

import com.dslovikosky.narnia.common.constants.ModBlocks;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.FoliageColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.registries.DeferredHolder;

public class ModColorRegister {
    @SubscribeEvent
    public void registerColorHandlersBlockEvent(final RegisterColorHandlersEvent.Block event) {
        final Block[] leafBlocks = ModBlocks.BLOCKS.getEntries().stream().map(DeferredHolder::get).filter(block -> block instanceof LeavesBlock).toArray(Block[]::new);

        event.register((pState, pLevel, pPos, pTintIndex) -> {
            if (pLevel != null && pPos != null) {
                return BiomeColors.getAverageFoliageColor(pLevel, pPos);
            } else {
                return FoliageColor.getDefaultColor();
            }
        }, leafBlocks);
    }

    @SubscribeEvent
    public void registerColorHandlersItemEvent(final RegisterColorHandlersEvent.Item event) {
        final Block[] leafBlocks = ModBlocks.BLOCKS.getEntries().stream().map(DeferredHolder::get).filter(block -> block instanceof LeavesBlock).toArray(Block[]::new);
        final BlockColors blockColors = event.getBlockColors();

        event.register((pStack, pTintIndex) -> {
            final BlockState leafBlockState = ((BlockItem) pStack.getItem()).getBlock().defaultBlockState();
            return blockColors.getColor(leafBlockState, null, null, pTintIndex);
        }, leafBlocks);
    }
}
