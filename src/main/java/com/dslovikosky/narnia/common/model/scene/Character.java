package com.dslovikosky.narnia.common.model.scene;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.Optional;
import java.util.function.Supplier;

public class Character {
    private final ResourceLocation id;
    private final Supplier<EntityType<? extends LivingEntity>> entityType;

    public Character(final ResourceLocation id, final Supplier<EntityType<? extends LivingEntity>> entityType) {
        this.id = id;
        this.entityType = entityType;
    }

    public Component getName() {
        return Component.translatable(String.format("character.%s.%s.name", id.getNamespace(), id.getPath()));
    }

    public boolean represents(final Actor actor, final Character character) {
        return character == actor.getCharacter();
    }

    public boolean representedBy(final Actor actor, final Player player) {
        return actor.getEntityId().equals(player.getUUID());
    }

    public Optional<LivingEntity> getEntity(final Actor actor, final Level level) {
        if (level instanceof ServerLevel serverLevel) {
            return Optional.ofNullable(serverLevel.getEntity(actor.getEntityId()))
                    .filter(it -> it instanceof LivingEntity)
                    .map(LivingEntity.class::cast);
        }
        return Optional.empty();
    }

    public ResourceLocation getId() {
        return id;
    }

    public EntityType<? extends LivingEntity> getEntityType() {
        return entityType.get();
    }

    @Override
    public String toString() {
        return "Character{" +
                "id=" + id +
                ", entityType=" + entityType +
                '}';
    }
}
