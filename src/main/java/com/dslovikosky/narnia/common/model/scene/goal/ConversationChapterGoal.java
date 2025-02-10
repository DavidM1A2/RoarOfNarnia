package com.dslovikosky.narnia.common.model.scene.goal;

import com.dslovikosky.narnia.common.constants.ModDataComponentTypes;
import com.dslovikosky.narnia.common.model.scene.Chapter;
import com.dslovikosky.narnia.common.model.scene.Character;
import com.dslovikosky.narnia.common.model.scene.GoalTickResult;
import com.dslovikosky.narnia.common.model.scene.Scene;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;

import java.util.HashMap;
import java.util.Map;

public class ConversationChapterGoal extends ChapterGoal {
    private final Component description;
    private final Map<Long, ChatLine> elapsedTimeToChatLine;
    private final long durationTicks;

    public ConversationChapterGoal(final Component description, final ChatLine... chatLines) {
        this.description = description;
        this.elapsedTimeToChatLine = new HashMap<>();
        long ticksElapsed = 1;
        for (final ChatLine chatLine : chatLines) {
            elapsedTimeToChatLine.put(ticksElapsed, chatLine);
            ticksElapsed = ticksElapsed + chatLine.getDurationTicks();
        }
        this.durationTicks = ticksElapsed;
    }

    @Override
    public boolean start(Scene scene, Level level) {
        scene.set(ModDataComponentTypes.CONVERSATION_START_TIME, level.getGameTime());
        return true;
    }

    @Override
    public GoalTickResult tick(Scene scene, Level level) {
        final long conversationStartTime = scene.getOrDefault(ModDataComponentTypes.CONVERSATION_START_TIME, 0L);
        final long elapsedTime = level.getGameTime() - conversationStartTime;

        if (elapsedTime > durationTicks) {
            return GoalTickResult.COMPLETED;
        }

        if (elapsedTimeToChatLine.containsKey(elapsedTime)) {
            final ChatLine chatLine = elapsedTimeToChatLine.get(elapsedTime);
            final Character speaker = chatLine.getSpeaker();
            final Character listener = chatLine.getListener();
            final Chapter chapter = scene.getChapter();
            chapter.getPlayers(scene, level).forEach(player -> player.sendSystemMessage(
                    Component.translatable("chat.conversation.character_speaks", speaker.getName(), chatLine.getComponent())));
            if (listener != null) {
                chapter.getActor(scene, listener).setLookPosition(speaker.getOrCreateEntity(scene, chapter.getActor(scene, speaker), level).getEyePosition());
                chapter.getActor(scene, speaker).setLookPosition(listener.getOrCreateEntity(scene, chapter.getActor(scene, listener), level).getEyePosition());
            }
        }

        return GoalTickResult.CONTINUE;

    }

    @Override
    public void finish(Scene scene, Level level) {
        scene.remove(ModDataComponentTypes.CONVERSATION_START_TIME);
    }

    @Override
    public Component getDescription(Scene scene, Level level) {
        return description;
    }

    @Override
    public void registerComponents(DataComponentMap.Builder builder) {
        builder.set(ModDataComponentTypes.CONVERSATION_START_TIME, 0L);
    }
}
