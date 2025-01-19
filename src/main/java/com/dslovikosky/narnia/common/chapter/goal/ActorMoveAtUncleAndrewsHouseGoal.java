package com.dslovikosky.narnia.common.chapter.goal;

import com.dslovikosky.narnia.common.constants.ModDataComponentTypes;
import com.dslovikosky.narnia.common.model.scene.Actor;
import com.dslovikosky.narnia.common.model.scene.Character;
import com.dslovikosky.narnia.common.model.scene.GoalTickResult;
import com.dslovikosky.narnia.common.model.scene.Scene;
import com.dslovikosky.narnia.common.model.scene.goal.ActorMoveChapterGoal;
import com.dslovikosky.narnia.common.world.schematic.StructureRelativeCoordinate;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.Optional;

public class ActorMoveAtUncleAndrewsHouseGoal extends ActorMoveChapterGoal {
    public ActorMoveAtUncleAndrewsHouseGoal(final DeferredHolder<Character, ? extends Character> character, final Vec3 position) {
        super(character, position);
    }

    @Override
    public boolean start(Scene scene, Level level) {
        if (level.dimension() != Level.OVERWORLD) {
            return false;
        }

        final BoundingBox boundingBox = scene.get(ModDataComponentTypes.UNCLE_ANDREWS_HOUSE_BB);
        final Direction houseDirection = scene.get(ModDataComponentTypes.UNCLE_ANDREWS_HOUSE_DIRECTION);
        final StructureRelativeCoordinate coord = new StructureRelativeCoordinate(boundingBox, houseDirection);
        final Vec3 newPosition = coord.relativeToAbsolutePos(position);

        scene.getChapter().getActor(scene, character.get()).setTargetPosition(newPosition);

        return true;
    }

    @Override
    public GoalTickResult tick(Scene scene, Level level) {
        final Actor actor = scene.getChapter().getActor(scene, character.get());
        final Optional<LivingEntity> entity = actor.getCharacter().getEntity(actor, level);

        if (entity.isPresent()) {
            final Vec3 currentPosition = entity.get().position();
            final BoundingBox boundingBox = scene.get(ModDataComponentTypes.UNCLE_ANDREWS_HOUSE_BB);
            final Direction houseDirection = scene.get(ModDataComponentTypes.UNCLE_ANDREWS_HOUSE_DIRECTION);
            final StructureRelativeCoordinate coord = new StructureRelativeCoordinate(boundingBox, houseDirection);
            final Vec3 newPosition = coord.relativeToAbsolutePos(position);
            if (currentPosition.distanceTo(newPosition) < 2) {
                return GoalTickResult.COMPLETED;
            }
        }

        return GoalTickResult.CONTINUE;
    }
}
