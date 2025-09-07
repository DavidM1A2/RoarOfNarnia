package com.dslovikosky.narnia.common.constants;

import com.dslovikosky.narnia.common.model.schematic.Schematic;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.RegistryBuilder;

import java.util.List;

public class ModRegistries {
    public static final ResourceKey<Registry<Schematic>> SCHEMATIC_KEY = ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "schematic"));

    public static final Registry<Schematic> SCHEMATIC = new RegistryBuilder<>(SCHEMATIC_KEY).sync(false).maxId(256).create();

    public static final List<Registry<?>> REGISTRIES = List.of(SCHEMATIC);
}
