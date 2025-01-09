package com.dslovikosky.narnia.common.constants;

import com.dslovikosky.narnia.common.model.scene.Book;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.List;

public class ModBooks {
    public static final DeferredRegister<Book> BOOKS = DeferredRegister.create(ModRegistries.BOOK, Constants.MOD_ID);

    public static final DeferredHolder<Book, ? extends Book> THE_MAGICIANS_NEPHEW = BOOKS.register("the_magicians_nephew", id -> new Book(id, List.of(
            ModChapters.THE_WRONG_DOOR,
            ModChapters.DIGORY_AND_HIS_UNCLE,
            ModChapters.THE_WOOD_BETWEEN_THE_WORLDS)));
}
