package com.dslovikosky.narnia.common.item.worldwood;

import com.dslovikosky.narnia.common.constants.Constants;
import com.dslovikosky.narnia.common.constants.ModEntityTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BoatItem;

public class WorldWoodChestBoatItem extends BoatItem {
    public WorldWoodChestBoatItem() {
        super(ModEntityTypes.WORLD_WOOD_CHEST_BOAT.get(), new Properties().stacksTo(1)
                .setId(ResourceKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "world_wood_chest_boat"))));
    }
}
