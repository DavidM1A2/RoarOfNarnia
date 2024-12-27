package com.dslovikosky.narnia.common.model;

import com.dslovikosky.narnia.common.model.chapter.Scene;
import com.dslovikosky.narnia.common.network.packet.SyncNarniaGlobalDataPacket;
import com.dslovikosky.narnia.server.world.saved.NarniaGlobalSavedData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.neoforged.fml.LogicalSide;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.Optional;

public final class NarniaGlobalData {
    private static final NarniaGlobalData CLIENT_INSTANCE = new NarniaGlobalData(LogicalSide.CLIENT);
    private static final NarniaGlobalData SERVER_INSTANCE = new NarniaGlobalData(LogicalSide.SERVER);

    private final LogicalSide logicalSide;
    private Scene activeChapter = null;

    private NarniaGlobalData(final LogicalSide logicalSide) {
        this.logicalSide = logicalSide;
    }

    public static NarniaGlobalData getInstance(final Level level) {
        return getInstance(level.isClientSide());
    }

    public static NarniaGlobalData getInstance(final boolean isClientSide) {
        if (isClientSide) {
            return CLIENT_INSTANCE;
        } else {
            return SERVER_INSTANCE;
        }
    }

    public Scene getActiveChapter() {
        return this.activeChapter;
    }

    public void setActiveChapter(final Scene activeChapter) {
        this.activeChapter = activeChapter;
    }

    public void syncAll() {
        PacketDistributor.sendToAllPlayers(new SyncNarniaGlobalDataPacket(Optional.ofNullable(activeChapter)));
    }

    public void syncTo(final ServerPlayer player) {
        PacketDistributor.sendToPlayer(player, new SyncNarniaGlobalDataPacket(Optional.ofNullable(activeChapter)));
    }

    public void markDirty() {
        if (logicalSide == LogicalSide.SERVER) {
            NarniaGlobalSavedData.get().setDirty();
        }
    }
}
