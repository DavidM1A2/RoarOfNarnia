package com.dslovikosky.narnia.common.model.scene;

import com.dslovikosky.narnia.common.constants.ModRegistries;
import com.dslovikosky.narnia.common.utils.LongStreamCodec;
import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.UUIDUtil;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.PatchedDataComponentMap;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.neoforge.common.MutableDataComponentHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Scene implements MutableDataComponentHolder {
    public static final StreamCodec<RegistryFriendlyByteBuf, Scene> STREAM_CODEC = LongStreamCodec.composite(
            ByteBufCodecs.registry(ModRegistries.CHAPTER_KEY), Scene::getChapter,
            Actor.STREAM_CODEC.apply(ByteBufCodecs.list()), it -> ImmutableList.copyOf(it.getActors().values()),
            UUIDUtil.STREAM_CODEC.apply(ByteBufCodecs.list()), Scene::getPlayerIds,
            DataComponentPatch.STREAM_CODEC, chapterInstance -> chapterInstance.components.asPatch(),
            UUIDUtil.STREAM_CODEC, Scene::getId,
            ByteBufCodecs.STRING_UTF8.map(SceneState::valueOf, SceneState::toString), Scene::getState,
            ByteBufCodecs.INT, Scene::getGoalIndex,
            Scene::new);
    public static final Codec<Scene> CODEC = Codec.lazyInitialized(() -> RecordCodecBuilder.create(instance -> instance.group(
            ModRegistries.CHAPTER.byNameCodec().fieldOf("chapter_id").forGetter(Scene::getChapter),
            Codec.list(Actor.CODEC).fieldOf("actors").forGetter(it -> ImmutableList.copyOf(it.getActors().values())),
            Codec.list(UUIDUtil.CODEC).fieldOf("player_ids").forGetter(Scene::getPlayerIds),
            DataComponentPatch.CODEC.fieldOf("components").forGetter(chapterInstance -> chapterInstance.components.asPatch()),
            UUIDUtil.CODEC.fieldOf("id").forGetter(Scene::getId),
            Codec.STRING.fieldOf("state").xmap(SceneState::valueOf, SceneState::toString).forGetter(Scene::getState),
            Codec.INT.fieldOf("goal_index").forGetter(Scene::getGoalIndex)
    ).apply(instance, Scene::new)));

    private final Chapter chapter;
    private final Map<Character, Actor> actors;
    private final List<UUID> playerIds;
    private final PatchedDataComponentMap components;
    private final UUID id;
    private SceneState state;
    private int goalIndex;

    public Scene(final Chapter chapter) {
        this.chapter = chapter;
        this.actors = new HashMap<>();
        this.playerIds = new ArrayList<>();
        this.components = new PatchedDataComponentMap(buildComponentMap(chapter));
        this.id = UUID.randomUUID();
        this.state = SceneState.NEW;
        this.goalIndex = 0;
    }

    private Scene(final Chapter chapter, final List<Actor> actors, final List<UUID> playerIds, final DataComponentPatch components, final UUID id, final SceneState state, final int goalIndex) {
        this.chapter = chapter;
        this.actors = actors.stream().collect(Collectors.toMap(Actor::getCharacter, Function.identity()));
        this.playerIds = new ArrayList<>(playerIds);
        this.components = PatchedDataComponentMap.fromPatch(buildComponentMap(chapter), components);
        this.id = id;
        this.state = state;
        this.goalIndex = goalIndex;
    }

    public Chapter getChapter() {
        return chapter;
    }

    public Map<Character, Actor> getActors() {
        return actors;
    }

    public List<UUID> getPlayerIds() {
        return playerIds;
    }

    public UUID getId() {
        return id;
    }

    public int getGoalIndex() {
        return goalIndex;
    }

    public void setGoalIndex(final int goalIndex) {
        this.goalIndex = goalIndex;
    }

    public SceneState getState() {
        return state;
    }

    public void setState(SceneState state) {
        this.state = state;
    }

    @Override
    public <T> @Nullable T set(final @NotNull DataComponentType<? super T> componentType, @Nullable final T value) {
        return this.components.set(componentType, value);
    }

    @Override
    public <T> @Nullable T remove(final @NotNull DataComponentType<? extends T> componentType) {
        return this.components.remove(componentType);
    }

    @Override
    public void applyComponents(final @NotNull DataComponentPatch patch) {
        this.components.applyPatch(patch);
    }

    @Override
    public void applyComponents(final @NotNull DataComponentMap components) {
        this.components.setAll(components);
    }

    @Override
    public @NotNull DataComponentMap getComponents() {
        return components;
    }

    private DataComponentMap buildComponentMap(final Chapter chapter) {
        final DataComponentMap.Builder builder = DataComponentMap.builder();
        chapter.getGoals().forEach(goal -> goal.registerComponents(builder));
        return builder.build();
    }

    @Override
    public String toString() {
        return "Scene{" +
                "chapter=" + chapter +
                ", actors=" + actors +
                ", playerIds=" + playerIds +
                ", components=" + components +
                ", id=" + id +
                ", state=" + state +
                ", goalIndex=" + goalIndex +
                '}';
    }
}
