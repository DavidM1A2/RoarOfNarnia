package com.dslovikosky.narnia.common.model.chapter;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public record Character(ResourceLocation id, boolean isPlayable) {
    public Component getName() {
        return Component.translatable(String.format("character.%s.%s.name", id.getNamespace(), id.getPath()));
    }
}
