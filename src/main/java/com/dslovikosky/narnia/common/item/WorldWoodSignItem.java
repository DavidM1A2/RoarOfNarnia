package com.dslovikosky.narnia.common.item;

import com.dslovikosky.narnia.common.constants.ModBlocks;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SignItem;
import org.jetbrains.annotations.NotNull;

public class WorldWoodSignItem extends SignItem {
    public WorldWoodSignItem() {
        super(new Item.Properties().stacksTo(16), ModBlocks.WORLD_WOOD_STANDING_SIGN.get(), ModBlocks.WORLD_WOOD_WALL_SIGN.get());
    }

    @Override
    public @NotNull String getDescriptionId() {
        return super.getOrCreateDescriptionId();
    }
}
