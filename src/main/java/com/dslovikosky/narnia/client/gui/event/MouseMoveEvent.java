package com.dslovikosky.narnia.client.gui.event;

import com.dslovikosky.narnia.client.gui.control.GuiComponentWithEvents;

public class MouseMoveEvent extends GuiEvent {
    private final int mouseX;
    private final int mouseY;
    private final EventType eventType;

    public MouseMoveEvent(final GuiComponentWithEvents source, final int mouseX, final int mouseY, final EventType eventType) {
        super(source);
        this.mouseX = mouseX;
        this.mouseY = mouseY;
        this.eventType = eventType;
    }

    public int getMouseX() {
        return mouseX;
    }

    public int getMouseY() {
        return mouseY;
    }

    public EventType getEventType() {
        return eventType;
    }

    public enum EventType {
        Enter,
        Exit,
        Move
    }
}
