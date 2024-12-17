package com.dslovikosky.narnia.common.constants;

import com.dslovikosky.narnia.common.item.RingItem;
import com.dslovikosky.narnia.common.item.WorldWoodHangingSignItem;
import com.dslovikosky.narnia.common.item.WorldWoodSignItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Constants.MOD_ID);

    public static final Supplier<RingItem> YELLOW_RING = ITEMS.register("yellow_ring", () -> new RingItem(RingItem.Type.YELLOW));
    public static final Supplier<RingItem> GREEN_RING = ITEMS.register("green_ring", () -> new RingItem(RingItem.Type.GREEN));

    public static final Supplier<WorldWoodSignItem> WORLD_WOOD_SIGN = ITEMS.register("world_wood_sign", WorldWoodSignItem::new);
    public static final Supplier<WorldWoodHangingSignItem> WORLD_WOOD_HANGING_SIGN = ITEMS.register("world_wood_hanging_sign", WorldWoodHangingSignItem::new);

    static {
        ModBlocks.BLOCKS.getEntries()
                .stream()
                .filter(block -> !ModBlocks.SPECIAL_ITEM_BLOCKS.contains(block))
                .forEach(ITEMS::registerSimpleBlockItem);
    }
}
