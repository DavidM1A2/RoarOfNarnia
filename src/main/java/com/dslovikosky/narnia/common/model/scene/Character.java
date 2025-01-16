package com.dslovikosky.narnia.common.model.scene;

import com.dslovikosky.narnia.common.entity.human_child.SceneEntity;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
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

    public LivingEntity getOrCreateEntity(final Scene scene, final Actor actor, final Level level) {
        final Optional<LivingEntity> entityOpt = getEntity(actor, level);
        if (entityOpt.isPresent()) {
            return entityOpt.get();
        }

        final LivingEntity actorEntity = entityType.get().create(level);
        if (actorEntity == null) {
            return null;
        }

        if (actorEntity instanceof SceneEntity sceneEntity) {
            sceneEntity.setSceneId(scene.getId());
        }
        actorEntity.setPos(actor.getTargetPosition());
        actorEntity.lookAt(EntityAnchorArgument.Anchor.EYES, actor.getLookPosition());
        actor.setEntity(actorEntity);
        level.addFreshEntity(actorEntity);
        return actorEntity;
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
