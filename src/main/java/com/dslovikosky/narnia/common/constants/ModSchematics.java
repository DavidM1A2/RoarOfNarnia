package com.dslovikosky.narnia.common.constants;

import com.dslovikosky.narnia.common.model.schematic.Schematic;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModSchematics {
    public static final DeferredRegister<Schematic> SCHEMATICS = DeferredRegister.create(ModRegistries.SCHEMATIC, Constants.MOD_ID);

    public static final DeferredHolder<Schematic, Schematic> DARK_CITY_SMALL_1 = SCHEMATICS.register("dark_city_small_1", Schematic::new);
}
