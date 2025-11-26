package com.dslovikosky.narnia.client.proxy;

import com.dslovikosky.narnia.client.event.CharnScreenShakeHandler;

/**
 * Use this to avoid classloading client-only classes like "Minecraft" or "Screen" server side
 */
public class ClientProxy {
    public static final CharnScreenShakeHandler CHARN_SCREEN_SHAKE_HANDLER = new CharnScreenShakeHandler();

    private ClientProxy() {
    }
}
