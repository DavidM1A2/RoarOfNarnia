package com.dslovikosky.narnia.common.item;

import com.dslovikosky.narnia.common.constants.ModBlocks;
import net.minecraft.world.item.HangingSignItem;
import org.jetbrains.annotations.NotNull;

public class WorldWoodHangingSignItem extends HangingSignItem {
    public WorldWoodHangingSignItem() {
        super(ModBlocks.WORLD_WOOD_WALL_HANGING_SIGN.get(), ModBlocks.WORLD_WOOD_CEILING_HANGING_SIGN.get(), new Properties().stacksTo(16));
    }

    @Override
    public @NotNull String getDescriptionId() {
        return super.getOrCreateDescriptionId();
    }
}
