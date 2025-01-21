package com.dslovikosky.narnia.common.model.scene.goal;

import com.dslovikosky.narnia.common.model.scene.Actor;
import com.dslovikosky.narnia.common.model.scene.Character;
import com.dslovikosky.narnia.common.model.scene.GoalTickResult;
import com.dslovikosky.narnia.common.model.scene.Scene;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.Optional;

public class ActorMoveChapterGoal extends BackgroundChapterGoal {
    private final ResourceKey<Level> dimension;
    private final DeferredHolder<Character, ? extends Character> character;
    private final Vec3 position;

    public ActorMoveChapterGoal(final ResourceKey<Level> dimension, final DeferredHolder<Character, ? extends Character> character, final Vec3 position) {
        this.dimension = dimension;
        this.character = character;
        this.position = position;
    }

    @Override
    public boolean start(Scene scene, Level level) {
        if (level.dimension() != dimension) {
            return false;
        }

        scene.getChapter().getActor(scene, character.get()).setTargetPosition(position);
        return true;
    }

    @Override
    public GoalTickResult tick(Scene scene, Level level) {
        if (level.dimension() != dimension) {
            return GoalTickResult.CONTINUE;
        }

        final Actor actor = scene.getChapter().getActor(scene, character.get());
        final Optional<LivingEntity> entity = actor.getCharacter().getEntity(actor, level);

        if (entity.isPresent()) {
            final Vec3 currentPosition = entity.get().position();
            if (currentPosition.distanceTo(position) < 2) {
                return GoalTickResult.COMPLETED;
            }
        }

        return GoalTickResult.CONTINUE;
    }
}
