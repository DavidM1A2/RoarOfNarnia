package com.dslovikosky.narnia.common.event;

import com.dslovikosky.narnia.common.constants.ModBlocks;
import net.minecraft.client.color.block.BlockTintSources;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LeavesBlock;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.List;

public class ModColorRegister {
    @SubscribeEvent
    public void registerColorHandlersBlockEvent(final RegisterColorHandlersEvent.BlockTintSources event) {
        final Block[] leafBlocks = ModBlocks.BLOCKS.getEntries().stream().map(DeferredHolder::get).filter(block -> block instanceof LeavesBlock).toArray(Block[]::new);

        event.register(List.of(BlockTintSources.foliage()), leafBlocks);
    }
}
