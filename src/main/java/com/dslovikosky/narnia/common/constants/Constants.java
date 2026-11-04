package com.dslovikosky.narnia.common.constants;

import net.minecraft.resources.Identifier;

public class Constants {
    public static final String MOD_ID = "narnia";

    public static Identifier modLocation(final String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }
}
