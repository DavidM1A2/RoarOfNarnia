package com.dslovikosky.narnia.common.model.chapter;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Supplier;

public record Chapter(ResourceLocation id, Supplier<? extends Book> book, Supplier<List<? extends Character>> characters, List<ChapterGoal> goals) {
    public Component title() {
        return Component.translatable(String.format("chapter.%s.%s.%s.title", id.getNamespace(), book.get().getId().getPath(), id.getPath()));
    }

    public void start(final Scene scene) {

    }

    public void stop(final Scene scene) {

    }

    public void tick(final Scene scene) {

    }

    public void tryJoin(final Scene scene, final Player player, @Nullable final Character character) {
        if (character == null) {
            scene.getSpectatingPlayerIds().add(player.getUUID());
        } else {
            scene.getCharacterInstances().put(character, new Actor(character, player));
        }
    }

    public void tryLeave(final Scene scene, final Player player) {

    }

    public boolean isParticipatingIn(final Scene scene, final Player player) {
        return true;
    }
}

