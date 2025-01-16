package com.dslovikosky.narnia.common.constants;

import com.dslovikosky.narnia.common.book.TheMagiciansNephewBook;
import com.dslovikosky.narnia.common.model.scene.Book;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModBooks {
    public static final DeferredRegister<Book> BOOKS = DeferredRegister.create(ModRegistries.BOOK, Constants.MOD_ID);

    public static final DeferredHolder<Book, TheMagiciansNephewBook> THE_MAGICIANS_NEPHEW = BOOKS.register("the_magicians_nephew", TheMagiciansNephewBook::new);
}
