package com.dslovikosky.narnia.client.gui.layout;

import com.mojang.blaze3d.platform.Window;
import net.minecraft.client.Minecraft;

public class GuiUtility {
    private static final Minecraft MINECRAFT = Minecraft.getInstance();
    private static final Window WINDOW = MINECRAFT.getWindow();

    public static int mcToRealScreenCoord(final int mcCoord) {
        return mcCoord * WINDOW.calculateScale(MINECRAFT.options.guiScale().get(), MINECRAFT.isEnforceUnicode());
    }

    public static int realScreenCoordToMC(final int realScreenCoord) {
        return realScreenCoord / WINDOW.calculateScale(MINECRAFT.options.guiScale().get(), MINECRAFT.isEnforceUnicode());
    }

    public static int realScreenYToGLYCoord(final int realScreenY) {
        return WINDOW.getHeight() - realScreenY;
    }

    public static int getMouseXInMCCoord() {
        if (WINDOW.getWidth() == 0) {
            return 0;
        } else {
            return (int) Math.round(MINECRAFT.mouseHandler.xpos()) * WINDOW.getGuiScaledWidth() / WINDOW.getWidth();
        }
    }

    public static int getMouseYInMCCoord() {
        if (WINDOW.getHeight() == 0) {
            return 0;
        } else {
            return (int) Math.round(MINECRAFT.mouseHandler.ypos()) * WINDOW.getGuiScaledHeight() / WINDOW.getHeight();
        }
    }

    public static int getWindowWidthInMCCoords() {
        return realScreenCoordToMC(WINDOW.getWidth());
    }

    public static int getWindowHeightInMCCoords() {
        return realScreenCoordToMC(WINDOW.getHeight());
    }

    public static Dimensions getWindowSizeInMCCoords() {
        return new Dimensions(getWindowWidthInMCCoords(), getWindowHeightInMCCoords(), false);
    }
}
