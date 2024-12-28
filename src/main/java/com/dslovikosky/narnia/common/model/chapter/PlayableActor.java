package com.dslovikosky.narnia.common.model.chapter;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public record PlayableActor(ResourceLocation id) {
    public Component getName() {
        return Component.translatable(String.format("actor.%s.%s.name", id.getNamespace(), id.getPath()));
    }
}
