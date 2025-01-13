package com.dslovikosky.narnia.common.model.scene.goal.base;

import com.dslovikosky.narnia.common.model.scene.GoalTickResult;
import com.dslovikosky.narnia.common.model.scene.Scene;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;

public abstract class ChapterGoal {
    public ChapterGoal() {
    }

    public boolean start(final Scene scene, final Level level) {
        return true;
    }

    public GoalTickResult tick(final Scene scene, final Level level) {
        return GoalTickResult.COMPLETED;
    }

    public void finish(final Scene scene, final Level level) {
    }

    public void registerComponents(final DataComponentMap.Builder builder) {
    }

    public abstract Component getDescription(final Scene scene, final Level level);
}
