package com.dslovikosky.narnia.common.event;

import com.dslovikosky.narnia.common.constants.ModAttachmentTypes;
import com.dslovikosky.narnia.common.constants.ModItems;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

public class GivePlayerMagiciansNephewHandler {
    @SubscribeEvent
    public void onPlayerLoggedInEvent(final PlayerEvent.PlayerLoggedInEvent event) {
        final Player player = event.getEntity();
        final Level level = player.level();
        if (!level.isClientSide()) {
            if (!player.getData(ModAttachmentTypes.WAS_GIVEN_THE_MAGICIANS_NEPHEW)) {
                player.setData(ModAttachmentTypes.WAS_GIVEN_THE_MAGICIANS_NEPHEW, true);
                player.getInventory().add(new ItemStack(ModItems.THE_MAGICIANS_NEPHEW.get()));
            }
        }
    }
}
