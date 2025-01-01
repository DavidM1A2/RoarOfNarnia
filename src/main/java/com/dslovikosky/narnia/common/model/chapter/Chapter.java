package com.dslovikosky.narnia.common.model.chapter;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public record Chapter(ResourceLocation id, Supplier<? extends Book> book, Supplier<List<? extends Character>> characters, List<ChapterGoal> goals) {
    public Component title() {
        return Component.translatable(String.format("chapter.%s.%s.%s.title", id.getNamespace(), book.get().getId().getPath(), id.getPath()));
    }

    public boolean isComplete(final Scene scene) {
        return scene.getGoalIndex() >= goals.size();
    }

    public void start(final Scene scene, final Level level) {
    }

    public void stop(final Scene scene, final Level level, final boolean isComplete) {
    }

    public boolean tick(final Scene scene, final Level level) {
        final Optional<ChapterGoal> currentGoalOpt = getCurrentGoal(scene);
        if (currentGoalOpt.isEmpty()) {
            return true;
        }

        final ChapterGoal currentGoal = currentGoalOpt.get();
        final GoalTickResult result = currentGoal.tick(scene, level);
        if (result == GoalTickResult.COMPLETED) {
            currentGoal.finish(scene, level);
            scene.setGoalIndex(scene.getGoalIndex() + 1);
            scene.setGoalStarted(false);
            return true;
        } else {
            return result == GoalTickResult.CONTINUE_SYNC;
        }
    }

    public boolean tryJoin(final Scene scene, final Player player, @Nullable final Character character) {
        Optional<Actor> currentActorOpt = getActor(scene, player);
        // Player is already part of the scene, ignore
        if (currentActorOpt.isPresent()) {
            return false;
        }

        // Player wishes to spectate, allow it
        if (character == null) {
            scene.getActors().add(new Actor(player));
            return true;
        }

        // Character is not playable, don't allow
        if (!character.isPlayable()) {
            return false;
        }

        currentActorOpt = getActor(scene, character);
        // Character is already being played
        if (currentActorOpt.isPresent()) {
            return false;
        }

        // Character is not being played, allow joining
        scene.getActors().add(new Actor(character, player));
        return true;
    }

    public void tryLeave(final Scene scene, final Player player) {
        final Optional<Actor> actorToRemove = scene.getActors().stream().filter(actor -> actor.representedBy(player)).findFirst();
        actorToRemove.ifPresent(actor -> scene.getActors().remove(actor));
    }

    public Optional<Actor> getActor(final Scene scene, final Player player) {
        return scene.getActors().stream().filter(actor -> actor.representedBy(player)).findFirst();
    }

    public Optional<Actor> getActor(final Scene scene, final Character character) {
        return scene.getActors().stream().filter(actor -> actor.represents(character)).findFirst();
    }

    public Optional<ChapterGoal> getCurrentGoal(final Scene scene) {
        final int goalIndex = scene.getGoalIndex();
        if (goals.isEmpty() || goalIndex >= goals.size() || goalIndex < 0) {
            return Optional.empty();
        }
        return Optional.of(goals.get(goalIndex));
    }
}

