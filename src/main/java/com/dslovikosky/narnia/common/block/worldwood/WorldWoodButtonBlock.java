package com.dslovikosky.narnia.common.block.worldwood;

import com.dslovikosky.narnia.common.constants.ModBlockSetTypes;
import com.dslovikosky.narnia.common.constants.ModBlocks;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelProvider;
import net.neoforged.neoforge.registries.DeferredHolder;

public class WorldWoodButtonBlock extends ButtonBlock implements HasCustomBlockItemModel {
    public WorldWoodButtonBlock() {
        super(ModBlockSetTypes.WORLD_WOOD, 30, BlockBehaviour.Properties.of().noCollission().strength(0.5F).pushReaction(PushReaction.DESTROY));
    }

    @Override
    public void applyBlockItemModel(final ItemModelProvider itemModelProvider, final DeferredHolder<Item, ? extends Item> item) {
        itemModelProvider.buttonInventory(item.getRegisteredName(), itemModelProvider.modLoc(ModelProvider.BLOCK_FOLDER + "/" + BuiltInRegistries.BLOCK.getKey(ModBlocks.WORLD_WOOD_PLANKS.get()).getPath()));
    }
}
