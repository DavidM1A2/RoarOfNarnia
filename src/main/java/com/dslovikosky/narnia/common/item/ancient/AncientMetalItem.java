package com.dslovikosky.narnia.common.item.ancient;

import com.dslovikosky.narnia.common.constants.Constants;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;

public class AncientMetalItem extends Item {
    public AncientMetalItem() {
        super(new Properties().setId(ResourceKey.create(Registries.ITEM, Constants.modLocation("ancient_metal"))));
    }
}
