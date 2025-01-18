package com.dslovikosky.narnia.common.chapter.goal;

import com.dslovikosky.narnia.common.constants.ModCharacters;
import com.dslovikosky.narnia.common.constants.ModDataComponentTypes;
import com.dslovikosky.narnia.common.model.scene.Chapter;
import com.dslovikosky.narnia.common.model.scene.GoalTickResult;
import com.dslovikosky.narnia.common.model.scene.Scene;
import com.dslovikosky.narnia.common.model.scene.goal.ChapterGoal;
import com.dslovikosky.narnia.common.world.schematic.StructureRelativeCoordinate;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.phys.Vec3;

public class DigoryPollyIntroductionGoal extends ChapterGoal {
    @Override
    public boolean start(Scene scene, Level level) {
        if (level.dimension() != Level.OVERWORLD) {
            return false;
        }

        final BoundingBox boundingBox = scene.get(ModDataComponentTypes.UNCLE_ANDREWS_HOUSE_BB);
        final Direction direction = scene.get(ModDataComponentTypes.UNCLE_ANDREWS_HOUSE_DIRECTION);
        final StructureRelativeCoordinate coord = new StructureRelativeCoordinate(boundingBox, direction);
        final Chapter chapter = scene.getChapter();
        chapter.getActor(scene, ModCharacters.DIGORY.get()).setTargetPosition(Vec3.atCenterOf(coord.relativeToAbsolutePos(10, 1, 20)));
        chapter.getActor(scene, ModCharacters.POLLY.get()).setTargetPosition(Vec3.atCenterOf(coord.relativeToAbsolutePos(16, 1, 20)));

        return super.start(scene, level);
    }

    @Override
    public GoalTickResult tick(Scene scene, Level level) {
        return GoalTickResult.COMPLETED;
    }

    @Override
    public Component getDescription(Scene scene, Level level) {
        return Component.literal("Digory meets Polly");
    }
}
