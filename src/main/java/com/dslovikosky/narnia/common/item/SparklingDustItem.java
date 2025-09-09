package com.dslovikosky.narnia.common.item;

import com.dslovikosky.narnia.common.constants.Constants;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;

public class SparklingDustItem extends Item {
    public SparklingDustItem() {
        super(new Properties().setId(ResourceKey.create(Registries.ITEM, Constants.modLocation("sparkling_dust"))));
    }
}
