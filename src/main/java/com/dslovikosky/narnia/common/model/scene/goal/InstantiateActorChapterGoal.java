package com.dslovikosky.narnia.common.model.scene.goal;

import com.dslovikosky.narnia.common.model.scene.Actor;
import com.dslovikosky.narnia.common.model.scene.Chapter;
import com.dslovikosky.narnia.common.model.scene.Character;
import com.dslovikosky.narnia.common.model.scene.Scene;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.function.Supplier;

public class InstantiateActorChapterGoal extends BackgroundChapterGoal {
    private final DeferredHolder<Character, ? extends Character> character;
    private final Supplier<Vec3> position;
    private final Vec3 direction;

    public InstantiateActorChapterGoal(final DeferredHolder<Character, ? extends Character> character, final Supplier<Vec3> position, final Vec3 direction) {
        this.character = character;
        this.position = position;
        this.direction = direction;
    }

    @Override
    public boolean start(Scene scene, Level level) {
        final Chapter chapter = scene.getChapter();
        final Actor actor = chapter.getActor(scene, character.get());
        actor.setTargetPosition(position.get());
        actor.setLookPosition(position.get().add(direction));
        final LivingEntity entity = actor.getCharacter().getOrCreateEntity(scene, actor, level);
        actor.setLookPosition(entity.getEyePosition().add(direction));
        actor.setEntity(entity);

        return true;
    }
}
