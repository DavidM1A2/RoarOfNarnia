package com.dslovikosky.narnia.common.item;

import com.dslovikosky.narnia.common.constants.Constants;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;

public class AslansChronicleItem extends Item {
    public AslansChronicleItem() {
        super(new Properties().stacksTo(1).fireResistant().setId(ResourceKey.create(Registries.ITEM, Constants.modLocation("aslans_chronicle"))));
    }
}
