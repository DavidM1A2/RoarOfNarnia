package com.dslovikosky.narnia.common.model.scene.goal;

import com.dslovikosky.narnia.common.model.scene.Actor;
import com.dslovikosky.narnia.common.model.scene.Character;
import com.dslovikosky.narnia.common.model.scene.Scene;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.registries.DeferredHolder;

public class ActorLookChapterGoal extends BackgroundChapterGoal {
    private final DeferredHolder<Character, ? extends Character> character;
    private final Vec3 direction;
    private final DeferredHolder<Character, ? extends Character> target;

    public ActorLookChapterGoal(final DeferredHolder<Character, ? extends Character> character, final Vec3 direction) {
        this.character = character;
        this.direction = direction;
        this.target = null;
    }

    public ActorLookChapterGoal(final DeferredHolder<Character, ? extends Character> character, final DeferredHolder<Character, ? extends Character> target) {
        this.character = character;
        this.direction = null;
        this.target = target;
    }

    @Override
    public boolean start(Scene scene, Level level) {
        final Vec3 targetLookPosition;
        final Actor sourceActor = scene.getChapter().getActor(scene, character.get());
        if (direction != null) {
            final LivingEntity sourceActorEntity = sourceActor.getCharacter().getOrCreateEntity(scene, sourceActor, level);
            targetLookPosition = sourceActorEntity.getEyePosition().add(direction);
        } else {
            final Actor targetActor = scene.getChapter().getActor(scene, target.get());
            final LivingEntity targetActorEntity = targetActor.getCharacter().getOrCreateEntity(scene, targetActor, level);
            targetLookPosition = targetActorEntity.getEyePosition();
        }
        sourceActor.setLookPosition(targetLookPosition);
        return super.start(scene, level);
    }
}
