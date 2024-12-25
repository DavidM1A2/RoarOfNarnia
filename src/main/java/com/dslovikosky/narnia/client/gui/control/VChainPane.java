package com.dslovikosky.narnia.client.gui.control;

import com.dslovikosky.narnia.client.gui.layout.ChainLayout;
import com.dslovikosky.narnia.client.gui.layout.Gravity;
import com.dslovikosky.narnia.client.gui.layout.Spacing;

public class VChainPane extends StackPane {
    private final ChainLayout layout;

    public VChainPane(final ChainLayout layout) {
        this.layout = layout;
    }

    @Override
    public void calcChildrenBounds() {
        // Calculate padding
        final Spacing calcPadding = getPadding().getAbsoluteInner(this);
        final double internalWidth = getInternalWidth();
        final double internalHeight = getInternalHeight();

        // Calculate an initial space allowance for each child
        final double initSpace = internalHeight / getChildren().size();
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
            final double childHeightWithMargins = child.getHeight() + calcMargins.getHeight();
            if (childHeightWithMargins < initSpace) {
                extraSpace += initSpace - childHeightWithMargins;
                requesterCount++;
            }
        }
        // Allocate extra space
        for (final GuiComponent child : getChildren()) {
            // Calculate margins
            final Spacing calcMargins = child.getMargins().getAbsoluteOuter(child);
            // Allocate space evenly
            final double childHeightWithMargins = child.getHeight() + calcMargins.getHeight();
            if (childHeightWithMargins > initSpace) {
                final double extraSpaceAllowed = extraSpace / requesterCount;
                final double requestedSpace = childHeightWithMargins - initSpace;
                if (requestedSpace > extraSpaceAllowed) {   // Request exceeds space allowance
                    child.negotiateDimensions(internalWidth, initSpace + extraSpaceAllowed - calcMargins.getHeight());
                    extraSpace -= extraSpaceAllowed;
                } else {    // Request is within space allowance
                    extraSpace -= requestedSpace;
                }
                requesterCount--;
            }
        }
        // Determine offsets
        double yOffset = switch (layout) {
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
            final double gravityXOffset = switch (child.getGravity()) {
                case Gravity.TOP_LEFT, Gravity.TOP_CENTER, Gravity.TOP_RIGHT -> calcPadding.left();
                case Gravity.CENTER_LEFT, Gravity.CENTER, Gravity.CENTER_RIGHT -> getWidth() / 2f - (child.getWidth() + marginWidth) / 2;
                case Gravity.BOTTOM_LEFT, Gravity.BOTTOM_CENTER, Gravity.BOTTOM_RIGHT -> getWidth() - (child.getWidth() + marginWidth) - calcPadding.right();
            };
            final double gravityYOffset = 0.0;    // Gravity doesn't affect the x dimension because cells are fitted to children
            // Calculate offset from position in HPane, not according to child.offset
            final double xOffset = 0.0;
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
            yOffset += child.getHeight() + marginHeight + layoutOffset;
            // If it's a pane, have it recalculate its children too
            if (child instanceof GuiPane guiPane) {
                guiPane.calcChildrenBounds();
            }
            // Determine if the subtree of children are in this node's bounds
            determineInBounds(child);
        }
    }
}
