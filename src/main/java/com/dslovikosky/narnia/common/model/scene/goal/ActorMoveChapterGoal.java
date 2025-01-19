package com.dslovikosky.narnia.common.model.scene.goal;

import com.dslovikosky.narnia.common.model.scene.Actor;
import com.dslovikosky.narnia.common.model.scene.Character;
import com.dslovikosky.narnia.common.model.scene.GoalTickResult;
import com.dslovikosky.narnia.common.model.scene.Scene;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.Optional;

public class ActorMoveChapterGoal extends BackgroundChapterGoal {
    protected final DeferredHolder<Character, ? extends Character> character;
    protected final Vec3 position;

    public ActorMoveChapterGoal(final DeferredHolder<Character, ? extends Character> character, final Vec3 position) {
        this.character = character;
        this.position = position;
    }

    @Override
    public boolean start(Scene scene, Level level) {
        scene.getChapter().getActor(scene, character.get()).setTargetPosition(position);
        return true;
    }

    @Override
    public GoalTickResult tick(Scene scene, Level level) {
        final Actor actor = scene.getChapter().getActor(scene, character.get());
        final Optional<LivingEntity> entity = actor.getCharacter().getEntity(actor, level);

        if (entity.isPresent()) {
            final Vec3 currentPosition = entity.get().position();
            if (currentPosition.distanceTo(position) < 1) {
                return GoalTickResult.COMPLETED;
            }
        }

        return GoalTickResult.CONTINUE;
    }
}
