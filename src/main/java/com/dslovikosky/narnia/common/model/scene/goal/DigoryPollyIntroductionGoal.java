package com.dslovikosky.narnia.common.model.scene.goal;

import com.dslovikosky.narnia.common.constants.ModCharacters;
import com.dslovikosky.narnia.common.constants.ModDataComponentTypes;
import com.dslovikosky.narnia.common.model.scene.Character;
import com.dslovikosky.narnia.common.model.scene.GoalTickResult;
import com.dslovikosky.narnia.common.model.scene.Scene;
import com.dslovikosky.narnia.common.model.scene.goal.base.ChapterGoal;
import com.dslovikosky.narnia.common.world.schematic.StructureRelativeCoordinate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.structure.BoundingBox;

public class DigoryPollyIntroductionGoal extends ChapterGoal {
    @Override
    public boolean start(Scene scene, Level level) {
        if (level.isClientSide()) {
            return false;
        }

        if (level.dimension() != Level.OVERWORLD) {
            return false;
        }

        ensureActorsExist(scene, level);
        return super.start(scene, level);
    }

    @Override
    protected void initActorEntity(final Scene scene, final Character character, final Entity entity) {
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
    }

    @Override
    public GoalTickResult tick(Scene scene, Level level) {
        if (level.getGameTime() % 600 == 0) {
            return GoalTickResult.COMPLETED;
        }
        return GoalTickResult.CONTINUE;
    }

    @Override
    public Component getDescription(Scene scene, Level level) {
        return Component.literal("Digory meets Polly");
    }
}
