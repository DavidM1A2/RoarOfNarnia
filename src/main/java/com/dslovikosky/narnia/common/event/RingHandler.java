package com.dslovikosky.narnia.common.event;

import com.dslovikosky.narnia.common.item.RingItem;
import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

public class RingHandler {
    @SubscribeEvent
    public void onPlayerTickEvent(final PlayerTickEvent.Pre event) {
        final Player player = event.getEntity();
        final Level level = player.level();

        if (level.isClientSide() && Minecraft.getInstance().player != player) {
            return;
        }

        final ItemStack heldItem = player.getItemInHand(InteractionHand.MAIN_HAND);
        if (heldItem.getItem() instanceof RingItem ringItem) {
            ringItem.inventoryTick(heldItem, level, player, EquipmentSlot.MAINHAND);
        }
    }
}
