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
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.MutableDataComponentHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Scene implements MutableDataComponentHolder {
    public static final StreamCodec<RegistryFriendlyByteBuf, Scene> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.registry(ModRegistries.BOOK_KEY), Scene::getBook,
            ResourceLocation.STREAM_CODEC, it -> it.chapter.id(),
            DataComponentPatch.STREAM_CODEC, chapterInstance -> chapterInstance.components.asPatch(),
            Scene::new);
    public static final Codec<Scene> CODEC = Codec.lazyInitialized(() -> RecordCodecBuilder.create(instance -> instance.group(
            ModRegistries.BOOK.byNameCodec().fieldOf("book").forGetter(Scene::getBook),
            ResourceLocation.CODEC.fieldOf("chapterId").forGetter(it -> it.chapter.id()),
            DataComponentPatch.CODEC.fieldOf("components").forGetter(chapterInstance -> chapterInstance.components.asPatch())
    ).apply(instance, Scene::new)));

    private final Book book;
    private final Chapter chapter;
    private final PatchedDataComponentMap components;

    public Scene(final Book book, final Chapter chapter) {
        this.book = book;
        this.chapter = chapter;
        this.components = new PatchedDataComponentMap(DataComponentMap.EMPTY);
    }

    private Scene(final Book book, final ResourceLocation chapterId, final DataComponentPatch components) {
        this.book = book;
        this.chapter = book.getChapter(chapterId);
        this.components = PatchedDataComponentMap.fromPatch(DataComponentMap.EMPTY, components);
    }

    public Book getBook() {
        return book;
    }

    public Chapter getChapter() {
        return chapter;
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
        return String.format("Scene{book=%s, data=%s}", book, components);
    }
}
