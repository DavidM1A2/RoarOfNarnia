package com.dslovikosky.narnia.common.model.chapter;

import com.dslovikosky.narnia.common.constants.ModRegistries;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
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

public class Scene implements MutableDataComponentHolder {
    public static final StreamCodec<RegistryFriendlyByteBuf, Scene> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.registry(ModRegistries.CHAPTER_KEY), Scene::getChapter,
            Actor.STREAM_CODEC.apply(ByteBufCodecs.list()), Scene::getActors,
            DataComponentPatch.STREAM_CODEC, chapterInstance -> chapterInstance.components.asPatch(),
            Scene::new);
    public static final Codec<Scene> CODEC = Codec.lazyInitialized(() -> RecordCodecBuilder.create(instance -> instance.group(
            ModRegistries.CHAPTER.byNameCodec().fieldOf("chapter_id").forGetter(Scene::getChapter),
            Codec.list(Actor.CODEC).fieldOf("actors").forGetter(Scene::getActors),
            DataComponentPatch.CODEC.fieldOf("components").forGetter(chapterInstance -> chapterInstance.components.asPatch())
    ).apply(instance, Scene::new)));

    private final Chapter chapter;
    private final List<Actor> actors;
    private final PatchedDataComponentMap components;

    public Scene(final Chapter chapter) {
        this.chapter = chapter;
        this.actors = new ArrayList<>();
        this.components = new PatchedDataComponentMap(DataComponentMap.EMPTY);
    }

    private Scene(final Chapter chapter, final List<Actor> actors, final DataComponentPatch components) {
        this.chapter = chapter;
        this.actors = new ArrayList<>(actors);
        this.components = PatchedDataComponentMap.fromPatch(DataComponentMap.EMPTY, components);
    }

    public Chapter getChapter() {
        return chapter;
    }

    public List<Actor> getActors() {
        return actors;
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

    @Override
    public String toString() {
        return "Scene{" +
                "chapter=" + chapter +
                ", actors=" + actors +
                ", components=" + components +
                '}';
    }
}
