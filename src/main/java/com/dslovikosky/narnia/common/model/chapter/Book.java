package com.dslovikosky.narnia.common.model.chapter;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public record Book(ResourceLocation id, List<Chapter> chapters) {
    public Component title() {
        return Component.translatable(String.format("book.%s.%s.name", id.getNamespace(), id.getPath()));
    }

    public Chapter getChapter(final ResourceLocation id) {
        return chapters.stream().filter(chapter -> chapter.id().equals(id)).findFirst().orElse(null);
    }
}
