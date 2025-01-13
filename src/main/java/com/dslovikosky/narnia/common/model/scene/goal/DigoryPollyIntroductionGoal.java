package com.dslovikosky.narnia.common.model.scene.goal;

import com.dslovikosky.narnia.common.model.scene.GoalTickResult;
import com.dslovikosky.narnia.common.model.scene.Scene;
import com.dslovikosky.narnia.common.model.scene.goal.base.ChapterGoal;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;

public class DigoryPollyIntroductionGoal extends ChapterGoal {
    @Override
    public boolean start(Scene scene, Level level) {
        if (level.isClientSide()) {
            return false;
        }

        if (level.dimension() != Level.OVERWORLD) {
            return false;
        }

        /*
        final BoundingBox boundingBox = scene.get(ModDataComponentTypes.UNCLE_ANDREWS_HOUSE_BB);
        final Direction direction = scene.get(ModDataComponentTypes.UNCLE_ANDREWS_HOUSE_DIRECTION);
        final StructureRelativeCoordinate coord = new StructureRelativeCoordinate(boundingBox, direction);
        if (character == ModCharacters.DIGORY.get()) {
            final BlockPos blockPos = coord.relativeToAbsolutePos(10, 1, 20);
            entity.setPos(blockPos.getX(), blockPos.getY(), blockPos.getZ());
        } else if (character == ModCharacters.POLLY.get()) {
            final BlockPos blockPos = coord.relativeToAbsolutePos(16, 1, 20);
            entity.setPos(blockPos.getX(), blockPos.getY(), blockPos.getZ());
        }
         */

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
