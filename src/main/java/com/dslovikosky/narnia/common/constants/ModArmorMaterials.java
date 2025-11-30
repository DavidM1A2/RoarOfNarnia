package com.dslovikosky.narnia.common.constants;

import com.google.common.collect.Maps;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;

import java.util.Map;

public class ModArmorMaterials {
    public static final ArmorMaterial ANCIENT = new ArmorMaterial(
            17,
            makeDefense(3, 6, 8, 3, 11),
            30,
            SoundEvents.ARMOR_EQUIP_IRON,
            3.0F,
            0.1F,
            ModItemTags.REPAIRS_ANCIENT_ARMOR,
            ModEquipmentAssets.ANCIENT
    );

    private static Map<ArmorType, Integer> makeDefense(final int boots, final int leggings, final int chestplate, final int helmet, final int body) {
        return Maps.newEnumMap(Map.of(
                ArmorType.BOOTS, boots,
                ArmorType.LEGGINGS, leggings,
                ArmorType.CHESTPLATE, chestplate,
                ArmorType.HELMET, helmet,
                ArmorType.BODY, body
        ));
    }
}
