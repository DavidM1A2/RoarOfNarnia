package com.dslovikosky.narnia.common.event;

import com.dslovikosky.narnia.common.model.NarniaGlobalData;
import com.dslovikosky.narnia.common.model.scene.Chapter;
import com.dslovikosky.narnia.common.model.scene.GoalTickResult;
import com.dslovikosky.narnia.common.model.scene.Scene;
import com.dslovikosky.narnia.common.model.scene.SceneState;
import com.dslovikosky.narnia.common.model.scene.goal.base.ChapterGoal;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;

import java.util.Optional;

public class ActiveSceneHandler {
    @SubscribeEvent
    public void onPostLevelTick(final LevelTickEvent.Post event) {
        final Level level = event.getLevel();
        if (level.isClientSide()) {
            return;
        }

        final NarniaGlobalData data = NarniaGlobalData.getInstance(level);
        final Scene activeScene = data.getActiveScene();
        if (activeScene == null) {
            return;
        }
        final Chapter chapter = activeScene.getChapter();
        final SceneState state = activeScene.getState();

        boolean needsSync = false;

        if (state == SceneState.NEW) {
            chapter.start(activeScene, level);
            activeScene.setState(SceneState.STARTED);
            needsSync = true;
        } else if (state == SceneState.STARTED || state == SceneState.GOAL_FINISHED) {
            chapter.tick(activeScene, level);

            final Optional<ChapterGoal> currentGoal = chapter.getCurrentGoal(activeScene);
            if (currentGoal.isPresent()) {
                final boolean started = currentGoal.get().start(activeScene, level);
                if (started) {
                    activeScene.setState(SceneState.GOAL_STARTED);
                    needsSync = true;
                }
            } else {
                chapter.stop(activeScene, level, false);
                activeScene.setState(SceneState.FINISHED);
                data.setActiveScene(null);
                needsSync = true;
            }
        } else if (state == SceneState.GOAL_STARTED) {
            chapter.tick(activeScene, level);

            final Optional<ChapterGoal> currentGoalOpt = chapter.getCurrentGoal(activeScene);
            if (currentGoalOpt.isPresent()) {
                final ChapterGoal currentGoal = currentGoalOpt.get();
                final GoalTickResult result = currentGoal.tick(activeScene, level);
                if (result == GoalTickResult.COMPLETED) {
                    currentGoal.finish(activeScene, level);
                    activeScene.setGoalIndex(activeScene.getGoalIndex() + 1);
                    activeScene.setState(SceneState.GOAL_FINISHED);
                    needsSync = true;
                } else {
                    needsSync = result == GoalTickResult.CONTINUE_SYNC;
                }
            }
        }

        if (!level.isClientSide() && needsSync) {
            data.markDirty();
            data.syncAll();
        }
    }
}
