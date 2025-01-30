package com.dslovikosky.narnia.client.proxy;

import com.dslovikosky.narnia.client.gui.screen.EditPositionalMarkerScreen;
import com.dslovikosky.narnia.client.gui.screen.TheChroniclesOfNarniaBookScreen;
import com.dslovikosky.narnia.common.model.scene.Book;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;

/**
 * Use this to avoid classloading client-only classes like "Minecraft" or "Screen" server side
 */
public class ClientProxy {
    private ClientProxy() {
    }

    public static void openChroniclesOfNarniaBookScreen(final Book book) {
        Minecraft.getInstance().setScreen(new TheChroniclesOfNarniaBookScreen(book));
    }

    public static void openEditPositionalMarkerScreen(final BlockPos pos, final String name, final Vec3 offset) {
        Minecraft.getInstance().setScreen(new EditPositionalMarkerScreen(pos, name, offset));
    }

    public static void refreshChroniclesOfNarniaBookScreen() {
        final Screen screen = Minecraft.getInstance().screen;
        if (screen instanceof TheChroniclesOfNarniaBookScreen bookScreen) {
            bookScreen.refreshPages();
        }
    }
}
