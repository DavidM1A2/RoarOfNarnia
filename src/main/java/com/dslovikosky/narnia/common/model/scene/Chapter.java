package com.dslovikosky.narnia.common.model.scene;

import com.dslovikosky.narnia.common.model.scene.goal.ChapterGoal;
import com.google.common.base.Suppliers;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

public class Chapter {
    private final ResourceLocation id;
    private final Supplier<Book> book;
    private final Supplier<List<Character>> characters;
    private final List<ChapterGoal> goals;
    private final ResourceKey<Level> startingDimension;
    private final Vec3 startingSpawnPosition;

    public Chapter(final ResourceLocation id, final DeferredHolder<Book, ? extends Book> book, final List<DeferredHolder<Character, ? extends Character>> characters, final ResourceKey<Level> startingDimension, final Vec3 startingSpawnPosition) {
        this.id = id;
        this.book = book::get;
        this.characters = Suppliers.memoize(() -> characters.stream().map(DeferredHolder::get).map(Character.class::cast).toList());
        this.goals = new ArrayList<>();
        this.startingDimension = startingDimension;
        this.startingSpawnPosition = startingSpawnPosition;
    }

    public void addGoal(final ChapterGoal goal) {
        this.goals.add(goal);
    }

    public Component getTitle() {
        return Component.translatable(String.format("chapter.%s.%s.%s.title", id.getNamespace(), book.get().getId().getPath(), id.getPath()));
    }

    public void start(final Scene scene, final Level level) {
    }

    public void stop(final Scene scene, final Level level, final boolean isComplete) {
        getPlayers(scene, level).forEach(player -> leave(scene, player));
    }

    public void tick(final Scene scene, final Level level) {
    }

    public boolean join(final Scene scene, final Player player) {
        final List<UUID> playerIds = scene.getPlayerIds();
        if (!playerIds.contains(player.getUUID())) {
            playerIds.add(player.getUUID());
            return true;
        }
        return false;
    }

    public boolean leave(final Scene scene, final Player player) {
        return scene.getPlayerIds().remove(player.getUUID());
    }

    public List<Player> getPlayers(final Scene scene, final Level level) {
        return scene.getPlayerIds().stream().map(level::getPlayerByUUID).filter(Objects::nonNull).toList();
    }

    public boolean isParticipating(final Scene scene, final Player player) {
        return scene.getPlayerIds().contains(player.getUUID());
    }

    public Actor getActor(final Scene scene, final Character character) {
        return scene.getActors().get(character);
    }

    public Actor getActor(final Scene scene, final LivingEntity entity) {
        return scene.getActors().values().stream().filter(actor -> entity.getUUID().equals(actor.getEntityId())).findFirst().orElse(null);
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

    public ResourceKey<Level> getStartingDimension() {
        return startingDimension;
    }

    public Vec3 getStartingSpawnPosition() {
        return startingSpawnPosition;
    }

    @Override
    public String toString() {
        return "Chapter{" +
                "id=" + id +
                ", book=" + book +
                ", characters=" + characters +
                ", goals=" + goals +
                ", startingDimension=" + startingDimension +
                ", startingSpawnPosition=" + startingSpawnPosition +
                '}';
    }
}

