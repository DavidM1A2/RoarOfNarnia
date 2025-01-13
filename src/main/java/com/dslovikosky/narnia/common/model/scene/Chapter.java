package com.dslovikosky.narnia.common.model.scene;

import com.dslovikosky.narnia.common.entity.human_child.SceneEntity;
import com.dslovikosky.narnia.common.model.scene.goal.base.ChapterGoal;
import com.google.common.base.Suppliers;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.registries.DeferredHolder;

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

    public Chapter(final ResourceLocation id, final DeferredHolder<Book, ? extends Book> book, final List<DeferredHolder<Character, ? extends Character>> characters, final List<ChapterGoal> goals) {
        this.id = id;
        this.book = book::get;
        this.characters = Suppliers.memoize(() -> characters.stream().map(DeferredHolder::get).map(Character.class::cast).toList());
        this.goals = goals;
    }

    public Component getTitle() {
        return Component.translatable(String.format("chapter.%s.%s.%s.title", id.getNamespace(), book.get().getId().getPath(), id.getPath()));
    }

    public void start(final Scene scene, final Level level) {
    }

    public void stop(final Scene scene, final Level level, final boolean isComplete) {
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

    public void leave(final Scene scene, final Player player) {
        scene.getPlayerIds().remove(player.getUUID());
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

    public LivingEntity getActingEntity(final Scene scene, final Level level, final Character character) {
        final Actor actor = getActor(scene, character);
        final Optional<LivingEntity> actingEntityOpt = actor.getCharacter().getEntity(actor, level);
        if (actingEntityOpt.isPresent()) {
            return actingEntityOpt.get();
        }

        final EntityType<? extends LivingEntity> entityType = character.getEntityType();
        final LivingEntity actorEntity = entityType.create(level);
        if (actorEntity == null) {
            return null;
        }

        if (actorEntity instanceof SceneEntity sceneEntity) {
            sceneEntity.setSceneId(scene.getId());
        }
        actorEntity.setPos(actor.getTargetPosition());
        actorEntity.lookAt(EntityAnchorArgument.Anchor.EYES, actor.getLookPosition());
        actor.setEntity(actorEntity);
        level.addFreshEntity(actorEntity);
        return actorEntity;
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

