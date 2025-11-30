package com.dslovikosky.narnia.common.constants;

import com.dslovikosky.narnia.common.model.schematic.Schematic;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModSchematics {
    public static final DeferredRegister<Schematic> SCHEMATICS = DeferredRegister.create(ModRegistries.SCHEMATIC, Constants.MOD_ID);

    public static final DeferredHolder<Schematic, Schematic> DARK_CITY_SMALL_1 = SCHEMATICS.register("dark_city_small_1", Schematic::new);
    public static final DeferredHolder<Schematic, Schematic> DARK_CITY_GARDEN_1 = SCHEMATICS.register("dark_city_garden_1", Schematic::new);
    public static final DeferredHolder<Schematic, Schematic> DARK_CITY_LARGE_1 = SCHEMATICS.register("dark_city_large_1", Schematic::new);
    public static final DeferredHolder<Schematic, Schematic> DARK_CITY_TOWER_1 = SCHEMATICS.register("dark_city_tower_1", Schematic::new);
    public static final DeferredHolder<Schematic, Schematic> DARK_CITY_PYRAMID_1 = SCHEMATICS.register("dark_city_pyramid_1", Schematic::new);
    public static final DeferredHolder<Schematic, Schematic> DARK_CITY_HALL_OF_IMAGES = SCHEMATICS.register("dark_city_hall_of_images", Schematic::new);
}
