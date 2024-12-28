package com.dslovikosky.narnia.common.item;

import com.dslovikosky.narnia.client.proxy.ClientProxy;
import com.dslovikosky.narnia.common.model.chapter.Book;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.registries.DeferredHolder;

public class TheChroniclesOfNarniaBookItem extends Item {
    private final DeferredHolder<Book, ? extends Book> book;

    public TheChroniclesOfNarniaBookItem(final DeferredHolder<Book, ? extends Book> book) {
        super(new Properties());
        this.book = book;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(final Level pLevel, final Player pPlayer, final InteractionHand pUsedHand) {
        if (pLevel.isClientSide()) {
            ClientProxy.openChroniclesOfNarniaBookScreen(book.get());
        }
        return super.use(pLevel, pPlayer, pUsedHand);
    }
}
