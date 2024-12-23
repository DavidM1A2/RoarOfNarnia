package com.dslovikosky.narnia.client.gui.layout;

import com.dslovikosky.narnia.client.gui.control.GuiComponent;
import com.dslovikosky.narnia.client.gui.control.GuiPane;

public record Spacing(double top, double bottom, double left, double right, boolean isRelative) {
    public Spacing(double uniform, boolean isRelative) {
        this(uniform, uniform, uniform, uniform, isRelative);
    }

    public Spacing getAbsoluteOuter(final GuiComponent reference) {
        if (isRelative) {
            return new Spacing(top * reference.getHeight(), bottom * reference.getHeight(), left * reference.getWidth(), right * reference.getWidth(), false);
        } else {
            return new Spacing(top, bottom, left, right, false);
        }
    }

    public Spacing getAbsoluteInner(final GuiPane reference) {
        if (isRelative) {
            return new Spacing(top * reference.getInternalHeight(), bottom * reference.getInternalHeight(), left * reference.getInternalWidth(), right * reference.getInternalWidth(), false);
        } else {
            return new Spacing(top, bottom, left, right, false);
        }
    }

    public double getWidth() {
        return left + right;
    }

    public double getHeight() {
        return top + bottom;
    }
}
