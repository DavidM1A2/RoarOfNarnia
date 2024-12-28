package com.dslovikosky.narnia.client.gui.font;

import com.dslovikosky.narnia.common.constants.Constants;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TtfFontLoader {
    private static final Map<TtfFontId, TrueTypeFont> FONT_CACHE = new HashMap<>();
    private static final ResourceLocation CALIBRI_FONT = ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "font/calibri.ttf");
    private static final ResourceLocation NARNIA_FONT = ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "font/narnia_bll.ttf");

    private static final Set<Character> NARNIA_CHARACTER_SET = " abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".chars().boxed().map(it -> (char) it.byteValue()).collect(Collectors.toSet());

    public static TrueTypeFont getNarniaFont(final float size) {
        return TtfFontLoader.createFont(NARNIA_FONT, size, true, NARNIA_CHARACTER_SET);
    }

    public static TrueTypeFont getTextFont(final float size, final boolean antiAlias) {
        // Grab the list of characters our font can support
        final Set<Character> alphabet = IntStream.rangeClosed(Character.MIN_VALUE, Character.MAX_VALUE)
                .boxed()
                .map(it -> (char) it.byteValue())
                .collect(Collectors.toSet());

        // Return a new true type font without any additional characters
        return createFont(CALIBRI_FONT, size, antiAlias, alphabet);
    }

    private static TrueTypeFont createFont(final ResourceLocation fontLocation, final float size, final boolean antiAlias, final Set<Character> alphabet) {
        return FONT_CACHE.computeIfAbsent(new TtfFontId(fontLocation, size, antiAlias, alphabet), id -> {
            final Font font;
            try (final InputStream fontInputStream = Minecraft.getInstance().getResourceManager().getResourceOrThrow(fontLocation).open()) {
                font = Font.createFont(Font.TRUETYPE_FONT, fontInputStream).deriveFont(size);
            } catch (IOException | FontFormatException e) {
                throw new RuntimeException(e);
            }

            // Return a new true type font without any additional characters
            return new TrueTypeFont(font, antiAlias, alphabet.stream().filter(font::canDisplay).collect(Collectors.toSet()));
        });
    }

    private record TtfFontId(ResourceLocation fontLocation, float size, boolean antiAlias, Set<Character> alphabet) {
    }
}
