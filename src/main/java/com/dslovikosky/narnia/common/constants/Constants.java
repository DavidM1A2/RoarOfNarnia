package com.dslovikosky.narnia.common.constants;

import net.minecraft.resources.ResourceLocation;

public class Constants {
    public static final String MOD_ID = "narnia";

    public static ResourceLocation modLocation(final String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }
}
