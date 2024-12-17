package com.dslovikosky.narnia.common.item.worldwood;

import com.dslovikosky.narnia.common.asm.BoatTypeExtension;
import net.minecraft.world.item.BoatItem;
import net.minecraft.world.item.Item;

public class WorldWoodBoatItem extends BoatItem {
    public WorldWoodBoatItem(final boolean pHasChest) {
        super(pHasChest, BoatTypeExtension.WORLD_WOOD_BOAT_TYPE_PROXY.getValue(), new Item.Properties().stacksTo(1));
    }
}
