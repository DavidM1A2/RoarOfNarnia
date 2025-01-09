package com.dslovikosky.narnia.common.model.scene;

import com.dslovikosky.narnia.common.model.scene.goal.base.ChapterGoal;
import com.google.common.base.Suppliers;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.registries.DeferredHolder;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public class Chapter {
    private final ResourceLocation id;
    private final Supplier<Book> book;
    private final Supplier<List<Character>> characters;
    private final List<ChapterGoal> goals;

    public Chapter(final ResourceLocation id, final DeferredHolder<Book, ? extends Book> book, final List<DeferredHolder<Character, ? extends Character>> characters, final List<ChapterGoal> goals) {
        this.id = id;
        this.book = book::get;
        this.characters = Suppliers.memoize(() -> characters.stream().map(DeferredHolder::get).map(Character.class::cast).toList());
        this.goals = goals;
    }

    public Component getTitle() {
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

    public void join(final Scene scene, final LivingEntity entity, final Character character) {
        scene.getActors().add(new Actor(character, entity));
    }

    public void leave(final Scene scene, final Character character) {
        scene.getActors().removeIf(actor -> actor.getCharacter().equals(character));
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
        final Optional<Actor> actorToRemove = scene.getActors().stream().filter(actor -> actor.getCharacter().representedBy(actor, player)).findFirst();
        actorToRemove.ifPresent(actor -> scene.getActors().remove(actor));
    }

    public Optional<Actor> getActor(final Scene scene, final Player player) {
        return scene.getActors().stream().filter(actor -> actor.getCharacter().representedBy(actor, player)).findFirst();
    }

    public Optional<Actor> getActor(final Scene scene, final Character character) {
        return scene.getActors().stream().filter(actor -> actor.getCharacter().represents(actor, character)).findFirst();
    }

    public Optional<ChapterGoal> getCurrentGoal(final Scene scene) {
        final int goalIndex = scene.getGoalIndex();
        if (goals.isEmpty() || goalIndex >= goals.size() || goalIndex < 0) {
            return Optional.empty();
        }
        return Optional.of(goals.get(goalIndex));
    }

    public ResourceLocation getId() {
        return id;
    }

    public Book getBook() {
        return book.get();
    }

    public List<Character> getCharacters() {
        return characters.get();
    }

    public List<ChapterGoal> getGoals() {
        return goals;
    }

    @Override
    public String toString() {
        return "Chapter{" +
                "id=" + id +
                ", book=" + book +
                ", characters=" + characters +
                ", goals=" + goals +
                '}';
    }
}

