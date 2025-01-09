package com.dslovikosky.narnia.common.model.chapter.goal;

import com.dslovikosky.narnia.common.entity.human_child.SceneEntity;
import com.dslovikosky.narnia.common.model.chapter.Actor;
import com.dslovikosky.narnia.common.model.chapter.ChapterGoal;
import com.dslovikosky.narnia.common.model.chapter.Character;
import com.dslovikosky.narnia.common.model.chapter.Scene;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.Optional;

public abstract class ActedChapterGoal implements ChapterGoal {
    protected void ensureActorsExist(Scene scene, Level level) {
        final List<Character> characters = scene.getChapter().getCharacters();
        for (final Character character : characters) {
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

    protected abstract void initActorEntity(final Scene scene, final Character character, final Entity entity);
}
