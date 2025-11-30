package com.dslovikosky.narnia.common.model.chat;

import com.dslovikosky.narnia.common.constants.Constants;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.UnaryOperator;

public class EntityChatEngine {
    private final Entity entity;
    private final String npcId;
    private final ChatFormatting nameColor;

    private final Map<String, ChatSequence> sequences = new HashMap<>();

    private boolean playing = false;
    private String currentSequence = null;
    private int currentLine = 0;
    private int ticksOnThisLine = 0;

    public EntityChatEngine(final Entity entity, final String npcId, final ChatFormatting nameColor) {
        this.entity = entity;
        this.npcId = npcId;
        this.nameColor = nameColor;
    }

    public void defineSequence(final String name, final UnaryOperator<ChatSequence> builder) {
        sequences.put(name, builder.apply(new ChatSequence()));
    }

    public void playSequence(final String name) {
        final ChatSequence chatSequence = sequences.get(name);
        if (chatSequence == null) {
            return;
        }

        playing = true;
        currentSequence = name;
        currentLine = 0;
        ticksOnThisLine = 0;
    }

    public void stop() {
        playing = false;
        currentSequence = null;
        currentLine = 0;
        ticksOnThisLine = 0;
    }

    public void tick() {
        if (!playing || entity.level().isClientSide()) {
            return;
        }

        final ChatSequence chatSequence = sequences.get(currentSequence);
        if (chatSequence == null) {
            stop();
            return;
        }

        if (currentLine >= chatSequence.getLines().size()) {
            stop();
            return;
        }

        if (ticksOnThisLine == 0) {
            final Component line = loadLine(currentSequence, currentLine + 1);
            sendToAudience(chatSequence.getAudienceForLine(currentLine), line);
        }

        ticksOnThisLine++;

        final int requiredTicks = chatSequence.getLines().get(currentLine).getDurationTicks();
        if (ticksOnThisLine < requiredTicks) {
            return;
        }

        ticksOnThisLine = 0;
        currentLine++;
    }

    public void save(ValueOutput out) {
        out.putBoolean("ChatPlaying", playing);

        if (!playing) {
            return;
        }

        out.putString("ChatSequence", currentSequence);
        out.putInt("ChatLine", currentLine);
        out.putInt("ChatTicksOnThisLine", ticksOnThisLine);
    }

    public void load(ValueInput in) {
        this.playing = in.getBooleanOr("ChatPlaying", false);

        if (!playing) {
            this.currentSequence = null;
            this.currentLine = 0;
            this.ticksOnThisLine = 0;
            return;
        }

        this.currentSequence = in.getString("ChatSequence").orElse(null);
        this.currentLine = in.getIntOr("ChatLine", 0);
        this.ticksOnThisLine = in.getIntOr("ChatTicksOnThisLine", 0);
        if (currentSequence == null || !sequences.containsKey(currentSequence)) {
            stop();
        }
    }

    private Component loadLine(final String sequence, final int index) {
        final MutableComponent name = Component.translatable("dialog." + Constants.MOD_ID + "." + npcId + ".name").withStyle(nameColor);
        final MutableComponent line = Component.translatable("dialog." + Constants.MOD_ID + "." + npcId + "." + sequence + "." + index).withStyle(ChatFormatting.WHITE);
        return name.append(" ").append(line);
    }

    private void sendToAudience(AudienceSelector selector, Component msg) {
        final ServerLevel level = (ServerLevel) entity.level();
        final List<ServerPlayer> players = selector.select(entity, level);

        for (final ServerPlayer serverPlayer : players) {
            serverPlayer.sendSystemMessage(msg);
        }
    }
}
