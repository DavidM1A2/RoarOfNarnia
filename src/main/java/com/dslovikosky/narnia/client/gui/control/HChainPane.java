package com.dslovikosky.narnia.client.gui.control;

import com.dslovikosky.narnia.client.gui.layout.ChainLayout;
import com.dslovikosky.narnia.client.gui.layout.Gravity;
import com.dslovikosky.narnia.client.gui.layout.Spacing;

public class HChainPane extends StackPane {
    private final ChainLayout layout;

    public HChainPane(final ChainLayout layout) {
        this.layout = layout;
    }

    @Override
    public void calcChildrenBounds() {
        // Calculate padding
        final Spacing calcPadding = getPadding().getAbsoluteInner(this);
        final double internalWidth = getInternalWidth();
        final double internalHeight = getInternalHeight();

        // Calculate an initial space allowance for each child
        final double initSpace = internalWidth / getChildren().size();
        double extraSpace = 0.0;

        // Set initial dimensions
        for (final GuiComponent child : getChildren()) {
            // Calculate margins
            final Spacing calcMargins = child.getMargins().getAbsoluteOuter(child);
            final double marginWidth = calcMargins.getWidth();
            final double marginHeight = calcMargins.getHeight();
            // Set dimensions
            child.negotiateDimensions(internalWidth - marginWidth, internalHeight - marginHeight);
        }
        // Find any extra space & Find children that want extra space
        int requesterCount = 0;
        for (final GuiComponent child : getChildren()) {
            // Calculate margins
            final Spacing calcMargins = child.getMargins().getAbsoluteOuter(child);
            // Accumulate extra space
            final double childWidthWithMargins = child.getWidth() + calcMargins.getWidth();
            if (childWidthWithMargins < initSpace) {
                extraSpace += initSpace - childWidthWithMargins;
                requesterCount++;
            }
        }
        // Allocate extra space
        for (final GuiComponent child : getChildren()) {
            // Calculate margins
            final Spacing calcMargins = child.getMargins().getAbsoluteOuter(child);
            // Allocate space evenly
            final double childWidthWithMargins = child.getWidth() + calcMargins.getWidth();
            if (childWidthWithMargins > initSpace) {
                final double extraSpaceAllowed = extraSpace / requesterCount;
                final double requestedSpace = childWidthWithMargins - initSpace;
                if (requestedSpace > extraSpaceAllowed) {   // Request exceeds space allowance
                    child.negotiateDimensions(initSpace + extraSpaceAllowed - calcMargins.getWidth(), internalHeight);
                    extraSpace -= extraSpaceAllowed;
                } else {    // Request is within space allowance
                    extraSpace -= requestedSpace;
                }
                requesterCount--;
            }
        }
        // Determine offsets
        double xOffset = switch (layout) {
            case ChainLayout.SPREAD -> 0.0;
            case ChainLayout.SPREAD_INSIDE -> extraSpace / (getChildren().size() + 1);
            case ChainLayout.CLOSE -> extraSpace / 2.0;
        };
        for (final GuiComponent child : getChildren()) {
            // Calculate margins
            final Spacing calcMargins = child.getMargins().getAbsoluteOuter(child);
            final double marginHeight = calcMargins.getHeight();
            final double marginWidth = calcMargins.getWidth();
            // Calculate position
            final double gravityXOffset = 0.0;    // Gravity doesn't affect the x dimension because cells are fitted to children
            final double gravityYOffset = switch (child.getGravity()) {
                case Gravity.TOP_LEFT, Gravity.TOP_CENTER, Gravity.TOP_RIGHT -> calcPadding.top();
                case Gravity.CENTER_LEFT, Gravity.CENTER, Gravity.CENTER_RIGHT -> getHeight() / 2f - (child.getHeight() + marginHeight) / 2;
                case Gravity.BOTTOM_LEFT, Gravity.BOTTOM_CENTER, Gravity.BOTTOM_RIGHT -> getHeight() - (child.getHeight() + marginHeight) - calcPadding.bottom();
            };
            // Calculate offset from position in HPane, not according to child.offset
            final double yOffset = 0.0;
            // Set position
            child.setX((int) Math.round(this.getX() + this.getGuiOffsetX() + gravityXOffset + xOffset + calcMargins.left()));
            child.setY((int) Math.round(this.getY() + this.getGuiOffsetY() + gravityYOffset + yOffset + calcMargins.top()));
            // Calculate offset based on our layout
            final double layoutOffset = switch (layout) {
                case ChainLayout.SPREAD -> getChildren().size() == 1 ? 0.0 : extraSpace / (getChildren().size() - 1);
                case ChainLayout.SPREAD_INSIDE -> extraSpace / (getChildren().size() + 1);
                case ChainLayout.CLOSE -> 0.0;
            };
            // Add to next child's offset
            xOffset += child.getWidth() + marginWidth + layoutOffset;
            // If it's a pane, have it recalculate its children too
            if (child instanceof GuiPane guiPane) {
                guiPane.calcChildrenBounds();
            }
            // Determine if the subtree of children are in this node's bounds
            determineInBounds(child);
        }
    }
}
