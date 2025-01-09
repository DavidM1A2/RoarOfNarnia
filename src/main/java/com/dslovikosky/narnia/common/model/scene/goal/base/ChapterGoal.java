package com.dslovikosky.narnia.common.model.scene.goal.base;

import com.dslovikosky.narnia.common.entity.human_child.SceneEntity;
import com.dslovikosky.narnia.common.model.scene.Actor;
import com.dslovikosky.narnia.common.model.scene.Character;
import com.dslovikosky.narnia.common.model.scene.GoalTickResult;
import com.dslovikosky.narnia.common.model.scene.Scene;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.Optional;

public abstract class ChapterGoal {
    public ChapterGoal() {
    }

    public boolean start(final Scene scene, final Level level) {
        return true;
    }

    public GoalTickResult tick(final Scene scene, final Level level) {
        return GoalTickResult.COMPLETED;
    }

    public void finish(final Scene scene, final Level level) {
    }

    public void registerComponents(final DataComponentMap.Builder builder) {
    }

    public abstract Component getDescription(final Scene scene, final Level level);

    protected void ensureActorsExist(Scene scene, Level level) {
        final List<com.dslovikosky.narnia.common.model.scene.Character> characters = scene.getChapter().getCharacters();
        for (final com.dslovikosky.narnia.common.model.scene.Character character : characters) {
            final Optional<Actor> currentActorOpt = scene.getChapter().getActor(scene, character);
            boolean needsToSpawnActor = false;
            if (currentActorOpt.isEmpty()) {
                needsToSpawnActor = true;
            } else {
                final Actor currentActor = currentActorOpt.get();
                final Optional<LivingEntity> currentActorEntity = character.getEntity(currentActor, level);
                if (currentActorEntity.isEmpty()) {
                    needsToSpawnActor = true;
                } else if (!currentActorEntity.get().isAlive() && !currentActor.isPlayerControlled()) {
                    needsToSpawnActor = true;
                }
            }

            if (needsToSpawnActor) {
                final EntityType<? extends LivingEntity> entityType = character.getEntityType();
                final LivingEntity actorEntity = entityType.create(level);
                if (actorEntity instanceof SceneEntity sceneEntity) {
                    sceneEntity.setSceneId(scene.getId());
                }
                initActorEntity(scene, character, actorEntity);
                scene.getChapter().join(scene, actorEntity, character);
                level.addFreshEntity(actorEntity);
            }
        }
    }

    protected void initActorEntity(final Scene scene, final Character character, final Entity entity) {
    }
}
