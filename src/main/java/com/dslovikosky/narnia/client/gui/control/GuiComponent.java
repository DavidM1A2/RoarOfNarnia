package com.dslovikosky.narnia.client.gui.control;

import com.dslovikosky.narnia.client.gui.layout.Dimensions;
import com.dslovikosky.narnia.client.gui.layout.Gravity;
import com.dslovikosky.narnia.client.gui.layout.GuiUtility;
import com.dslovikosky.narnia.client.gui.layout.Position;
import com.dslovikosky.narnia.client.gui.layout.Spacing;
import com.dslovikosky.narnia.client.gui.screen.BaseScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class GuiComponent {
    private static final Font FONT = Minecraft.getInstance().font;
    private Position offset = new Position(0.0, 0.0, true);
    private Dimensions prefSize = new Dimensions(1, 1, true);
    private Spacing margins = new Spacing(0.0, true);
    private Gravity gravity = Gravity.TOP_LEFT;
    private List<Component> hoverTexts = new ArrayList<>();
    private Color color = Color.WHITE;
    private int x = 0;
    private int y = 0;
    private int width = 0;
    private int height = 0;
    private boolean isHovered = false;
    private boolean isVisible = true;
    private boolean inBounds = true;

    public abstract void draw(final GuiGraphics guiGraphics);

    public abstract void update();

    public void drawOverlay(final GuiGraphics guiGraphics) {
        // Make sure the control is visible and hovered
        if (this.isVisible && this.inBounds && this.isHovered && !hoverTexts.isEmpty()) {
            // Grab the mouse X and Y coordinates to draw at
            final int mouseX = GuiUtility.getMouseXInMCCoord();
            final int mouseY = GuiUtility.getMouseYInMCCoord();
            guiGraphics.renderTooltip(FONT, hoverTexts, Optional.empty(), mouseX, mouseY);
        }
    }

    public Rectangle computeBoundingBox() {
        return new Rectangle(x, y, width, height);
    }

    public boolean intersects(final GuiComponent other) {
        return this.intersects(other.computeBoundingBox());
    }

    public boolean intersects(final Point point) {
        return this.computeBoundingBox().contains(point);
    }

    public boolean intersects(final Rectangle rectangle) {
        return this.computeBoundingBox().intersects(rectangle);
    }

    public void negotiateDimensions(final double width, final Double height) {
        if (prefSize.isRelative()) {
            this.width = (int) Math.round(Math.min(prefSize.width(), 1.0) * width);
        } else {
            this.width = (int) Math.round(Math.min(prefSize.width(), width));
        }
        if (prefSize.isRelative()) {
            this.height = (int) Math.round(Math.min(prefSize.height(), 1.0) * height);
        } else {
            this.height = (int) Math.round(Math.min(prefSize.height(), height));
        }

        // Reset the inBounds tag and let ancestor nodes check it again
        this.inBounds = true;
    }

    public void invalidate() {
        if (Minecraft.getInstance().screen instanceof BaseScreen baseScreen) {
            baseScreen.invalidate();
        }
    }

    public Position getOffset() {
        return offset;
    }

    public void setOffset(final Position offset) {
        this.offset = offset;
    }

    public Dimensions getPrefSize() {
        return prefSize;
    }

    public void setPrefSize(final Dimensions prefSize) {
        this.prefSize = prefSize;
    }

    public Spacing getMargins() {
        return margins;
    }

    public void setMargins(final Spacing margins) {
        this.margins = margins;
    }

    public Gravity getGravity() {
        return gravity;
    }

    public void setGravity(final Gravity gravity) {
        this.gravity = gravity;
    }

    public List<Component> getHoverTexts() {
        return hoverTexts;
    }

    public void setHoverTexts(final List<Component> hoverTexts) {
        this.hoverTexts = hoverTexts;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(final Color color) {
        this.color = color;
    }

    public int getX() {
        return x;
    }

    public void setX(final int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(final int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(final int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(final int height) {
        this.height = height;
    }

    public boolean isHovered() {
        return isHovered;
    }

    public void setHovered(final boolean hovered) {
        isHovered = hovered;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(final boolean visible) {
        isVisible = visible;
    }

    public boolean isInBounds() {
        return inBounds;
    }

    public void setInBounds(final boolean inBounds) {
        this.inBounds = inBounds;
    }
}
