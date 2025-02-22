package com.dslovikosky.narnia.client.gui.control;

import com.dslovikosky.narnia.client.gui.event.KeyEvent;
import com.dslovikosky.narnia.client.gui.event.MouseDragEvent;
import com.dslovikosky.narnia.client.gui.event.MouseEvent;
import com.dslovikosky.narnia.client.gui.event.MouseMoveEvent;
import com.dslovikosky.narnia.client.gui.event.MouseScrollEvent;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public abstract class GuiComponentWithEvents extends GuiComponent {
    // Add a mouse move listener to this control that allows us to fire off events whenever conditions are met
    private static final Consumer<MouseMoveEvent> DEFAULT_LISTENER = it -> {
        // If we move the mouse also fire other related events
        if (it.getEventType() == MouseMoveEvent.EventType.Move) {
            // Grab the source of the event
            final GuiComponentWithEvents component = it.getSource();
            // Store the flag telling us if the component was hovered
            final boolean wasHovered = component.isHovered();
            // Set the hovered flag based on if we intersect the component
            component.setHovered(component.isVisible() && component.isInBounds() && component.intersects(new Point(it.getMouseX(), it.getMouseY())));
            // Fire mouse enter/exit events if our mouse entered or exited the control
            if (component.isHovered() && !wasHovered) {
                component.processMouseMoveInput(new MouseMoveEvent(component, it.getMouseX(), it.getMouseY(), MouseMoveEvent.EventType.Enter));
            }
            if (!component.isHovered() && wasHovered) {
                component.processMouseMoveInput(new MouseMoveEvent(component, it.getMouseX(), it.getMouseY(), MouseMoveEvent.EventType.Exit));
            }
        }
    };

    private final List<Consumer<MouseEvent>> mouseListeners = new ArrayList<>();
    private final List<Consumer<MouseMoveEvent>> mouseMoveListeners = new ArrayList<>();
    private final List<Consumer<MouseDragEvent>> mouseDragListeners = new ArrayList<>();
    private final List<Consumer<MouseScrollEvent>> mouseScrollListeners = new ArrayList<>();
    private final List<Consumer<KeyEvent>> keyListeners = new ArrayList<>();

    public GuiComponentWithEvents() {
        this.addMouseMoveListener(DEFAULT_LISTENER);
    }

    public void processMouseInput(final MouseEvent event) {
        // If the event is consumed, don't do anything
        if (event.isConsumed()) {
            return;
        }

        // We set the source to be this component, because we are processing it
        event.setSource(this);

        mouseListeners.forEach(it -> it.accept(event));
    }

    public void processMouseMoveInput(final MouseMoveEvent event) {
        // If the event is consumed, don't do anything
        if (event.isConsumed()) {
            return;
        }

        // We set the source to be this component, because we are processing it
        event.setSource(this);

        mouseMoveListeners.forEach(it -> it.accept(event));
    }

    public void processMouseDragInput(final MouseDragEvent event) {
        // If the event is consumed, don't do anything
        if (event.isConsumed()) {
            return;
        }

        // We set the source to be this component, because we are processing it
        event.setSource(this);

        mouseDragListeners.forEach(it -> it.accept(event));
    }

    public void processMouseScrollInput(final MouseScrollEvent event) {
        // If the event is consumed, don't do anything
        if (event.isConsumed()) {
            return;
        }

        // We set the source to be this component, because we are processing it
        event.setSource(this);

        mouseScrollListeners.forEach(it -> it.accept(event));
    }

    public void processKeyInput(final KeyEvent event) {
        // If the event is consumed, don't do anything
        if (event.isConsumed()) {
            return;
        }

        // We set the source to be this component, because we are processing it
        event.setSource(this);

        keyListeners.forEach(it -> it.accept(event));
    }

    public void addMouseListener(final Consumer<MouseEvent> mouseListener) {
        this.mouseListeners.add(mouseListener);
    }

    public void removeMouseListener(final Consumer<MouseEvent> mouseListener) {
        this.mouseListeners.remove(mouseListener);
    }

    public void clearMouseListeners() {
        this.mouseListeners.clear();
    }

    public void addMouseMoveListener(final Consumer<MouseMoveEvent> mouseMoveListener) {
        this.mouseMoveListeners.add(mouseMoveListener);
    }

    public void removeMouseMoveListener(final Consumer<MouseMoveEvent> mouseMoveListener) {
        this.mouseMoveListeners.remove(mouseMoveListener);
    }

    public void clearMouseMoveListeners() {
        this.mouseMoveListeners.clear();
        this.mouseMoveListeners.add(DEFAULT_LISTENER);
    }

    public void addMouseDragListener(final Consumer<MouseDragEvent> mouseDragListener) {
        this.mouseDragListeners.add(mouseDragListener);
    }

    public void removeMouseDragListener(final Consumer<MouseDragEvent> mouseDragListener) {
        this.mouseDragListeners.remove(mouseDragListener);
    }

    public void clearMouseDragListeners() {
        this.mouseDragListeners.clear();
    }

    public void addMouseScrollListener(final Consumer<MouseScrollEvent> mouseScrollListener) {
        this.mouseScrollListeners.add(mouseScrollListener);
    }

    public void removeMouseScrollListener(final Consumer<MouseScrollEvent> mouseScrollListener) {
        this.mouseScrollListeners.remove(mouseScrollListener);
    }

    public void clearMouseScrollListeners() {
        this.mouseScrollListeners.clear();
    }

    public void addKeyListener(final Consumer<KeyEvent> keyListener) {
        this.keyListeners.add(keyListener);
    }

    public void removeKeyListener(final Consumer<KeyEvent> keyListener) {
        this.keyListeners.remove(keyListener);
    }

    public void clearKeyListeners() {
        this.keyListeners.clear();
    }
}
