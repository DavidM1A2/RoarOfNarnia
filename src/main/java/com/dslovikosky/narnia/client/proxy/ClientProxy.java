package com.dslovikosky.narnia.client.proxy;

import com.dslovikosky.narnia.client.event.CharnScreenShakeHandler;
import com.dslovikosky.narnia.client.gui.screen.SpellwrightingScreen;
import net.minecraft.client.Minecraft;

/**
 * Use this to avoid classloading client-only classes like "Minecraft" or "Screen" server side
 */
public class ClientProxy {
    public static final CharnScreenShakeHandler CHARN_SCREEN_SHAKE_HANDLER = new CharnScreenShakeHandler();

    private ClientProxy() {
    }

    public static void openSpellwrightingScreen() {
        Minecraft.getInstance().gui.setScreen(new SpellwrightingScreen());
    }
}
