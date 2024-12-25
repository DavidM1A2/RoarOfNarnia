package com.dslovikosky.narnia.client.gui.font;

import com.dslovikosky.narnia.client.gui.layout.TextAlignment;
import com.mojang.blaze3d.platform.TextureUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferInt;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TrueTypeFont {
    private static final float TEXT_SCALE_FACTOR = 0.2f;
    private static final Set<Integer> VALID_TEXTURE_SIZES = IntStream.rangeClosed(6, 12).map(it -> (int) Math.pow(2.0, it)).boxed().collect(Collectors.toSet());
    private final Map<Character, CharacterGlyph> glyphs = new HashMap<>();
    private final Font font;
    private final boolean antiAlias;
    private final FontMetrics fontMetrics;
    private final int fontTextureId;
    private final int textureWidth;
    private final int textureHeight;
    private int charHeight;

    public TrueTypeFont(final Font font, final boolean antiAlias, final Set<Character> alphabet) {
        this.font = font;
        this.antiAlias = antiAlias;

        this.fontMetrics = computeFontMetrics();

        // A multiple of 2 for the opengl texture (ex. 256, 512, or 1024)
        final int textureSize = getTextureSize(alphabet);
        this.textureWidth = textureSize;
        this.textureHeight = textureSize;

        // Render the characters into open GL format
        this.fontTextureId = createTextureSheet(alphabet);
    }

    private FontMetrics computeFontMetrics() {
        // To get a graphics object we need a buffered image...
        final Graphics2D graphics = (Graphics2D) new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB).getGraphics();
        if (antiAlias) {
            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        }
        graphics.setFont(font);
        return graphics.getFontMetrics();
    }

    private int getTextureSize(final Set<Character> alphabet) {
        // Get the maximum possible height of each character
        final int maxCharHeight = fontMetrics.getHeight();

        // Use a list of characters so it is indexable
        final List<Character> indexableAlphabet = new ArrayList<>(alphabet);

        // Find the closest valid texture size that will hold all the glyphs up to 4096x4096
        for (final int possibleTextureSize : VALID_TEXTURE_SIZES) {
            int rowsRemaining = possibleTextureSize / maxCharHeight;
            double currentRowLength = 0.0;
            int currentCharIndex = 0;

            // Go row by row and see if all the glyphs will fit
            while (rowsRemaining > 0) {
                // If no glyphs are left, we're done
                if (currentCharIndex >= indexableAlphabet.size()) {
                    return possibleTextureSize;
                }

                // Get the glyph, see if it fits in this row. If not, move on to the next row
                final int currentCharWidth = fontMetrics.charWidth(indexableAlphabet.get(currentCharIndex));
                if (currentRowLength + currentCharWidth > possibleTextureSize) {
                    currentRowLength = 0.0;
                    rowsRemaining--;
                } else {
                    currentRowLength += currentCharWidth;
                    currentCharIndex++;
                }
            }
        }

        throw new IllegalArgumentException(String.format("Texture width/height could not be created as it would be larger than %s", VALID_TEXTURE_SIZES.stream().max(Integer::compareTo).get()));
    }

    private int createTextureSheet(final Set<Character> alphabet) {
        // Create a temp buffered image to write to
        final BufferedImage imgTemp = new BufferedImage(textureWidth, textureHeight, BufferedImage.TYPE_INT_ARGB);
        // Grab the graphics object to write to the image
        final Graphics2D graphics = (Graphics2D) imgTemp.getGraphics();

        // Set the color to blank
        graphics.setColor(new Color(0, 0, 0, 0));
        // Fill the rectangle with black
        graphics.fillRect(0, 0, textureWidth, textureHeight);

        // Set the anti-alias flag if needed
        if (antiAlias) {
            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        }
        // Set the font
        graphics.setFont(font);
        // Set the color to white
        graphics.setColor(Color.WHITE);

        // The current glyph pos x and y
        int positionX = 0;
        int positionY = 0;

        // Compute the height of the character
        this.charHeight = fontMetrics.getHeight();

        // Go over each character
        for (final Character character : alphabet) {
            // Compute the width of the current character
            final int charWidth = Math.max(1, fontMetrics.charWidth(character));

            // If the glyph is too big for the texture move down a line
            if (positionX + charWidth >= textureWidth) {
                // Reset X to the far left
                positionX = 0;
                // Move Y down a row
                positionY += this.charHeight;
            }

            // Assign the glyph position on the texture
            // Create a new character glyph for this font
            final CharacterGlyph characterGlyph = new CharacterGlyph(charWidth, charHeight, positionX, positionY);

            // Draw the character glyph to the large texture
            graphics.drawString(character.toString(), positionX, positionY + fontMetrics.getAscent());

            // Move the X position over by the glyph's width
            positionX += characterGlyph.width;

            glyphs.put(character, characterGlyph);
        }

        try {
            ImageIO.write(imgTemp, "png", new File("./out.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Once we're done writing all of our glyphs onto the 1024x1024 image we load it into open GL for rendering
        return loadImage(imgTemp);
    }

    private int loadImage(final BufferedImage bufferedImage) {
        // Grab the width and height of the texture
        final int width = bufferedImage.getWidth();
        final int height = bufferedImage.getHeight();

        // Get the number of bytes per pixel
        final int bitsPerPixel = bufferedImage.getColorModel().getPixelSize();
        final int bytesPerPixel = bitsPerPixel / 8;
        // Grab the data buffer used by the buffered image
        final DataBuffer dataBuffer = bufferedImage.getData().getDataBuffer();
        final ByteBuffer byteBuffer;
        if (dataBuffer instanceof DataBufferInt dataBufferInt) {
            // If it's an int buffer write each int to the data buffer 4 bytes at a time
            byteBuffer = ByteBuffer.allocateDirect(width * height * bytesPerPixel).order(ByteOrder.nativeOrder());
            Arrays.stream(dataBufferInt.getData()).forEach(byteBuffer::putInt);
        } else {
            // If it's a byte buffer write it directly into the buffer
            byteBuffer = ByteBuffer.allocateDirect(width * height * bytesPerPixel).order(ByteOrder.nativeOrder());
            byteBuffer.put(((DataBufferByte) dataBuffer).getData());
        }
        // We need to flip the bytes so they get drawn correctly
        byteBuffer.flip();

        // Not very familiar with OpenGl here, but create an int buffer and generate the texture from the byte buffer
        final int textureId = TextureUtil.generateTextureId();
        RenderSystem.bindTextureForSetup(textureId);

        RenderSystem.texParameter(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_CLAMP);
        RenderSystem.texParameter(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_CLAMP);

        RenderSystem.texParameter(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
        RenderSystem.texParameter(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);

        RenderSystem.pixelStore(GL11.GL_UNPACK_ROW_LENGTH, 0);
        RenderSystem.pixelStore(GL11.GL_UNPACK_SKIP_ROWS, 0);
        RenderSystem.pixelStore(GL11.GL_UNPACK_SKIP_PIXELS, 0);
        RenderSystem.pixelStore(GL11.GL_UNPACK_ALIGNMENT, bytesPerPixel);

        GL11.glTexImage2D(
                GL11.GL_TEXTURE_2D,
                0,
                GL11.GL_RGBA8,
                width,
                height,
                0,
                GL11.GL_RGBA,
                GL11.GL_UNSIGNED_BYTE,
                byteBuffer
        );

        // Return the texture ID
        return textureId;
    }

    public void drawString(final GuiGraphics guiGraphics, final float x, final float y, final String stringToDraw, final TextAlignment textAlignment, final Color rgba) {
        // The current glyph being drawn
        CharacterGlyph characterGlyph;
        int drawX = 0;
        int drawY = 0;

        final PoseStack poseStack = guiGraphics.pose();
        poseStack.pushPose();
        RenderSystem.enableBlend();
        RenderSystem.setShader(GameRenderer::getPositionTexShader);

        final float red = rgba.getRed() / 255f;
        final float green = rgba.getGreen() / 255f;
        final float blue = rgba.getBlue() / 255f;
        final float alpha = rgba.getAlpha() / 255f;

        RenderSystem.setShaderColor(red, green, blue, alpha);

        // Bind our custom texture sheet
        RenderSystem.setShaderTexture(0, fontTextureId);

        final Tesselator tessellator = Tesselator.getInstance();
        for (final String line : stringToDraw.split("\n")) {
            // Set start position
            drawX = switch (textAlignment) {
                case TextAlignment.ALIGN_CENTER -> -(this.fontMetrics.stringWidth(line) / 2);
                case TextAlignment.ALIGN_LEFT -> 0;
                case TextAlignment.ALIGN_RIGHT -> -this.fontMetrics.stringWidth(line);
            };

            // Draw each character
            for (final char currentChar : line.toCharArray()) {
                // Grab the glyph to draw, it will either be ascii or in the additional glyphs map
                characterGlyph = Optional.ofNullable(glyphs.get(currentChar)).orElse(glyphs.get('a'));
                // Draw a letter
                drawQuad(
                        poseStack,
                        tessellator,
                        drawX * TEXT_SCALE_FACTOR + x,
                        drawY * TEXT_SCALE_FACTOR + y,
                        (drawX + characterGlyph.width) * TEXT_SCALE_FACTOR + x,
                        (drawY + characterGlyph.height) * TEXT_SCALE_FACTOR + y,
                        characterGlyph.storedX,
                        characterGlyph.storedY,
                        characterGlyph.storedX + characterGlyph.width,
                        characterGlyph.storedY + characterGlyph.height
                );
                drawX += characterGlyph.width;
            }
            // On newline
            drawY += charHeight;
        }
        poseStack.popPose();
    }

    /**
     * Draws a glyph using a quad on the screen
     */
    private void drawQuad(
            final PoseStack poseStack,
            final Tesselator tessellator,
            final float drawX, final float drawY,
            final float drawX2, final float drawY2,
            final float srcX, final float srcY,
            final float srcX2, final float srcY2) {
        final Matrix4f pose = poseStack.last().pose();
        final BufferBuilder bufferBuilder = tessellator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);

        // Compute the width and height of the glyph to draw
        final float drawWidth = Math.abs(drawX2 - drawX);
        final float drawHeight = Math.abs(drawY2 - drawY);
        // Compute the width and height of the glyph on the source
        final float srcWidth = Math.abs(srcX2 - srcX);
        final float srcHeight = Math.abs(srcY2 - srcY);

        // Add the 4 vertices that are used to draw the glyph. These must be done in this order
        bufferBuilder.addVertex(pose, drawX, drawY + drawHeight, 0f)
                .setUv(srcX / textureWidth, (srcY + srcHeight) / textureHeight);
        bufferBuilder.addVertex(pose, drawX + drawWidth, drawY + drawHeight, 0f)
                .setUv((srcX + srcWidth) / textureWidth, (srcY + srcHeight) / textureHeight);
        bufferBuilder.addVertex(pose, drawX + drawWidth, drawY, 0f)
                .setUv((srcX + srcWidth) / textureWidth, srcY / textureHeight);
        bufferBuilder.addVertex(pose, drawX, drawY, 0f)
                .setUv(srcX / textureWidth, srcY / textureHeight);

        BufferUploader.drawWithShader(bufferBuilder.buildOrThrow());
    }

    public int getWidth(final String string) {
        return Math.round(this.fontMetrics.stringWidth(string) * TEXT_SCALE_FACTOR);
    }

    public int getHeight(final String string) {
        return Math.round(this.fontMetrics.getHeight() * TEXT_SCALE_FACTOR * (string.chars().filter(it -> it == '\n').count() + 1));
    }

    public void destroy() {
        TextureUtil.releaseTextureId(fontTextureId);
    }

    private record CharacterGlyph(int width, int height, int storedX, int storedY) {
    }
}
