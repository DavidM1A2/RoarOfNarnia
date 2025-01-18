package com.dslovikosky.narnia.common.model.scene.goal;

import com.dslovikosky.narnia.common.model.scene.Actor;
import com.dslovikosky.narnia.common.model.scene.Chapter;
import com.dslovikosky.narnia.common.model.scene.Character;
import com.dslovikosky.narnia.common.model.scene.Scene;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.registries.DeferredHolder;

public class InstantiateActorChapterGoal extends BackgroundChapterGoal {
    protected final DeferredHolder<Character, ? extends Character> character;
    protected final Vec3 position;
    protected final Vec3 direction;

    public InstantiateActorChapterGoal(final DeferredHolder<Character, ? extends Character> character, final Vec3 position, final Vec3 direction) {
        this.character = character;
        this.position = position;
        this.direction = direction;
    }

    @Override
    public boolean start(Scene scene, Level level) {
        if (level.dimension() != Level.OVERWORLD) {
            return false;
        }

        final Chapter chapter = scene.getChapter();
        final Actor actor = chapter.getActor(scene, character.get());
        actor.setTargetPosition(position);
        actor.setLookPosition(position.add(direction));
        actor.setEntity(actor.getCharacter().getOrCreateEntity(scene, actor, level));

        return true;
    }
}
