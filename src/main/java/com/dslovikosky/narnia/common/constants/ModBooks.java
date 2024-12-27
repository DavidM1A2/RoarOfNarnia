package com.dslovikosky.narnia.common.constants;

import com.dslovikosky.narnia.common.model.chapter.Book;
import com.dslovikosky.narnia.common.model.chapter.Chapter;
import com.dslovikosky.narnia.common.model.chapter.goal.FindUncleAndrewsHouseGoal;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public class ModBooks {
    public static final Book THE_MAGICIANS_NEPHEW = new Book(
            ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "the_magicians_nephew"),
            List.of(
                    new Chapter(
                            ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "the_wrong_door"),
                            List.of(new FindUncleAndrewsHouseGoal()),
                            List.of(ModActors.DIGORY, ModActors.POLLY)),
                    new Chapter(
                            ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "digory_and_his_uncle"),
                            List.of(),
                            List.of(ModActors.DIGORY, ModActors.POLLY)),
                    new Chapter(
                            ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "the_wood_between_the_worlds"),
                            List.of(),
                            List.of(ModActors.DIGORY, ModActors.POLLY))
            ));

    public static final List<Book> BOOKS = List.of(THE_MAGICIANS_NEPHEW);

    public static Book getBook(final ResourceLocation id) {
        return BOOKS.stream().filter(it -> id.equals(it.id())).findFirst().orElse(null);
    }

    public static Chapter getChapter(final Book book, final ResourceLocation id) {
        return book.chapters().stream().filter(it -> id.equals(it.id())).findFirst().orElse(null);
    }
}
