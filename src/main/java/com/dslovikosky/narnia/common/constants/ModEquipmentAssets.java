package com.dslovikosky.narnia.common.constants;

import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.equipment.EquipmentAsset;

public class ModEquipmentAssets {
    private static final ResourceKey<? extends Registry<EquipmentAsset>> EQUIPMENT_ASSET = ResourceKey.createRegistryKey(Identifier.withDefaultNamespace("equipment_asset"));

    public static final ResourceKey<EquipmentAsset> ANCIENT = ResourceKey.create(EQUIPMENT_ASSET, Constants.modLocation("ancient"));
}
