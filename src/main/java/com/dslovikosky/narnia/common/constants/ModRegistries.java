package com.dslovikosky.narnia.common.constants;

import com.dslovikosky.narnia.common.model.scene.Book;
import com.dslovikosky.narnia.common.model.scene.Chapter;
import com.dslovikosky.narnia.common.model.scene.Character;
import com.dslovikosky.narnia.common.model.schematic.Schematic;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.RegistryBuilder;

import java.util.List;

public class ModRegistries {
    public static final ResourceKey<Registry<Book>> BOOK_KEY = ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "book"));
    public static final ResourceKey<Registry<Chapter>> CHAPTER_KEY = ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "chapter"));
    public static final ResourceKey<Registry<Character>> CHARACTER_KEY = ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "character"));
    public static final ResourceKey<Registry<Schematic>> SCHEMATIC_KEY = ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "schematic"));

    public static final Registry<Book> BOOK = new RegistryBuilder<>(BOOK_KEY).sync(true).maxId(256).create();
    public static final Registry<Chapter> CHAPTER = new RegistryBuilder<>(CHAPTER_KEY).sync(true).maxId(256).create();
    public static final Registry<Character> CHARACTER = new RegistryBuilder<>(CHARACTER_KEY).sync(true).maxId(256).create();
    public static final Registry<Schematic> SCHEMATIC = new RegistryBuilder<>(SCHEMATIC_KEY).sync(false).maxId(256).create();

    public static final List<Registry<?>> REGISTRIES = List.of(BOOK, CHAPTER, CHARACTER, SCHEMATIC);
}
