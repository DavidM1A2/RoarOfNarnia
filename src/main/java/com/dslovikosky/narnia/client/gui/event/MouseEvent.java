package com.dslovikosky.narnia.client.gui.event;

import com.dslovikosky.narnia.client.gui.control.GuiComponentWithEvents;
import org.lwjgl.glfw.GLFW;

public class MouseEvent extends GuiEvent {
    // The mouse buttons that are standard
    public static final int LEFT_MOUSE_BUTTON = GLFW.GLFW_MOUSE_BUTTON_LEFT;
    public static final int RIGHT_MOUSE_BUTTON = GLFW.GLFW_MOUSE_BUTTON_RIGHT;
    public static final int MIDDLE_MOUSE_BUTTON = GLFW.GLFW_MOUSE_BUTTON_MIDDLE;

    private final int mouseX;
    private final int mouseY;
    private final int clickedButton;
    private final EventType eventType;

    public MouseEvent(final GuiComponentWithEvents source, final int mouseX, final int mouseY, final int clickedButton, final EventType eventType) {
        super(source);
        this.mouseX = mouseX;
        this.mouseY = mouseY;
        this.clickedButton = clickedButton;
        this.eventType = eventType;
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

    public EventType getEventType() {
        return eventType;
    }

    public enum EventType {
        Click,
        Release
    }
}
