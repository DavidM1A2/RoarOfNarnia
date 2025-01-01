package com.dslovikosky.narnia.common.model.chapter;

import net.minecraft.core.component.DataComponentMap;
import net.minecraft.world.level.Level;

public interface ChapterGoal {
    default boolean start(final Scene scene, final Level level) {
        return true;
    }

    GoalTickResult tick(final Scene scene, final Level level);

    default void finish(final Scene scene, final Level level) {
    }

    default void registerComponents(final DataComponentMap.Builder builder) {
    }
}
