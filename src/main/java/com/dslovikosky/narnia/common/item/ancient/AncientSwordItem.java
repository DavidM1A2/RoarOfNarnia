package com.dslovikosky.narnia.common.item.ancient;

import com.dslovikosky.narnia.common.constants.Constants;
import com.dslovikosky.narnia.common.constants.ModToolMaterials;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;

public class AncientSwordItem extends Item {
    public AncientSwordItem() {
        super(new Properties()
                .sword(ModToolMaterials.ANCIENT, 3.0F, -2.4F)
                .setId(ResourceKey.create(Registries.ITEM, Constants.modLocation("ancient_sword"))));
    }
}
