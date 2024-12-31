package com.dslovikosky.narnia.common.model.chapter;

import com.dslovikosky.narnia.common.constants.ModChapters;
import com.google.common.base.Suppliers;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.List;
import java.util.function.Supplier;

public final class Book {
    private final ResourceLocation id;
    private final Supplier<List<? extends Chapter>> chapters;

    public Book(final ResourceLocation id) {
        this.id = id;
        this.chapters = Suppliers.memoize(() -> ModChapters.CHAPTERS.getEntries()
                .stream()
                .map(Supplier::get)
                .filter(it -> it.book().get() == this)
                .toList());
    }

    public Component title() {
        return Component.translatable(String.format("book.%s.%s.name", id.getNamespace(), id.getPath()));
    }

    public List<? extends Chapter> getChapters() {
        return chapters.get();
    }

    public ResourceLocation getId() {
        return id;
    }
}
