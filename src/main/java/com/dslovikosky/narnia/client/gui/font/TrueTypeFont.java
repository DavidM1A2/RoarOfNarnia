package com.dslovikosky.narnia.client.gui.font;

import com.dslovikosky.narnia.client.gui.layout.TextAlignment;
import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.AddressMode;
import com.mojang.blaze3d.textures.FilterMode;
import com.mojang.blaze3d.textures.GpuTexture;
import com.mojang.blaze3d.textures.GpuTextureView;
import com.mojang.blaze3d.textures.TextureFormat;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.MeshData;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderPipelines;
import org.joml.Matrix3x2f;

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
import java.util.OptionalInt;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TrueTypeFont {
    private static final float TEXT_SCALE_FACTOR = 0.2f;
    private static final Set<Integer> VALID_TEXTURE_SIZES = IntStream.rangeClosed(6, 12).map(it -> (int) Math.pow(2.0, it)).boxed().collect(Collectors.toSet());
    private final GpuTexture texture;
    private final GpuTextureView textureView;
    private final Map<Character, CharacterGlyph> glyphs = new HashMap<>();
    private final Font font;
    private final boolean antiAlias;
    private final FontMetrics fontMetrics;
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
        this.texture = createTextureSheet(alphabet);
        this.textureView = RenderSystem.getDevice().createTextureView(texture);
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

    private GpuTexture createTextureSheet(final Set<Character> alphabet) {
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

    private GpuTexture loadImage(final BufferedImage bufferedImage) {
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

        final GpuTexture texture = RenderSystem.getDevice().createTexture("TrueTypeFont", GpuTexture.USAGE_COPY_DST, TextureFormat.RGBA8, width, height, 1, 1);
        texture.setAddressMode(AddressMode.CLAMP_TO_EDGE, AddressMode.CLAMP_TO_EDGE);
        texture.setTextureFilter(FilterMode.NEAREST, FilterMode.NEAREST, false);

        RenderSystem.getDevice().createCommandEncoder().writeToTexture(texture, byteBuffer.asIntBuffer(), NativeImage.Format.RGBA, 0, 0, 0, 0, width, height);

        return texture;
    }

    public void drawString(final GuiGraphics guiGraphics, float x, float y, String stringToDraw, TextAlignment textAlignment, Color rgba) {
        final Matrix3x2f pose = guiGraphics.pose();
        try (final RenderPass pass = RenderSystem.getDevice().createCommandEncoder().createRenderPass(texture::getLabel, textureView, OptionalInt.of(0xFFFFFFFF))) {
            // Build one big buffer for the entire string
            BufferBuilder builder = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);

            int drawY = 0;

            for (String line : stringToDraw.split("\n")) {
                // Start X depending on alignment
                int drawX = switch (textAlignment) {
                    case ALIGN_CENTER -> -(this.fontMetrics.stringWidth(line) / 2);
                    case ALIGN_LEFT -> 0;
                    case ALIGN_RIGHT -> -this.fontMetrics.stringWidth(line);
                };

                // Add each glyph as a quad
                for (char currentChar : line.toCharArray()) {
                    final CharacterGlyph glyph = Optional.ofNullable(glyphs.get(currentChar)).orElse(glyphs.get('a'));

                    float gx1 = drawX * TEXT_SCALE_FACTOR + x;
                    float gy1 = drawY * TEXT_SCALE_FACTOR + y;
                    float gx2 = (drawX + glyph.width) * TEXT_SCALE_FACTOR + x;
                    float gy2 = (drawY + glyph.height) * TEXT_SCALE_FACTOR + y;

                    float u1 = glyph.storedX / (float) textureWidth;
                    float v1 = glyph.storedY / (float) textureHeight;
                    float u2 = (glyph.storedX + glyph.width) / (float) textureWidth;
                    float v2 = (glyph.storedY + glyph.height) / (float) textureHeight;

                    builder.addVertexWith2DPose(pose, gx1, gy2, 0f).setUv(u1, v2);
                    builder.addVertexWith2DPose(pose, gx2, gy2, 0f).setUv(u2, v2);
                    builder.addVertexWith2DPose(pose, gx2, gy1, 0f).setUv(u2, v1);
                    builder.addVertexWith2DPose(pose, gx1, gy1, 0f).setUv(u1, v1);

                    drawX += glyph.width;
                }

                drawY += charHeight;
            }

            // Build once at the end
            final MeshData mesh = builder.buildOrThrow();
            int indexCount = mesh.indexBuffer().remaining() / Integer.BYTES;

            // Upload into GPU buffers
            try (GpuBuffer vertexBuffer = RenderSystem.getDevice().createBuffer(texture::getLabel, GpuBuffer.USAGE_VERTEX, mesh.vertexBuffer());
                 GpuBuffer indexBuffer = RenderSystem.getDevice().createBuffer(texture::getLabel, GpuBuffer.USAGE_INDEX, mesh.indexBuffer())) {

                pass.setPipeline(RenderPipelines.GUI_TEXTURED);
                pass.setVertexBuffer(0, vertexBuffer);
                pass.setIndexBuffer(indexBuffer, VertexFormat.IndexType.INT); // or SHORT

                pass.draw(0, indexCount);
            }
        }
    }

    public int getWidth(final String string) {
        return Math.round(this.fontMetrics.stringWidth(string) * TEXT_SCALE_FACTOR);
    }

    public int getHeight(final String string) {
        return Math.round(this.fontMetrics.getHeight() * TEXT_SCALE_FACTOR * (string.chars().filter(it -> it == '\n').count() + 1));
    }

    private record CharacterGlyph(int width, int height, int storedX, int storedY) {
    }
}
