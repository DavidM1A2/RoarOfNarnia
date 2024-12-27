package com.dslovikosky.narnia.common.model.chapter;

import net.minecraft.world.level.Level;

public interface ChapterGoal {
    void onStart(final Level level, final Scene scene);

    void tick(final Level level, final Scene scene);

    void onFinish(final Level level, final Scene scene);
}
