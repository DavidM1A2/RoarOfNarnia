package com.dslovikosky.narnia.common.chapter;

import com.dslovikosky.narnia.common.constants.ModBooks;
import com.dslovikosky.narnia.common.constants.ModCharacters;
import com.dslovikosky.narnia.common.model.scene.Chapter;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public class TheWoodBetweenTheWorldsChapter extends Chapter {
    public TheWoodBetweenTheWorldsChapter(final ResourceLocation id) {
        super(id, ModBooks.THE_MAGICIANS_NEPHEW, List.of(ModCharacters.DIGORY, ModCharacters.POLLY, ModCharacters.UNCLE_ANDREW), List.of());
    }
}
