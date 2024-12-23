package com.dslovikosky.narnia.client.gui.font;

import com.dslovikosky.narnia.common.constants.Constants;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TtfFontLoader {
    // If a font can't render all characters required use this instead
    private static final ResourceLocation CALIBRI_FONT_LOCATION = ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "font/calibri.ttf");

    public static TrueTypeFont createFont(final float size, final boolean antiAlias) {
        final Font font;
        try (final InputStream fontInputStream = Minecraft.getInstance().getResourceManager().getResourceOrThrow(CALIBRI_FONT_LOCATION).open()) {
            font = Font.createFont(Font.TRUETYPE_FONT, fontInputStream).deriveFont(size);
        } catch (IOException | FontFormatException e) {
            throw new RuntimeException(e);
        }

        // Grab the list of characters our font can support
        final Set<Character> alphabet = IntStream.rangeClosed(Character.MIN_VALUE, Character.MAX_VALUE)
                .filter(font::canDisplay)
                .boxed()
                .map(it -> (char) it.byteValue())
                .collect(Collectors.toSet());

        // Return a new true type font without any additional characters
        return new TrueTypeFont(font, antiAlias, alphabet);
    }
}
