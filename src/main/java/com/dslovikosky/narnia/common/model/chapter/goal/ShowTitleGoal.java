package com.dslovikosky.narnia.common.model.chapter.goal;

import com.dslovikosky.narnia.common.constants.ModDataComponentTypes;
import com.dslovikosky.narnia.common.model.chapter.ChapterGoal;
import com.dslovikosky.narnia.common.model.chapter.GoalTickResult;
import com.dslovikosky.narnia.common.model.chapter.Scene;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.world.level.Level;

import java.time.Duration;

public class ShowTitleGoal implements ChapterGoal {
    private static final long TITLE_SHOW_TIME = Duration.ofSeconds(5).toMillis();

    @Override
    public boolean start(Scene scene, Level level) {
        scene.set(ModDataComponentTypes.TITLE_END_TIME, System.currentTimeMillis() + TITLE_SHOW_TIME);
        return true;
    }

    @Override
    public GoalTickResult tick(final Scene scene, final Level level) {
        final Long endTime = scene.get(ModDataComponentTypes.TITLE_END_TIME);
        if (endTime != null && endTime > System.currentTimeMillis()) {
            return GoalTickResult.CONTINUE;
        }
        return GoalTickResult.COMPLETED;
    }

    @Override
    public void finish(final Scene scene, final Level level) {
        scene.remove(ModDataComponentTypes.TITLE_END_TIME);
    }

    @Override
    public void registerComponents(DataComponentMap.Builder builder) {
        builder.set(ModDataComponentTypes.TITLE_END_TIME, 0L);
    }
}
