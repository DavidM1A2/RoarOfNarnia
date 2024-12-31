package com.dslovikosky.narnia.common.constants;

import com.dslovikosky.narnia.common.model.chapter.Book;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModBooks {
    public static final DeferredRegister<Book> BOOKS = DeferredRegister.create(ModRegistries.BOOK, Constants.MOD_ID);

    public static final DeferredHolder<Book, ? extends Book> THE_MAGICIANS_NEPHEW = BOOKS.register("the_magicians_nephew", Book::new);
}
