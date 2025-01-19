package com.dslovikosky.narnia.common.chapter.goal;

import com.dslovikosky.narnia.common.constants.ModDataComponentTypes;
import com.dslovikosky.narnia.common.model.scene.Actor;
import com.dslovikosky.narnia.common.model.scene.Chapter;
import com.dslovikosky.narnia.common.model.scene.Character;
import com.dslovikosky.narnia.common.model.scene.Scene;
import com.dslovikosky.narnia.common.model.scene.goal.InstantiateActorChapterGoal;
import com.dslovikosky.narnia.common.world.schematic.StructureRelativeCoordinate;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.registries.DeferredHolder;

public class InstantiateAtUncleAndrewsHouseGoal extends InstantiateActorChapterGoal {
    public InstantiateAtUncleAndrewsHouseGoal(final DeferredHolder<Character, ? extends Character> character, final Vec3 offsetPosition, final Vec3 direction) {
        super(character, offsetPosition, direction);
    }

    @Override
    public boolean start(final Scene scene, final Level level) {
        if (level.dimension() != Level.OVERWORLD) {
            return false;
        }

        final BoundingBox boundingBox = scene.get(ModDataComponentTypes.UNCLE_ANDREWS_HOUSE_BB);
        final Direction houseDirection = scene.get(ModDataComponentTypes.UNCLE_ANDREWS_HOUSE_DIRECTION);
        final StructureRelativeCoordinate coord = new StructureRelativeCoordinate(boundingBox, houseDirection);
        final Chapter chapter = scene.getChapter();
        final Actor actor = chapter.getActor(scene, character.get());
        final Vec3 newPosition = coord.relativeToAbsolutePos(position);
        actor.setTargetPosition(newPosition);
        actor.getCharacter().getOrCreateEntity(scene, actor, level);

        return true;
    }
}
