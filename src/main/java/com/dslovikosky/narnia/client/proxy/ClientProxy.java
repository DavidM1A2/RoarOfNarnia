package com.dslovikosky.narnia.client.proxy;

import com.dslovikosky.narnia.client.gui.screen.TheChroniclesOfNarniaBookScreen;
import com.dslovikosky.narnia.common.model.chapter.Book;
import net.minecraft.client.Minecraft;

/**
 * Use this to avoid classloading client-only classes like "Minecraft" or "Screen" server side
 */
public class ClientProxy {
    private ClientProxy() {
    }

    public static void openChroniclesOfNarniaBookScreen(final Book book) {
        Minecraft.getInstance().setScreen(new TheChroniclesOfNarniaBookScreen(book));
    }
}
