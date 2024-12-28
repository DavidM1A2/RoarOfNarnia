package com.dslovikosky.narnia.common.constants;

import com.dslovikosky.narnia.common.model.chapter.Book;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.RegistryBuilder;

import java.util.List;

public class ModRegistries {
    public static final ResourceKey<Registry<Book>> BOOK_KEY = ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "book"));

    public static final Registry<Book> BOOK = new RegistryBuilder<>(BOOK_KEY).sync(true).maxId(256).create();

    public static final List<Registry<?>> REGISTRIES = List.of(BOOK);
}
