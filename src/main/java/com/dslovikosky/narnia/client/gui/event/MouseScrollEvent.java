package com.dslovikosky.narnia.client.gui.event;

import com.dslovikosky.narnia.client.gui.control.GuiComponentWithEvents;

public class MouseScrollEvent extends GuiEvent {
    private final int scrollDistance;

    public MouseScrollEvent(final GuiComponentWithEvents source, final int scrollDistance) {
        super(source);
        this.scrollDistance = scrollDistance;
    }

    public int getScrollDistance() {
        return scrollDistance;
    }
}
