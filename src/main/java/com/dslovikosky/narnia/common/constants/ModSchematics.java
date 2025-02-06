package com.dslovikosky.narnia.common.constants;

import com.dslovikosky.narnia.common.model.schematic.Schematic;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModSchematics {
    public static final DeferredRegister<Schematic> SCHEMATICS = DeferredRegister.create(ModRegistries.SCHEMATIC, Constants.MOD_ID);

    public static final DeferredHolder<Schematic, Schematic> UNCLE_ANDREWS_HOUSE = SCHEMATICS.register("uncle_andrews_house", Schematic::new);
}
