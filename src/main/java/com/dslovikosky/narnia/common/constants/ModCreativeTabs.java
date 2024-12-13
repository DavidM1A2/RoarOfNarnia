package com.dslovikosky.narnia.common.constants;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Constants.MOD_ID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> ROAR_OF_NARNIA = CREATIVE_TABS.register("roar_of_narnia", () -> CreativeModeTab.builder()
            .title(Component.literal("Roar of Narnia"))
            .displayItems((params, output) -> ModItems.ITEMS.getEntries().stream().map(DeferredHolder::get).forEach(output::accept))
            .icon(() -> new ItemStack(ModItems.YELLOW_RING.get()))
            .build());
}
