package com.dslovikosky.narnia.common.item;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class DebugItem extends Item {
    public DebugItem() {
        super(new Properties().stacksTo(1));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(final Level pLevel, final Player pPlayer, final InteractionHand pUsedHand) {
        return super.use(pLevel, pPlayer, pUsedHand);
    }
}
