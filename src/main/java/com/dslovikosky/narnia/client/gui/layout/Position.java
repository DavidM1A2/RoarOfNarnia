package com.dslovikosky.narnia.client.gui.layout;

import com.dslovikosky.narnia.client.gui.control.GuiPane;

public record Position(double x, double y, boolean isRelative) {
    public Position getAbsolute(final GuiPane reference) {
        if (isRelative) {
            return new Position(x * reference.getInternalWidth(), y * reference.getInternalHeight(), false);
        } else {
            return new Position(x, y, false);
        }
    }

    public Position getRelative(final GuiPane reference) {
        if (isRelative) {
            return new Position(x, y, true);
        } else {
            final double internalWidth = reference.getInternalWidth();
            final double internalHeight = reference.getInternalHeight();
            return new Position(internalWidth == 0.0 ? 0.0 : x / internalWidth, internalHeight == 0.0 ? 0.0 : y / internalHeight, true);
        }
    }

    public Dimensions dimensionsBetween(final Position secondPoint) {
        assert (this.isRelative == secondPoint.isRelative);
        return new Dimensions(secondPoint.x - this.x, secondPoint.y - this.y, secondPoint.isRelative);
    }

    public Position avg(final Position secondPoint) {
        assert (this.isRelative == secondPoint.isRelative);
        return new Position((this.x + secondPoint.x) / 2, (this.y + secondPoint.y) / 2, secondPoint.isRelative);
    }

    @Override
    public boolean equals(final Object obj) {
        return obj instanceof Position(double x1, double y1, boolean relative) && relative == this.isRelative && x1 == this.x && y1 == this.y;
    }
}
