package com.dslovikosky.narnia.client.gui.control;

import net.minecraft.client.gui.GuiGraphics;

public class StackPane extends GuiPane {
    private boolean scissorsEnabled = false;

    @Override
    public void draw(final GuiGraphics guiGraphics) {
        if (scissorsEnabled) {
            guiGraphics.enableScissor(getX(), getY(), getX() + getWidth(), getY() + getHeight());
        }

        super.draw(guiGraphics);

        if (scissorsEnabled) {
            guiGraphics.disableScissor();
        }
    }

    public boolean isScissorsEnabled() {
        return scissorsEnabled;
    }

    public void setScissorsEnabled(final boolean scissorsEnabled) {
        this.scissorsEnabled = scissorsEnabled;
    }
}
