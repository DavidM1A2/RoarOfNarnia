package com.dslovikosky.narnia.common.model.chapter;

import com.dslovikosky.narnia.common.constants.ModRegistries;
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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Scene implements MutableDataComponentHolder {
    public static final StreamCodec<RegistryFriendlyByteBuf, Scene> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.registry(ModRegistries.CHAPTER_KEY), Scene::getChapter,
            UUIDUtil.STREAM_CODEC.apply(ByteBufCodecs.collection(it -> new HashSet<>())), Scene::getSpectatingPlayerIds,
            Actor.STREAM_CODEC.apply(ByteBufCodecs.list()), it -> new ArrayList<>(it.getCharacterInstances().values()),
            DataComponentPatch.STREAM_CODEC, chapterInstance -> chapterInstance.components.asPatch(),
            Scene::new);
    public static final Codec<Scene> CODEC = Codec.lazyInitialized(() -> RecordCodecBuilder.create(instance -> instance.group(
            ModRegistries.CHAPTER.byNameCodec().fieldOf("chapter_id").forGetter(Scene::getChapter),
            UUIDUtil.CODEC_SET.fieldOf("spectating_player_ids").forGetter(Scene::getSpectatingPlayerIds),
            Codec.list(Actor.CODEC).fieldOf("character_instances").forGetter(it -> it.getCharacterInstances().values().stream().toList()),
            DataComponentPatch.CODEC.fieldOf("components").forGetter(chapterInstance -> chapterInstance.components.asPatch())
    ).apply(instance, Scene::new)));

    private final Chapter chapter;
    private final Set<UUID> spectatingPlayerIds;
    private final Map<Character, Actor> characterInstances;
    private final PatchedDataComponentMap components;

    public Scene(final Chapter chapter) {
        this.chapter = chapter;
        this.spectatingPlayerIds = new HashSet<>();
        this.characterInstances = new HashMap<>();
        this.components = new PatchedDataComponentMap(DataComponentMap.EMPTY);
    }

    private Scene(final Chapter chapter, final Set<UUID> spectatingPlayerIds, final List<Actor> characterInstances, final DataComponentPatch components) {
        this.chapter = chapter;
        this.spectatingPlayerIds = spectatingPlayerIds;
        this.characterInstances = characterInstances.stream().collect(Collectors.toMap(Actor::character, Function.identity()));
        this.components = PatchedDataComponentMap.fromPatch(DataComponentMap.EMPTY, components);
    }

    public Chapter getChapter() {
        return chapter;
    }

    public Set<UUID> getSpectatingPlayerIds() {
        return spectatingPlayerIds;
    }

    public Map<Character, Actor> getCharacterInstances() {
        return characterInstances;
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
                ", spectatingPlayerIds=" + spectatingPlayerIds +
                ", characterInstances=" + characterInstances +
                ", components=" + components +
                '}';
    }
}
