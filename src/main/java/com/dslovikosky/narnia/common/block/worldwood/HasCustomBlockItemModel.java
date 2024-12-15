package com.dslovikosky.narnia.common.block.worldwood;

import net.minecraft.world.item.Item;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.registries.DeferredHolder;

public interface HasCustomBlockItemModel {
    void applyBlockItemModel(final ItemModelProvider itemModelProvider, final DeferredHolder<Item, ? extends Item> item);
}
