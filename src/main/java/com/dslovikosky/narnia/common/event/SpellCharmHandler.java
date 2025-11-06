package com.dslovikosky.narnia.common.event;

import com.dslovikosky.narnia.common.constants.ModAttachmentTypes;
import com.dslovikosky.narnia.common.model.attachment_type.SpellCharmData;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import java.util.UUID;

public class SpellCharmHandler {
    @SubscribeEvent
    public void onPlayerTickEvent(final PlayerTickEvent.Pre event) {
        final Player player = event.getEntity();
        final Level level = player.level();
        if (level.isClientSide()) {
            return;
        }

        final SpellCharmData charmData = player.getData(ModAttachmentTypes.SPELL_CHARM_DATA);

        // Ensure there's at least 1 charm tick remaining
        if (charmData.getCharmTicks() > 0) {
            // Reduce the charm ticks by 1
            final int newCharmTicks = charmData.getCharmTicks() - 1;
            charmData.setCharmTicks(newCharmTicks);
            if (newCharmTicks <= 0) {
                // Kick off manual sync because we're not using setData() to notify client's they're done
                player.syncData(ModAttachmentTypes.SPELL_CHARM_DATA);
            }

            // Force the player to look at the entity
            final UUID charmingEntityId = charmData.getCharmingEntityId();
            final Entity charmingEntity = charmingEntityId == null ? null : level.getEntity(charmingEntityId);

            // If the player is non-null set the player's facing
            if (charmingEntity != null) {
                // A player cant charm themselves
                if (!player.getUUID().equals(charmingEntityId)) {
                    // Set the player's look to be at the charming entity
                    ((ServerPlayer) player).lookAt(EntityAnchorArgument.Anchor.EYES, charmingEntity, EntityAnchorArgument.Anchor.EYES);
                }
            }
        }
    }
}
