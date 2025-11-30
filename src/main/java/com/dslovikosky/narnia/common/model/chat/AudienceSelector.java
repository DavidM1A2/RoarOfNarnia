package com.dslovikosky.narnia.common.model.chat;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;

import java.util.List;

@FunctionalInterface
public interface AudienceSelector {
    List<ServerPlayer> select(Entity npc, ServerLevel level);
}