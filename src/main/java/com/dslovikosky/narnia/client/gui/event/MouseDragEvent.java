package com.dslovikosky.narnia.client.gui.event;

import com.dslovikosky.narnia.client.gui.control.GuiComponentWithEvents;

public class MouseDragEvent extends GuiEvent {
    private final int mouseX;
    private final int mouseY;
    private final int clickedButton;

    public MouseDragEvent(final GuiComponentWithEvents source, final int mouseX, final int mouseY, final int clickedButton) {
        super(source);
        this.mouseX = mouseX;
        this.mouseY = mouseY;
        this.clickedButton = clickedButton;
    }

    public int getMouseX() {
        return mouseX;
    }

    public int getMouseY() {
        return mouseY;
    }

    public int getClickedButton() {
        return clickedButton;
    }
}
