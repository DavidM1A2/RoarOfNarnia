package com.dslovikosky.narnia.common.item;

import com.dslovikosky.narnia.client.gui.screen.TheChroniclesOfNarniaBookScreen;
import com.dslovikosky.narnia.common.model.chapter.NarniaBook;
import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class TheChroniclesOfNarniaBookItem extends Item {
    private final NarniaBook book;

    public TheChroniclesOfNarniaBookItem(final NarniaBook book) {
        super(new Properties());
        this.book = book;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(final Level pLevel, final Player pPlayer, final InteractionHand pUsedHand) {
        if (pLevel.isClientSide()) {
            Minecraft.getInstance().setScreen(new TheChroniclesOfNarniaBookScreen(book));
        }
        return super.use(pLevel, pPlayer, pUsedHand);
    }
}
