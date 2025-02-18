package com.dslovikosky.narnia.common.model.scene.goal;

import com.dslovikosky.narnia.common.constants.ModDataComponentTypes;
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
    private final long timeoutTicks;

    public ActorMoveChapterGoal(final DeferredHolder<Character, ? extends Character> character, final Supplier<Vec3> position) {
        this(character, position, 60 * 20, true);
    }

    public ActorMoveChapterGoal(final DeferredHolder<Character, ? extends Character> character, final Supplier<Vec3> position, final boolean canUseDoors) {
        this(character, position, 60 * 20, canUseDoors);
    }

    public ActorMoveChapterGoal(final DeferredHolder<Character, ? extends Character> character, final Supplier<Vec3> position, final long timeoutTicks) {
        this(character, position, timeoutTicks, true);
    }

    public ActorMoveChapterGoal(final DeferredHolder<Character, ? extends Character> character, final Supplier<Vec3> position, final long timeoutTicks, final boolean canUseDoors) {
        this.character = character;
        this.position = position;
        this.canUseDoors = canUseDoors;
        this.timeoutTicks = timeoutTicks;
    }

    @Override
    public boolean start(Scene scene, Level level) {
        final Actor actor = scene.getChapter().getActor(scene, character.get());
        actor.setTargetPosition(position.get());
        actor.setCanUseDoors(canUseDoors);
        scene.set(ModDataComponentTypes.ACTOR_MOVE_TICKS_LEFT, timeoutTicks);
        return true;
    }

    @Override
    public GoalTickResult tick(Scene scene, Level level) {
        final Long moveTicksLeft = scene.update(ModDataComponentTypes.ACTOR_MOVE_TICKS_LEFT, 0L, ticksLeft -> ticksLeft - 1);

        final Actor actor = scene.getChapter().getActor(scene, character.get());
        final Optional<LivingEntity> entity = actor.getCharacter().getEntity(actor, level);

        if (entity.isPresent()) {
            if (moveTicksLeft <= 0) {
                entity.get().setPos(position.get());
            }
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
