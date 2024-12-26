package com.dslovikosky.narnia.client.gui.control;

import com.dslovikosky.narnia.client.gui.font.TrueTypeFont;
import com.dslovikosky.narnia.client.gui.layout.TextAlignment;
import net.minecraft.client.gui.GuiGraphics;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class TextBoxComponent extends GuiComponentWithEvents {
    private final List<String> textLines = new ArrayList<>();
    private TrueTypeFont font;
    private String text;
    private String overflowText;
    private Color textColor = Color.WHITE;
    private TextAlignment textAlignment = TextAlignment.ALIGN_CENTER;

    public TextBoxComponent(final TrueTypeFont font, final String text) {
        this.font = font;
        this.text = text;
    }

    @Override
    public void draw(final GuiGraphics guiGraphics) {
        if (this.isVisible() && this.isInBounds()) {
            float yCoord = getY();

            for (final String line : textLines) {
                final int lineHeight = font.getHeight(line);

                final float xCoord = getX() + switch (this.textAlignment) {
                    case TextAlignment.ALIGN_RIGHT -> getWidth();
                    case TextAlignment.ALIGN_CENTER -> getWidth() / 2f;
                    default -> 0f;
                };

                this.font.drawString(guiGraphics, xCoord, yCoord, line, textAlignment, this.textColor);

                yCoord += lineHeight;
            }
        }
    }

    @Override
    public void update() {
        computeTextForSize();
    }

    private void computeTextForSize() {
        textLines.clear();

        if (getWidth() <= 0 && getHeight() <= 0) {
            overflowText = text;
            return;
        }

        if (text.isEmpty()) {
            overflowText = "";
            return;
        }

        StringBuilder currentWord = new StringBuilder();
        String currentLineText = "";
        final List<Integer> lineEndings = new ArrayList<>();
        for (int i = 0; i < text.length(); i++) {
            final char character = text.charAt(i);
            switch (character) {
                case '\t' -> throw new IllegalStateException("Tabs are not supported for now by text boxes. Use 3 spaces instead");
                // Done with the current word AND line
                case '\n' -> {
                    this.textLines.add(currentLineText + currentWord);
                    lineEndings.add(i + 1);
                    currentLineText = "";
                    currentWord = new StringBuilder();
                }
                // Done with the current word
                case ' ' -> {
                    if (this.font.getWidth(currentLineText + currentWord) > getWidth()) {
                        this.textLines.add(currentLineText);
                        lineEndings.add(i - currentWord.length());
                        currentLineText = currentWord + " ";
                        currentWord = new StringBuilder();
                    } else {
                        currentLineText = currentLineText + currentWord + " ";
                        currentWord = new StringBuilder();
                    }
                }
                // Add to the current word
                default -> {
                    currentWord.append(character);
                    if (this.font.getWidth(currentLineText + currentWord) > getWidth()) {
                        if (currentLineText.isEmpty()) {
                            throw new IllegalStateException(String.format("Could not lay out text '%s' in text box because the word was too long and couldn't be split", currentWord.toString()));
                        }
                        this.textLines.add(currentLineText);
                        lineEndings.add(i - currentWord.length() + 1);
                        currentLineText = "";
                    }
                }
            }
        }

        final String remainder = currentLineText + currentWord;
        if (!remainder.isEmpty()) {
            this.textLines.add(remainder);
            lineEndings.add(text.length());
        }

        // Check for overflow. Start by removing lines that don't fit
        while (this.font.getHeight(String.join("\n", this.textLines)) > this.getHeight()) {
            textLines.removeLast();
        }

        this.overflowText = text.substring(lineEndings.get(textLines.size() - 1));
    }

    public String getOverflowText() {
        return overflowText;
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
