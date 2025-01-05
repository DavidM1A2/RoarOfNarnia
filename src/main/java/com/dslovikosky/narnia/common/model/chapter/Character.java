package com.dslovikosky.narnia.common.model.chapter;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;

public record Character(ResourceLocation id, EntityType<? extends LivingEntity> entityType, boolean isPlayable) {
    public Component getName() {
        return Component.translatable(String.format("character.%s.%s.name", id.getNamespace(), id.getPath()));
    }
}
