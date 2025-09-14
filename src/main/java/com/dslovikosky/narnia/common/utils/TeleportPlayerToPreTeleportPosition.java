package com.dslovikosky.narnia.common.utils;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.portal.TeleportTransition;
import net.minecraft.world.phys.Vec3;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public record TeleportPlayerToPreTeleportPosition(Vec3 preTeleportLocation, float yaw, float pitch) implements TeleportTransition.PostTeleportTransition {
    @Override
    public void onTransition(final Entity entity) {
        if (entity instanceof ServerPlayer) {
            ((ServerPlayer) entity).connection.teleport(preTeleportLocation.x(), preTeleportLocation.y(), preTeleportLocation.z(), yaw, pitch);
        } else {
            entity.setPos(preTeleportLocation.x(), preTeleportLocation.y(), preTeleportLocation.z());
            entity.setXRot(pitch);
            entity.setYRot(yaw);
        }
    }
}
