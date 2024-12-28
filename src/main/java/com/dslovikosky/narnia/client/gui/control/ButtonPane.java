package com.dslovikosky.narnia.client.gui.control;

import com.dslovikosky.narnia.client.gui.event.MouseEvent;
import com.dslovikosky.narnia.client.gui.font.TrueTypeFont;
import com.dslovikosky.narnia.client.gui.layout.Gravity;
import com.dslovikosky.narnia.client.gui.layout.TextAlignment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.sounds.SoundEvents;

import java.awt.Color;
import java.util.function.Consumer;

public class ButtonPane extends GuiPane {
    private final ImagePane icon;
    private final ImagePane iconHovered;
    private final LabelComponent label;

    public ButtonPane(final ImagePane icon, final ImagePane iconHovered, final TrueTypeFont font) {
        this.icon = icon;
        if (icon != null) {
            icon.setGravity(Gravity.CENTER);
            add(icon);
        }

        this.iconHovered = iconHovered;
        if (iconHovered != null) {
            iconHovered.setGravity(Gravity.CENTER);
            add(iconHovered);
        }

        if (font != null) {
            this.label = new LabelComponent(font, "");
            label.setGravity(Gravity.CENTER);
            add(this.label);
        } else {
            this.label = null;
        }

        this.addMouseListener(event -> {
            if (event.getEventType() == MouseEvent.EventType.Click && event.getClickedButton() == MouseEvent.LEFT_MOUSE_BUTTON && this.isVisible() && this.isHovered() && this.isInBounds()) {
                Minecraft.getInstance().player.playSound(SoundEvents.UI_BUTTON_CLICK.value(), 1f, 1f);
                event.consume();
            }
        });
    }

    @Override
    public void draw(final GuiGraphics guiGraphics) {
        if (this.isVisible()) {
            if (iconHovered != null) {
                iconHovered.setVisible(isHovered());
            }
            if (icon != null) {
                icon.setVisible(iconHovered == null || !isHovered());
            }
            super.draw(guiGraphics);
        }
    }

    public void addOnClick(final Consumer<MouseEvent> listener) {
        this.addMouseListener(event -> {
            if (event.getEventType() == MouseEvent.EventType.Click && event.getClickedButton() == MouseEvent.LEFT_MOUSE_BUTTON && this.isVisible() && this.isHovered() && this.isInBounds()) {
                listener.accept(event);
                event.consume();
            }
        });
    }

    public void setText(final String text) {
        if (label != null) {
            label.setText(text);
        }
    }

    public void setTextColor(final Color textColor) {
        if (label != null) {
            label.setTextColor(textColor);
        }
    }

    public void setTextAlignment(final TextAlignment textAlignment) {
        if (label != null) {
            label.setTextAlignment(textAlignment);
        }
    }
}
