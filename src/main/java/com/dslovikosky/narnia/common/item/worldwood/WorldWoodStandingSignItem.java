package com.dslovikosky.narnia.common.item.worldwood;

import com.dslovikosky.narnia.common.constants.Constants;
import com.dslovikosky.narnia.common.constants.ModBlocks;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.StandingAndWallBlockItem;

public class WorldWoodStandingSignItem extends StandingAndWallBlockItem {
    public WorldWoodStandingSignItem() {
        super(ModBlocks.WORLD_WOOD_STANDING_SIGN.get(), ModBlocks.WORLD_WOOD_WALL_SIGN.get(), Direction.DOWN, new Item.Properties().stacksTo(16)
                .signText()
                .setId(ResourceKey.create(Registries.ITEM, Constants.modLocation("world_wood_standing_sign"))));
    }
}
