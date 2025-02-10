package com.dslovikosky.narnia.common.model.schematic;

import com.google.common.base.Suppliers;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.function.Supplier;

public class SchematicMarkerPosition {
    private final DeferredHolder<Schematic, Schematic> schematic;
    private final Vec3 baseOffset;

    public SchematicMarkerPosition(final DeferredHolder<Schematic, Schematic> schematic, final Vec3 baseOffset) {
        this.schematic = schematic;
        this.baseOffset = baseOffset;
    }

    public Supplier<Vec3> named(final String name) {
        return Suppliers.memoize(() -> baseOffset.add(schematic.get().getMarkers().get(name)));
    }

    public Supplier<Vec3> named(final String name, final Vec3 offset) {
        return Suppliers.memoize(() -> baseOffset.add(schematic.get().getMarkers().get(name)).add(offset));
    }
}
