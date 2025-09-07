package com.dslovikosky.narnia.common.item.worldwood;

import com.dslovikosky.narnia.common.constants.Constants;
import com.dslovikosky.narnia.common.constants.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SignItem;

public class WorldWoodSignItem extends SignItem {
    public WorldWoodSignItem() {
        super(ModBlocks.WORLD_WOOD_STANDING_SIGN.get(), ModBlocks.WORLD_WOOD_WALL_SIGN.get(), new Item.Properties().stacksTo(16)
                .useBlockDescriptionPrefix()
                .setId(ResourceKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "world_wood_standing_sign"))));
    }
}
