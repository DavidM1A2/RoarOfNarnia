package com.dslovikosky.narnia.common.book;

import com.dslovikosky.narnia.common.constants.ModChapters;
import com.dslovikosky.narnia.common.model.scene.Book;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public class TheMagiciansNephewBook extends Book {
    public TheMagiciansNephewBook(final ResourceLocation id) {
        super(id, List.of(ModChapters.THE_WRONG_DOOR, ModChapters.DIGORY_AND_HIS_UNCLE, ModChapters.THE_WOOD_BETWEEN_THE_WORLDS));
    }
}
