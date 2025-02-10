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
import java.util.function.Supplier;

public class ActorMoveChapterGoal extends BackgroundChapterGoal {
    private final DeferredHolder<Character, ? extends Character> character;
    private final Supplier<Vec3> position;
    private final boolean canUseDoors;

    public ActorMoveChapterGoal(final DeferredHolder<Character, ? extends Character> character, final Supplier<Vec3> position) {
        this(character, position, true);
    }

    public ActorMoveChapterGoal(final DeferredHolder<Character, ? extends Character> character, final Supplier<Vec3> position, final boolean canUseDoors) {
        this.character = character;
        this.position = position;
        this.canUseDoors = canUseDoors;
    }

    @Override
    public boolean start(Scene scene, Level level) {
        final Actor actor = scene.getChapter().getActor(scene, character.get());
        actor.setTargetPosition(position.get());
        actor.setCanUseDoors(canUseDoors);
        return true;
    }

    @Override
    public GoalTickResult tick(Scene scene, Level level) {
        final Actor actor = scene.getChapter().getActor(scene, character.get());
        final Optional<LivingEntity> entity = actor.getCharacter().getEntity(actor, level);

        if (entity.isPresent()) {
            final Vec3 currentPosition = entity.get().position();
            if (currentPosition.closerThan(position.get(), 1)) {
                return GoalTickResult.COMPLETED;
            }
        }

        return GoalTickResult.CONTINUE;
    }

    @Override
    public void finish(Scene scene, Level level) {
        final Actor actor = scene.getChapter().getActor(scene, character.get());
        actor.setCanUseDoors(true);
    }
}
