package com.dslovikosky.narnia.common.model.scene;

import com.dslovikosky.narnia.common.constants.ModRegistries;
import com.dslovikosky.narnia.common.utils.LongStreamCodec;
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
import java.util.List;
import java.util.UUID;

public class Scene implements MutableDataComponentHolder {
    public static final StreamCodec<RegistryFriendlyByteBuf, Scene> STREAM_CODEC = LongStreamCodec.composite(
            ByteBufCodecs.registry(ModRegistries.CHAPTER_KEY), Scene::getChapter,
            Actor.STREAM_CODEC.apply(ByteBufCodecs.list()), Scene::getActors,
            UUIDUtil.STREAM_CODEC.apply(ByteBufCodecs.list()), Scene::getPlayerIds,
            DataComponentPatch.STREAM_CODEC, chapterInstance -> chapterInstance.components.asPatch(),
            UUIDUtil.STREAM_CODEC, Scene::getId,
            ByteBufCodecs.INT, Scene::getGoalIndex,
            ByteBufCodecs.BOOL, Scene::isGoalStarted,
            Scene::new);
    public static final Codec<Scene> CODEC = Codec.lazyInitialized(() -> RecordCodecBuilder.create(instance -> instance.group(
            ModRegistries.CHAPTER.byNameCodec().fieldOf("chapter_id").forGetter(Scene::getChapter),
            Codec.list(Actor.CODEC).fieldOf("actors").forGetter(Scene::getActors),
            Codec.list(UUIDUtil.CODEC).fieldOf("player_ids").forGetter(Scene::getPlayerIds),
            DataComponentPatch.CODEC.fieldOf("components").forGetter(chapterInstance -> chapterInstance.components.asPatch()),
            UUIDUtil.CODEC.fieldOf("id").forGetter(Scene::getId),
            Codec.INT.fieldOf("goal_index").forGetter(Scene::getGoalIndex),
            Codec.BOOL.fieldOf("goal_started").forGetter(Scene::isGoalStarted)
    ).apply(instance, Scene::new)));

    private final Chapter chapter;
    private final List<Actor> actors;
    private final List<UUID> playerIds;
    private final PatchedDataComponentMap components;
    private final UUID id;
    private int goalIndex;
    private boolean goalStarted;

    public Scene(final Chapter chapter) {
        this.chapter = chapter;
        this.actors = new ArrayList<>();
        this.playerIds = new ArrayList<>();
        this.components = new PatchedDataComponentMap(buildComponentMap(chapter));
        this.id = UUID.randomUUID();
        this.goalIndex = 0;
        this.goalStarted = false;
    }

    private Scene(final Chapter chapter, final List<Actor> actors, final List<UUID> playerIds, final DataComponentPatch components, final UUID id, final int goalIndex, final boolean goalStarted) {
        this.chapter = chapter;
        this.actors = new ArrayList<>(actors);
        this.playerIds = new ArrayList<>(playerIds);
        this.components = PatchedDataComponentMap.fromPatch(buildComponentMap(chapter), components);
        this.id = id;
        this.goalIndex = goalIndex;
        this.goalStarted = goalStarted;
    }

    public Chapter getChapter() {
        return chapter;
    }

    public List<Actor> getActors() {
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

    public boolean isGoalStarted() {
        return goalStarted;
    }

    public void setGoalStarted(final boolean goalStarted) {
        this.goalStarted = goalStarted;
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
                ", components=" + components +
                ", goalIndex=" + goalIndex +
                ", goalStarted=" + goalStarted +
                ", id=" + id +
                '}';
    }
}
