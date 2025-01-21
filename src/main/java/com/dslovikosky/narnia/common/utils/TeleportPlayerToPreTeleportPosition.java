package com.dslovikosky.narnia.common.utils;

import com.dslovikosky.narnia.common.model.PreTeleportLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.portal.DimensionTransition;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public record TeleportPlayerToPreTeleportPosition(PreTeleportLocation preTeleportLocation) implements DimensionTransition.PostDimensionTransition {
    @Override
    public void onTransition(final Entity entity) {
        if (entity instanceof ServerPlayer) {
            ((ServerPlayer) entity).connection.teleport(preTeleportLocation.posX(), preTeleportLocation.posY(), preTeleportLocation.posZ(), 0f, 0f);
        } else {
            entity.setPos(preTeleportLocation.posX(), preTeleportLocation.posY(), preTeleportLocation.posZ());
        }
    }
}
