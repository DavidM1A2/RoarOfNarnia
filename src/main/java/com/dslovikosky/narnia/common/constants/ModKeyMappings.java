package com.dslovikosky.narnia.common.constants;

import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;

import java.util.List;

public class ModKeyMappings {
    public static final KeyMapping.Category NARNIA = new KeyMapping.Category(Constants.modLocation("narnia"));
    private static final String POWER_SOURCE_SELECTOR_NAME = "key.narnia.power_source_selection";
    public static final KeyMapping POWER_SOURCE_SELECTOR = new KeyMapping(POWER_SOURCE_SELECTOR_NAME, GLFW.GLFW_KEY_R, NARNIA);
    public static final List<KeyMapping> KEY_MAPPINGS = List.of(POWER_SOURCE_SELECTOR);
}
