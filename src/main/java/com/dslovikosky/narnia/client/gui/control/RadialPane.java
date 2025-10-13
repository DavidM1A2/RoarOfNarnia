package com.dslovikosky.narnia.client.gui.control;

import com.dslovikosky.narnia.client.gui.layout.Gravity;
import com.dslovikosky.narnia.client.gui.layout.Spacing;

public class RadialPane extends RatioPane {
    public RadialPane() {
        super(1, 1);
    }

    @Override
    public void calcChildrenBounds() {
        final Spacing calcPadding = getPadding().getAbsoluteOuter(this);
        final double internalWidth = this.getInternalWidth();
        final double internalHeight = this.getInternalHeight();

        for (final GuiComponent child : this.getChildren()) {
            // Calculate margins
            final Spacing calcMargins = child.getMargins().getAbsoluteInner(this);
            final double marginWidth = calcMargins.getWidth();
            final double marginHeight = calcMargins.getHeight();

            // Set dimensions
            child.negotiateDimensions(internalWidth - marginWidth, internalHeight - marginHeight);

            // Calculate position
            final double gravityXOffset = switch (child.getGravity()) {
                case Gravity.TOP_LEFT, Gravity.CENTER_LEFT, Gravity.BOTTOM_LEFT -> calcMargins.left();
                case Gravity.TOP_CENTER, Gravity.CENTER, Gravity.BOTTOM_CENTER -> -(child.getWidth() + calcMargins.getWidth()) / 2 + calcMargins.left();
                case Gravity.TOP_RIGHT, Gravity.CENTER_RIGHT, Gravity.BOTTOM_RIGHT -> -child.getWidth() - calcMargins.right();
            };
            final double gravityYOffset = switch (child.getGravity()) {
                case Gravity.TOP_LEFT, Gravity.TOP_CENTER, Gravity.TOP_RIGHT -> calcMargins.top();
                case Gravity.CENTER_LEFT, Gravity.CENTER, Gravity.CENTER_RIGHT -> -(child.getHeight() + calcMargins.getHeight()) / 2 + calcMargins.top();
                case Gravity.BOTTOM_LEFT, Gravity.BOTTOM_CENTER, Gravity.BOTTOM_RIGHT -> -child.getHeight() - calcMargins.bottom();
            };

            // Treat children offsets as polar coordinates (r,theta)
            final double rVal = child.getOffset().isRelative() ? child.getOffset().x() * internalWidth / 2 : child.getOffset().x();
            final double tVal = child.getOffset().isRelative() ? child.getOffset().y() * 2 * Math.PI : child.getOffset().y();

            // Set position
            child.setX((int) Math.round(this.getX() + this.getGuiOffsetX() + calcPadding.left() + gravityXOffset + Math.cos(tVal) * rVal + internalWidth / 2));
            child.setY((int) Math.round(this.getY() + this.getGuiOffsetY() + calcPadding.top() + gravityYOffset + Math.sin(tVal) * rVal + internalHeight / 2));

            // If it's a pane, have it recalculate its children too
            if (child instanceof GuiPane pane) pane.calcChildrenBounds();
            // Determine if the subtree of children is in this node's bounds
            determineInBounds(child);
        }
    }
}
