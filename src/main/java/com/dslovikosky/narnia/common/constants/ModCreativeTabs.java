package com.dslovikosky.narnia.common.constants;

import com.google.common.collect.ImmutableSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Set;

public class ModCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Constants.MOD_ID);
    private static final Set<DeferredItem<? extends Item>> CREATIVE_HIDDEN_ITEMS = ImmutableSet.of(ModItems.DEBUG);
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> ROAR_OF_NARNIA = CREATIVE_TABS.register("roar_of_narnia", () -> CreativeModeTab.builder()
            .title(Component.literal("Roar of Narnia"))
            .displayItems((params, output) -> ModItems.ITEMS.getEntries()
                    .stream()
                    .filter(item -> !CREATIVE_HIDDEN_ITEMS.contains(item))
                    .map(DeferredHolder::get)
                    .forEach(output::accept))
            .icon(() -> new ItemStack(ModItems.YELLOW_RING.get()))
            .build());
}
