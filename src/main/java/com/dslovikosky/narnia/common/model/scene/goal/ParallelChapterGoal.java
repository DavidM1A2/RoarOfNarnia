package com.dslovikosky.narnia.common.model.scene.goal;

import com.dslovikosky.narnia.common.constants.ModDataComponentTypes;
import com.dslovikosky.narnia.common.model.ChapterGoalStatus;
import com.dslovikosky.narnia.common.model.scene.GoalTickResult;
import com.dslovikosky.narnia.common.model.scene.Scene;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ParallelChapterGoal extends ChapterGoal {
    private final ChapterGoal[] goals;

    public ParallelChapterGoal(final ChapterGoal... goals) {
        this.goals = goals;
    }

    @Override
    public boolean start(Scene scene, Level level) {
        if (!scene.has(ModDataComponentTypes.CHAPTER_GOAL_STATUS_LIST)) {
            scene.set(ModDataComponentTypes.CHAPTER_GOAL_STATUS_LIST, Arrays.stream(goals)
                    .map(goal -> ChapterGoalStatus.NEW).collect(Collectors.toCollection(ArrayList::new)));
        }
        final List<ChapterGoalStatus> statuses = scene.get(ModDataComponentTypes.CHAPTER_GOAL_STATUS_LIST);
        for (int i = 0; i < goals.length; i++) {
            final ChapterGoalStatus chapterGoalStatus = statuses.get(i);
            if (chapterGoalStatus == ChapterGoalStatus.NEW) {
                final ChapterGoal goal = goals[i];
                if (goal.start(scene, level)) {
                    statuses.set(i, ChapterGoalStatus.STARTED);
                    scene.set(ModDataComponentTypes.CHAPTER_GOAL_STATUS_LIST, statuses);
                }
            }
        }
        return statuses.stream().allMatch(status -> status == ChapterGoalStatus.STARTED);
    }

    @Override
    public GoalTickResult tick(Scene scene, Level level) {
        boolean sync = false;
        final List<ChapterGoalStatus> statuses = scene.get(ModDataComponentTypes.CHAPTER_GOAL_STATUS_LIST);
        for (int i = 0; i < goals.length; i++) {
            ChapterGoal goal = goals[i];
            final ChapterGoalStatus chapterGoalStatus = statuses.get(i);
            if (chapterGoalStatus == ChapterGoalStatus.STARTED) {
                final GoalTickResult result = goal.tick(scene, level);
                if (result == GoalTickResult.COMPLETED) {
                    statuses.set(i, ChapterGoalStatus.FINISHED);
                    scene.set(ModDataComponentTypes.CHAPTER_GOAL_STATUS_LIST, statuses);
                } else if (result == GoalTickResult.CONTINUE_SYNC) {
                    sync = true;
                }
            }
        }
        if (sync) {
            return GoalTickResult.CONTINUE_SYNC;
        } else if (statuses.stream().allMatch(status -> status == ChapterGoalStatus.FINISHED)) {
            return GoalTickResult.COMPLETED;
        } else {
            return GoalTickResult.CONTINUE;
        }
    }

    @Override
    public void finish(Scene scene, Level level) {
        for (final ChapterGoal goal : goals) {
            goal.finish(scene, level);
        }
        scene.remove(ModDataComponentTypes.CHAPTER_GOAL_STATUS_LIST);
    }

    @Override
    public void registerComponents(DataComponentMap.Builder builder) {
        for (final ChapterGoal goal : goals) {
            goal.registerComponents(builder);
        }
        builder.set(ModDataComponentTypes.CHAPTER_GOAL_STATUS_LIST, Arrays.stream(goals)
                .map(goal -> ChapterGoalStatus.NEW).collect(Collectors.toCollection(ArrayList::new)));
    }

    @Override
    public Component getDescription(Scene scene, Level level) {
        return goals[0].getDescription(scene, level);
    }
}
