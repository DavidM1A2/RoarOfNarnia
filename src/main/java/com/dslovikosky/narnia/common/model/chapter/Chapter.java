package com.dslovikosky.narnia.common.model.chapter;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public record Chapter(ResourceLocation id, List<ChapterGoal> goals, List<PlayableActor> actors) {
    public Component title() {
        return Component.translatable(String.format("chapter.%s.the_magicians_nephew.%s.title", id.getNamespace(), id.getPath()));
    }
}
