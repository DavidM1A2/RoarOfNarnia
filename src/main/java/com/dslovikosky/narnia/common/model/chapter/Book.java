package com.dslovikosky.narnia.common.model.chapter;

import com.google.common.base.Suppliers;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.List;
import java.util.function.Supplier;

public class Book {
    private final ResourceLocation id;
    private final Supplier<List<Chapter>> chapters;

    public Book(final ResourceLocation id, final List<DeferredHolder<Chapter, ? extends Chapter>> chapters) {
        this.id = id;
        this.chapters = Suppliers.memoize(() -> chapters.stream().map(DeferredHolder::get).map(Chapter.class::cast).toList());
    }

    public Component title() {
        return Component.translatable(String.format("book.%s.%s.name", id.getNamespace(), id.getPath()));
    }

    public List<Chapter> getChapters() {
        return chapters.get();
    }

    public ResourceLocation getId() {
        return id;
    }
}
