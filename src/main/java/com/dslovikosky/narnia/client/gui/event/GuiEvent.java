package com.dslovikosky.narnia.client.gui.event;

import com.dslovikosky.narnia.client.gui.control.GuiComponentWithEvents;

public class GuiEvent {
    private GuiComponentWithEvents source;
    private boolean isConsumed = false;

    public GuiEvent(GuiComponentWithEvents source) {
        this.source = source;
    }

    public void consume() {
        isConsumed = true;
    }

    public boolean isConsumed() {
        return isConsumed;
    }

    public GuiComponentWithEvents getSource() {
        return source;
    }

    public void setSource(final GuiComponentWithEvents source) {
        this.source = source;
    }
}
