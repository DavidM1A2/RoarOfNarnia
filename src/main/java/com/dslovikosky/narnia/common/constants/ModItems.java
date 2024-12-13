package com.dslovikosky.narnia.common.constants;

import com.dslovikosky.narnia.common.item.RingItem;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, Constants.MOD_ID);

    public static final Supplier<RingItem> RING = ITEMS.register("ring", RingItem::new);
}
