package com.dslovikosky.narnia.client.gui.control;

import com.dslovikosky.narnia.client.gui.event.KeyEvent;
import com.dslovikosky.narnia.client.gui.event.MouseDragEvent;
import com.dslovikosky.narnia.client.gui.event.MouseEvent;
import com.dslovikosky.narnia.client.gui.event.MouseMoveEvent;
import com.dslovikosky.narnia.client.gui.event.MouseScrollEvent;
import com.dslovikosky.narnia.client.gui.layout.Gravity;
import com.dslovikosky.narnia.client.gui.layout.GuiUtility;
import com.dslovikosky.narnia.client.gui.layout.Position;
import com.dslovikosky.narnia.client.gui.layout.Spacing;
import net.minecraft.client.gui.GuiGraphics;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public abstract class GuiPane extends GuiComponentWithEvents {
    // Panes can have children
    private final List<GuiComponent> subComponents = new CopyOnWriteArrayList<>();
    private Spacing padding = new Spacing(0.0, true);
    // Special offsets so that panes can scroll with children
    private double guiOffsetX = 0.0;
    private double guiOffsetY = 0.0;

    @Override
    public void setVisible(final boolean visible) {
        super.setVisible(visible);
        this.subComponents.forEach(component -> component.setVisible(visible));
    }

    public void add(final GuiComponent component) {
        // Add the sub-component
        this.subComponents.add(component);
    }

    public void remove(final GuiComponent component) {
        if (!this.subComponents.contains(component)) {
            return;
        }
        this.subComponents.remove(component);
    }

    public double getInternalWidth() {
        return getWidth() - padding.getAbsoluteOuter(this).getWidth();
    }

    public double getInternalHeight() {
        return getHeight() - padding.getAbsoluteOuter(this).getHeight();
    }

    /**
     * Calculates the proper locations and dimensions of all children nodes
     * Default behavior is that of a StackPane (children drawn on top of each other)
     */
    public void calcChildrenBounds() {
        // Calculate padding
        final Spacing calcPadding = padding.getAbsoluteOuter(this);
        final double internalWidth = this.getInternalWidth();
        final double internalHeight = this.getInternalHeight();

        for (final GuiComponent child : subComponents) {
            // Calculate margins
            if (child.getMargins().isRelative()) {
                // Set dimensions based on margins
                child.negotiateDimensions(internalWidth / (1.0 + child.getMargins().getWidth()), internalHeight / (1.0 + child.getMargins().getHeight()));
            } else {
                child.negotiateDimensions(internalWidth - child.getMargins().getWidth(), internalHeight - child.getMargins().getHeight());
            }
            final Spacing calcMargins = child.getMargins().getAbsoluteOuter(child);
            final double marginWidth = calcMargins.getWidth();
            final double marginHeight = calcMargins.getHeight();
            // Set dimensions
            child.negotiateDimensions(internalWidth - marginWidth, internalHeight - marginHeight);
            // Calculate position
            final double gravityXOffset = switch (child.getGravity()) {
                case Gravity.TOP_LEFT, Gravity.CENTER_LEFT, Gravity.BOTTOM_LEFT -> calcMargins.left();
                case Gravity.TOP_CENTER, Gravity.CENTER, Gravity.BOTTOM_CENTER -> internalWidth / 2 - (child.getWidth() + calcMargins.getWidth()) / 2 + calcMargins.left();
                case Gravity.TOP_RIGHT, Gravity.CENTER_RIGHT, Gravity.BOTTOM_RIGHT -> internalWidth - child.getWidth() - calcMargins.right();
            };
            final double gravityYOffset = switch (child.getGravity()) {
                case Gravity.TOP_LEFT, Gravity.TOP_CENTER, Gravity.TOP_RIGHT -> calcMargins.top();
                case Gravity.CENTER_LEFT, Gravity.CENTER, Gravity.CENTER_RIGHT -> internalHeight / 2 - (child.getHeight() + calcMargins.getHeight()) / 2 + calcMargins.top();
                case Gravity.BOTTOM_LEFT, Gravity.BOTTOM_CENTER, Gravity.BOTTOM_RIGHT -> internalHeight - child.getHeight() - calcMargins.bottom();
            };
            final Position offset = child.getOffset().getAbsolute(this);
            // Set position
            child.setX((int) Math.round(this.getX() + this.guiOffsetX + calcPadding.left() + gravityXOffset + offset.x()));
            child.setY((int) Math.round(this.getY() + this.guiOffsetY + calcPadding.top() + gravityYOffset + offset.y()));
            // If it's a pane, have it recalculate its children too
            if (child instanceof GuiPane guiPane) {
                guiPane.calcChildrenBounds();
            }
            // Determine if the subtree of children are in this node's bounds
            determineInBounds(child);
        }
    }

    public void determineInBounds(final GuiComponent component) {
        if (!component.intersects(this.computeBoundingBox())) {
            component.setInBounds(false);
        }
        if (component instanceof GuiPane guiPane) {
            for (final GuiComponent child : guiPane.getChildren()) {
                determineInBounds(child);
            }
        }
    }

    public List<GuiComponent> getChildren() {
        return Collections.unmodifiableList(subComponents);
    }

    @Override
    public void draw(final GuiGraphics guiGraphics) {
        // Then draw children
        this.subComponents.forEach(it -> it.draw(guiGraphics));
    }

    @Override
    public void drawOverlay(final GuiGraphics guiGraphics) {
        // Then draw children
        this.subComponents.forEach(it -> it.drawOverlay(guiGraphics));
    }

    @Override
    public void processMouseInput(final MouseEvent event) {
        // Fire our sub-component's mouse input events
        this.subComponents.forEach(it -> {
            if (it instanceof GuiComponentWithEvents guiComponentWithEvents) {
                guiComponentWithEvents.processMouseInput(event);
            }
        });
        // Fire our component's mouse input
        super.processMouseInput(event);
    }

    @Override
    public void processMouseMoveInput(final MouseMoveEvent event) {
        // Fire our sub-component's mouse input events
        this.subComponents.forEach(it -> {
            if (it instanceof GuiComponentWithEvents guiComponentWithEvents) {
                guiComponentWithEvents.processMouseMoveInput(event);
            }
        });
        // Fire our component's mouse input
        super.processMouseMoveInput(event);
    }

    @Override
    public void processMouseDragInput(final MouseDragEvent event) {
        // Fire our sub-component's mouse input events
        this.subComponents.forEach(it -> {
            if (it instanceof GuiComponentWithEvents guiComponentWithEvents) {
                guiComponentWithEvents.processMouseDragInput(event);
            }
        });
        // Fire our component's mouse input
        super.processMouseDragInput(event);
    }

    @Override
    public void processMouseScrollInput(final MouseScrollEvent event) {
        // Fire our sub-component's mouse input events
        this.subComponents.forEach(it -> {
            if (it instanceof GuiComponentWithEvents guiComponentWithEvents) {
                guiComponentWithEvents.processMouseScrollInput(event);
            }
        });
        // Fire our component's mouse input
        super.processMouseScrollInput(event);
    }

    @Override
    public void processKeyInput(final KeyEvent event) {
        // Fire our sub-component's mouse input events
        this.subComponents.forEach(it -> {
            if (it instanceof GuiComponentWithEvents guiComponentWithEvents) {
                guiComponentWithEvents.processKeyInput(event);
            }
        });
        // Fire our component's mouse input
        super.processKeyInput(event);
    }

    @Override
    public void update() {
        this.setHovered(this.computeBoundingBox().contains(GuiUtility.getMouseXInMCCoord(), GuiUtility.getMouseYInMCCoord()));
        calcChildrenBounds();
        subComponents.forEach(GuiComponent::update);
    }

    public Spacing getPadding() {
        return padding;
    }

    public void setPadding(final Spacing padding) {
        this.padding = padding;
    }
}
