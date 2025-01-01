package com.dslovikosky.narnia.common.event;

import com.dslovikosky.narnia.common.model.NarniaGlobalData;
import com.dslovikosky.narnia.common.model.chapter.Chapter;
import com.dslovikosky.narnia.common.model.chapter.ChapterGoal;
import com.dslovikosky.narnia.common.model.chapter.Scene;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;

import java.util.Optional;

public class ActiveSceneHandler {
    @SubscribeEvent
    public void onPostLevelTick(final LevelTickEvent.Post event) {
        final Level level = event.getLevel();

        final NarniaGlobalData data = NarniaGlobalData.getInstance(level);
        final Scene activeScene = data.getActiveScene();
        if (activeScene == null) {
            return;
        }
        final Chapter chapter = activeScene.getChapter();
        boolean needsSync = false;

        if (!activeScene.isGoalStarted()) {
            Optional<ChapterGoal> currentGoal = chapter.getCurrentGoal(activeScene);
            if (currentGoal.isPresent()) {
                final boolean started = currentGoal.get().start(activeScene, level);
                if (started) {
                    activeScene.setGoalStarted(true);
                    needsSync = true;
                } else {
                    // If there's no goal to start no-op
                    return;
                }
            }
        }

        needsSync = needsSync || chapter.tick(activeScene, level);

        if (chapter.isComplete(activeScene)) {
            chapter.stop(activeScene, level, false);
            data.setActiveScene(null);
            needsSync = true;
        }

        if (!level.isClientSide() && needsSync) {
            data.markDirty();
            data.syncAll();
        }
    }
}
