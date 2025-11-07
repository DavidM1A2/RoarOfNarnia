package com.dslovikosky.narnia.common.event;

import com.dslovikosky.narnia.common.constants.ModAttachmentTypes;
import com.dslovikosky.narnia.common.model.attachment_type.SpellFreezeData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

public class SpellFreezeHandler {
    @SubscribeEvent
    public void onPlayerTickEventPre(final PlayerTickEvent.Pre event) {
        final Player player = event.getEntity();

        final SpellFreezeData playerFreezeData = player.getData(ModAttachmentTypes.SPELL_FREEZE_DATA);

        // Ensure there's at least 1 freeze tick remaining
        if (playerFreezeData.getFreezeTicks() > 0) {
            // Reduce the freeze ticks by 1 (server side only though)
            if (!player.level().isClientSide()) {
                final int newFreezeTicks = playerFreezeData.getFreezeTicks() - 1;
                playerFreezeData.setFreezeTicks(newFreezeTicks);
                if (newFreezeTicks <= 0) {
                    // Kick off manual sync because we're not using setData() to notify client's they're done
                    player.syncData(ModAttachmentTypes.SPELL_FREEZE_DATA);
                }
            }

            final Vec3 freezePosition = playerFreezeData.getFreezePosition();
            final float yaw = playerFreezeData.getFreezeYaw();
            final float pitch = playerFreezeData.getFreezePitch();

            // Freeze the player's location
            if (player.level().isClientSide()) {
                player.setPos(freezePosition);
                player.setYRot(yaw);
                player.setXRot(pitch);
            } else {
                ((ServerPlayer) player).connection.teleport(
                        freezePosition.x,
                        freezePosition.y,
                        freezePosition.z,
                        yaw,
                        pitch
                );
            }
        }
    }
}
