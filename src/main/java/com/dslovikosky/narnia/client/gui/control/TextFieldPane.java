package com.dslovikosky.narnia.client.gui.control;

import com.dslovikosky.narnia.client.gui.event.ITextChangeListener;
import com.dslovikosky.narnia.client.gui.event.KeyEvent;
import com.dslovikosky.narnia.client.gui.event.MouseEvent;
import com.dslovikosky.narnia.client.gui.font.TrueTypeFont;
import com.dslovikosky.narnia.client.gui.layout.Dimensions;
import com.dslovikosky.narnia.client.gui.layout.Spacing;
import com.dslovikosky.narnia.client.gui.layout.TextAlignment;
import com.dslovikosky.narnia.common.constants.Constants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringUtil;
import org.lwjgl.glfw.GLFW;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class TextFieldPane extends GuiPane {
    private static final Color GHOST_TEXT_COLOR = new Color(128, 128, 128);
    private static final Color FOCUSED_COLOR_TINT = new Color(230, 230, 230);
    private static final Color BASE_COLOR_TINT = new Color(255, 255, 255);

    private final List<ITextChangeListener> textChangeListeners = new ArrayList<>();
    private final ImagePane background;
    private final StackPane textContainer;
    private final LabelComponent textLabel;

    private boolean isFocused = false;
    private String ghostText = "";
    private Color textColor = Color.WHITE;

    public TextFieldPane(final TrueTypeFont font) {
        background = new ImagePane(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "textures/gui/text_field_background.png"), ImagePane.DisplayMode.STRETCH);

        textContainer = new StackPane();
        textContainer.setPrefSize(new Dimensions(1.0, 1.0, true));
        textContainer.setMargins(new Spacing(0.0, 0.0, 0.1, 0.05, true));
        textContainer.setScissorsEnabled(true);

        textLabel = new LabelComponent(font, "");
        textLabel.setPrefSize(new Dimensions(1.0, 1.0, true));
        textLabel.setShortenToFitDimensions(false);

        textContainer.add(textLabel);
        background.add(textContainer);
        add(background);

        this.addMouseListener(event -> {
            if (event.getEventType() == MouseEvent.EventType.Click) {
                if (event.getClickedButton() == MouseEvent.LEFT_MOUSE_BUTTON) {
                    setFocused(isHovered());
                }
            }
        });
        this.addKeyListener(event -> {
            if (event.getEventType() == KeyEvent.KeyEventType.Type) {
                keyTyped(event);
            } else if (event.getEventType() == KeyEvent.KeyEventType.Press) {
                keyPressed(event);
            }
        });
    }

    @Override
    public void draw(GuiGraphics guiGraphics) {
        if (isVisible()) {
            super.draw(guiGraphics);
        }
    }

    @Override
    public void calcChildrenBounds() {
        super.calcChildrenBounds();
        updateScroll();
    }

    private void updateScroll() {
        final double textWidth = textLabel.getFont().getWidth(textLabel.getText());
        if (textWidth > textLabel.getWidth()) {
            textLabel.setTextAlignment(TextAlignment.ALIGN_RIGHT);
        } else {
            textLabel.setTextAlignment(TextAlignment.ALIGN_LEFT);
        }
    }

    private void keyTyped(final KeyEvent event) {
        if (isFocused) {
            final String character = StringUtil.filterText(Character.toString(event.getCharacter()));
            addText(character);
        }
    }

    private void keyPressed(final KeyEvent event) {
        if (isFocused) {
            if (event.hasModifier(KeyEvent.Modifier.CONTROL) && event.getKey() == GLFW.GLFW_KEY_A) {
                // Select all text, not yet implemented
            } else if (event.hasModifier(KeyEvent.Modifier.CONTROL) && event.getKey() == GLFW.GLFW_KEY_C) {
                Minecraft.getInstance().keyboardHandler.setClipboard(getText());
            } else if (event.hasModifier(KeyEvent.Modifier.CONTROL) && event.getKey() == GLFW.GLFW_KEY_V) {
                setText("");
                addText(StringUtil.filterText(Minecraft.getInstance().keyboardHandler.getClipboard()));
            } else if (event.hasModifier(KeyEvent.Modifier.CONTROL) && event.getKey() == GLFW.GLFW_KEY_X) {
                Minecraft.getInstance().keyboardHandler.setClipboard(getText());
                setText("");
            } else {
                switch (event.getKey()) {
                    case GLFW.GLFW_KEY_BACKSPACE -> removeChars(1);
                    case GLFW.GLFW_KEY_LEFT, GLFW.GLFW_KEY_RIGHT -> { /* Not yet implemented */ }
                }
            }
        }
    }

    public String getText() {
        if (isShowingGhostText()) {
            return "";
        } else {
            if (isFocused) {
                return textLabel.getText().substring(0, textLabel.getText().length() - 1);
            } else {
                return textLabel.getText();
            }
        }
    }

    public void setText(final String text) {
        setTextInternal(text, getText());
    }

    private void setTextInternal(final String rawText, final String oldText) {
        final String newText = StringUtil.filterText(rawText);
        if (isFocused) {
            textLabel.setText(newText + "_");
            textLabel.setTextColor(textColor);
        } else {
            if (newText.isEmpty()) {
                textLabel.setText(ghostText);
                textLabel.setTextColor(GHOST_TEXT_COLOR);
            } else {
                textLabel.setText(newText);
                textLabel.setTextColor(textColor);
            }
        }

        if (!newText.equals(oldText)) {
            textChangeListeners.forEach(listener -> listener.apply(oldText, newText));
        }

        invalidate();
    }

    private void addText(final String text) {
        if (!text.isEmpty()) {
            setText(getText() + text);
        }
    }

    private void removeChars(final int number) {
        final String currentText = getText();
        final int numberClamped = max(0, min(currentText.length(), number));
        setText(currentText.substring(0, currentText.length() - numberClamped));
    }

    public void setGhostText(final String ghostText) {
        final boolean ghostTextWasLoaded = isShowingGhostText();
        this.ghostText = ghostText;
        if (ghostTextWasLoaded) {
            textLabel.setText(ghostText);
        } else if (textLabel.getText().isEmpty()) {
            textLabel.setText(ghostText);
            textLabel.setTextColor(GHOST_TEXT_COLOR);
        }
    }

    public void setTextColor(final Color textColor) {
        this.textColor = textColor;
        if (!isShowingGhostText()) {
            textLabel.setTextColor(textColor);
        }
    }

    private boolean isShowingGhostText() {
        return textLabel.getTextColor() == GHOST_TEXT_COLOR && textLabel.getText().equals(ghostText);
    }

    public void addTextChangeListener(final ITextChangeListener listener) {
        textChangeListeners.add(listener);
    }

    public boolean isFocused() {
        return isFocused;
    }

    private void setFocused(final boolean isFocused) {
        final boolean wasFocused = this.isFocused;
        if (!wasFocused && isFocused) {
            background.setColor(FOCUSED_COLOR_TINT);
            final String currentText = getText();
            this.isFocused = true;
            this.setTextInternal(currentText, currentText);
        } else if (wasFocused && !isFocused) {
            background.setColor(BASE_COLOR_TINT);
            final String currentText = getText();
            this.isFocused = false;
            this.setTextInternal(currentText, currentText);
        }
    }
}
