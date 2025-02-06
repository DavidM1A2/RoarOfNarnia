package com.dslovikosky.narnia.common.chapter.goal;

import com.dslovikosky.narnia.common.constants.ModSchematics;
import com.dslovikosky.narnia.common.model.scene.GoalTickResult;
import com.dslovikosky.narnia.common.model.scene.Scene;
import com.dslovikosky.narnia.common.model.scene.goal.BackgroundChapterGoal;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.structure.BoundingBox;

import java.util.List;

public class WalkToUncleAndrewsHouseGoal extends BackgroundChapterGoal {
    @Override
    public GoalTickResult tick(final Scene scene, final Level level) {
        final List<Player> players = scene.getChapter().getPlayers(scene, level);

        if (players.isEmpty()) {
            return GoalTickResult.CONTINUE;
        }

        final BoundingBox houseBoundingBox = BoundingBox.fromCorners(new BlockPos(0, 64, 10),
                new BlockPos(ModSchematics.UNCLE_ANDREWS_HOUSE.get().getWidth() - 1,
                        64 + ModSchematics.UNCLE_ANDREWS_HOUSE.get().getHeight() - 1,
                        ModSchematics.UNCLE_ANDREWS_HOUSE.get().getLength() - 1));
        if (players.stream().allMatch(playerEntity -> houseBoundingBox.isInside(playerEntity.blockPosition()))) {
            return GoalTickResult.COMPLETED;
        }

        return GoalTickResult.CONTINUE;
    }

    @Override
    public Component getDescription(Scene scene, Level level) {
        return Component.literal("Walk in to Uncle Andrew's house yard");
    }
}
