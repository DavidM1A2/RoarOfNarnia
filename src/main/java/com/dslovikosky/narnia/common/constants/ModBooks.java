package com.dslovikosky.narnia.common.constants;

import com.dslovikosky.narnia.common.model.chapter.Book;
import com.dslovikosky.narnia.common.model.chapter.Chapter;
import com.dslovikosky.narnia.common.model.chapter.goal.FindUncleAndrewsHouseGoal;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.List;

public class ModBooks {
    public static final DeferredRegister<Book> BOOKS = DeferredRegister.create(ModRegistries.BOOK, Constants.MOD_ID);

    public static final DeferredHolder<Book, ? extends Book> THE_MAGICIANS_NEPHEW = BOOKS.register("the_magicians_nephew",
            id -> new Book(id, List.of(
                    new Chapter(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "the_wrong_door"), List.of(new FindUncleAndrewsHouseGoal()), List.of(ModCharacters.DIGORY.value(), ModCharacters.POLLY.get(), ModCharacters.UNCLE_ANDREW.get())),
                    new Chapter(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "digory_and_his_uncle"), List.of(), List.of(ModCharacters.DIGORY.get(), ModCharacters.UNCLE_ANDREW.get())),
                    new Chapter(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "the_wood_between_the_worlds"), List.of(), List.of(ModCharacters.DIGORY.get(), ModCharacters.POLLY.get(), ModCharacters.UNCLE_ANDREW.get())))));
}
