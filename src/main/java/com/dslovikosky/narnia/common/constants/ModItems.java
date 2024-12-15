package com.dslovikosky.narnia.common.constants;

import com.dslovikosky.narnia.common.item.RingItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Constants.MOD_ID);

    public static final Supplier<RingItem> YELLOW_RING = ITEMS.register("yellow_ring", () -> new RingItem(RingItem.Type.YELLOW));
    public static final Supplier<RingItem> GREEN_RING = ITEMS.register("green_ring", () -> new RingItem(RingItem.Type.GREEN));

    static {
        ModBlocks.BLOCKS.getEntries().forEach(ITEMS::registerSimpleBlockItem);
    }
}
