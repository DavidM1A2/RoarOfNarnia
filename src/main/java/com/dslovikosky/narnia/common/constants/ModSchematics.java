package com.dslovikosky.narnia.common.constants;

import com.dslovikosky.narnia.common.world.schematic.Schematic;
import com.dslovikosky.narnia.common.world.schematic.SchematicLoader;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.packs.resources.ResourceManager;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ModSchematics {
    public static final Schematic UNCLE_ANDREWS_HOUSE = load("uncle_andrews_house");

    private static final Map<ResourceLocation, Schematic> ID_TO_SCHEMATIC = Stream.of(UNCLE_ANDREWS_HOUSE)
            .collect(Collectors.toMap(Schematic::id, Function.identity()));

    public static Schematic get(final ResourceLocation id) {
        return ID_TO_SCHEMATIC.get(id);
    }

    private static Schematic load(final String name) {
        final MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        final ResourceManager resourceManager = server.getResourceManager();
        final RegistryAccess.Frozen registryAccess = server.registryAccess();
        return SchematicLoader.load(registryAccess, resourceManager, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, String.format("schematic/%s.schem", name)));
    }
}
