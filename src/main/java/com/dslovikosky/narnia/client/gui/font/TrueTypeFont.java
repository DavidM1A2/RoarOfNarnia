package com.dslovikosky.narnia.client.gui.font;

import com.dslovikosky.narnia.client.gui.layout.TextAlignment;
import com.dslovikosky.narnia.common.constants.Constants;
import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ARGB;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TrueTypeFont {
    private static final float TEXT_SCALE_FACTOR = 0.2f;
    // Add 1 pixel padding around each glyph
    private static final int PADDING = 1;
    private static final Set<Integer> VALID_TEXTURE_SIZES = IntStream.rangeClosed(6, 12)
            .map(i -> (int) Math.pow(2, i)).boxed().collect(Collectors.toSet());

    private final Font font;
    private final boolean antiAlias;
    private final FontMetrics fontMetrics;
    private final int textureWidth;
    private final int textureHeight;
    private final int charHeight;

    private final Map<Character, CharacterGlyph> glyphs = new HashMap<>();
    private final ResourceLocation atlasLocation;

    public TrueTypeFont(Font font, boolean antiAlias, Set<Character> alphabet) {
        this.font = font;
        this.antiAlias = antiAlias;

        // Compute font metrics
        final BufferedImage temp = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        final Graphics2D g = temp.createGraphics();
        if (antiAlias) g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setFont(font);
        this.fontMetrics = g.getFontMetrics();
        this.charHeight = fontMetrics.getHeight();

        // Compute texture size
        this.textureWidth = this.textureHeight = computeTextureSize(alphabet);

        // Bake atlas
        final BufferedImage atlas = bakeFontAtlas(alphabet);

        // Convert to NativeImage and register
        final NativeImage nativeImage = bufferedImageToNativeImage(atlas);
        this.atlasLocation = Constants.modLocation("textures/gui/font_atlas_" + font.getSize() + ".png");
        final DynamicTexture texture = new DynamicTexture(atlasLocation::toString, nativeImage);
        Minecraft.getInstance().getTextureManager().register(atlasLocation, texture);
    }

    private int computeTextureSize(Set<Character> alphabet) {
        final int maxHeight = fontMetrics.getHeight() + PADDING * 2;
        final List<Character> chars = new ArrayList<>(alphabet);
        for (int size : VALID_TEXTURE_SIZES) {
            int rows = size / maxHeight;
            int currentChar = 0;
            double rowLength = 0;
            while (rows > 0) {
                if (currentChar >= chars.size()) return size;
                final int charWidth = Math.max(1, fontMetrics.charWidth(chars.get(currentChar))) + PADDING * 2;
                if (rowLength + charWidth > size) {
                    rowLength = 0;
                    rows--;
                } else {
                    rowLength += charWidth;
                    currentChar++;
                }
            }
        }
        throw new IllegalArgumentException("Font atlas too big");
    }

    private BufferedImage bakeFontAtlas(Set<Character> alphabet) {
        final BufferedImage img = new BufferedImage(textureWidth, textureHeight, BufferedImage.TYPE_INT_ARGB);
        final Graphics2D g = img.createGraphics();
        g.setColor(new Color(0, 0, 0, 0));
        g.fillRect(0, 0, textureWidth, textureHeight);
        if (antiAlias) {
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        }
        g.setFont(font);
        g.setColor(Color.WHITE);

        int x = PADDING;
        int y = PADDING;

        for (char c : alphabet) {
            final int w = Math.max(1, fontMetrics.charWidth(c));
            final int h = charHeight;

            if (x + w + PADDING > textureWidth) {
                x = PADDING;
                y += h + PADDING;
            }

            g.drawString(String.valueOf(c), x, y + fontMetrics.getAscent());
            glyphs.put(c, new CharacterGlyph(w, h, x, y));

            x += w + PADDING;
        }
        return img;
    }

    private NativeImage bufferedImageToNativeImage(BufferedImage img) {
        int w = img.getWidth(), h = img.getHeight();
        NativeImage nativeImage = new NativeImage(w, h, true);
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                nativeImage.setPixelABGR(x, y, img.getRGB(x, y));
            }
        }
        return nativeImage;
    }

    public void drawString(GuiGraphics guiGraphics, float x, float y, String text, TextAlignment alignment, Color color) {
        int drawY = Math.round(y);

        for (String line : text.split("\n")) {
            int lineWidth = Math.round(line.chars().map(fontMetrics::charWidth).sum() * TEXT_SCALE_FACTOR);
            int drawX = Math.round(x);
            drawX += switch (alignment) {
                case ALIGN_CENTER -> -lineWidth / 2;
                case ALIGN_LEFT -> 0;
                case ALIGN_RIGHT -> -lineWidth;
            };

            for (char character : line.toCharArray()) {
                CharacterGlyph glyph = glyphs.getOrDefault(character, glyphs.get('a'));

                int width = Math.round(glyph.width * TEXT_SCALE_FACTOR);
                int height = Math.round(glyph.height * TEXT_SCALE_FACTOR);

                guiGraphics.blit(
                        RenderPipelines.GUI_TEXTURED,
                        atlasLocation,
                        drawX, drawY,
                        glyph.storedX, glyph.storedY,
                        width, height,
                        glyph.width, glyph.height,
                        textureWidth, textureHeight,
                        ARGB.color(color.getAlpha(), color.getRed(), color.getGreen(), color.getBlue())
                );

                drawX += width;
            }
            drawY += Math.round(charHeight * TEXT_SCALE_FACTOR);
        }
    }

    public int getWidth(String text) {
        int maxWidth = 0;
        for (String line : text.split("\n")) {
            int lineWidth = line.chars()
                    .map(c -> glyphs.getOrDefault((char) c, glyphs.get('a')).width)
                    .sum();
            maxWidth = Math.max(maxWidth, lineWidth);
        }
        return Math.round(maxWidth * TEXT_SCALE_FACTOR);
    }

    public int getHeight(String text) {
        return Math.round(charHeight * (text.split("\n").length) * TEXT_SCALE_FACTOR);
    }

    public int getBaselineOffset() {
        return Math.round(fontMetrics.getAscent() * TEXT_SCALE_FACTOR);
    }

    private record CharacterGlyph(int width, int height, int storedX, int storedY) {
    }
}
