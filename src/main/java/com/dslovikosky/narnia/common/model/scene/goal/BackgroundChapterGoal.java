package com.dslovikosky.narnia.common.model.scene.goal;

import com.dslovikosky.narnia.common.model.scene.Scene;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;

public abstract class BackgroundChapterGoal extends ChapterGoal {
    @Override
    public Component getDescription(Scene scene, Level level) {
        return Component.literal("");
    }
}
