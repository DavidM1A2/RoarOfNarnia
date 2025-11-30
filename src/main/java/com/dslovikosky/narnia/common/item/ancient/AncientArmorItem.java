package com.dslovikosky.narnia.common.item.ancient;

import com.dslovikosky.narnia.common.constants.Constants;
import com.dslovikosky.narnia.common.constants.ModArmorMaterials;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.equipment.ArmorType;

public class AncientArmorItem extends Item {
    public AncientArmorItem(final ArmorType type) {
        super(new Properties()
                .humanoidArmor(ModArmorMaterials.ANCIENT, type)
                .setId(ResourceKey.create(Registries.ITEM, Constants.modLocation("ancient_" + type.getName()))));
    }
}
