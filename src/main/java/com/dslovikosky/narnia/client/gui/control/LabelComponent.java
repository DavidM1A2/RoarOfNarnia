package com.dslovikosky.narnia.client.gui.control;

import com.dslovikosky.narnia.client.gui.font.TrueTypeFont;
import com.dslovikosky.narnia.client.gui.layout.TextAlignment;
import net.minecraft.client.gui.GuiGraphics;

import java.awt.*;

public class LabelComponent extends GuiComponentWithEvents {
    private String text = "";
    private String fitText = "";
    private Color textColor = Color.WHITE;
    private TextAlignment textAlignment = TextAlignment.ALIGN_CENTER;
    private TrueTypeFont font;

    public LabelComponent(final TrueTypeFont font, final String text) {
        this.font = font;
        this.text = text;
        this.fitText = text;
    }

    @Override
    public void draw(final GuiGraphics guiGraphics) {
        // If the label is visible, draw it
        if (this.isVisible() && this.isInBounds()) {
            // Compute the x and y positions of the text
            final float xCoord = getX() + switch (this.textAlignment) {
                case TextAlignment.ALIGN_RIGHT -> this.getWidth();
                case TextAlignment.ALIGN_CENTER -> this.getWidth() / 2f;
                default -> 0f;
            };
            float yCoord = getY();

            // Center align text on the y-axis
            final float spaceLeft = this.getHeight() - this.font.getHeight(this.fitText);
            if (spaceLeft > 0) {
                yCoord += (spaceLeft / 2);
            }

            // Draw the string at (x, y) with the correct color and scale
            this.font.drawString(guiGraphics, xCoord, yCoord, fitText, textAlignment, textColor);
        }
    }

    @Override
    public void update() {
    }

    @Override
    public void negotiateDimensions(final double width, final Double height) {
        super.negotiateDimensions(width, height);
        computeTextForSize();
    }

    private void computeTextForSize() {
        // Test if the text will fit into our label based on height
        if (this.font.getHeight(this.text) > this.getHeight()) {
            this.fitText = "";
        } else {
            // If the height is OK shorten the text until it fits into the label
            this.fitText = this.text;
            // Grab the current width of the text
            var width = this.font.getWidth(this.fitText);
            // If it's too big remove one character at a time until it isn't
            while (width > this.getWidth() && this.fitText.length() >= 2) {
                this.fitText = this.fitText.substring(0, this.fitText.length() - 2);
                // Grab the current width of the text
                width = this.font.getWidth(this.fitText);
            }
        }
    }

    public String getText() {
        return text;
    }

    public void setText(final String text) {
        this.text = text;
        computeTextForSize();
    }

    public Color getTextColor() {
        return textColor;
    }

    public void setTextColor(final Color textColor) {
        this.textColor = textColor;
    }

    public TextAlignment getTextAlignment() {
        return textAlignment;
    }

    public void setTextAlignment(final TextAlignment textAlignment) {
        this.textAlignment = textAlignment;
        computeTextForSize();
    }

    public TrueTypeFont getFont() {
        return font;
    }

    public void setFont(final TrueTypeFont font) {
        this.font = font;
        computeTextForSize();
    }
}
