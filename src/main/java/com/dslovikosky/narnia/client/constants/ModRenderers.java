package com.dslovikosky.narnia.client.constants;

import com.dslovikosky.narnia.client.renderer.CharnSkyRenderer;
import com.dslovikosky.narnia.client.renderer.DrowsyVignetteRenderer;
import com.dslovikosky.narnia.common.constants.Constants;
import net.minecraft.resources.ResourceLocation;

public class ModRenderers {
    public static final ResourceLocation DROWSY_VIGNETTE_ID = Constants.modLocation("drowsy_vignette");
    public static final ResourceLocation CHARN_SKY_RENDERER_ID = Constants.modLocation("charn_sky");

    public static final class Client {
        public static final CharnSkyRenderer CHARN_SKY_RENDERER = new CharnSkyRenderer();
        public static final DrowsyVignetteRenderer DROWSY_VIGNETTE = new DrowsyVignetteRenderer();
    }
}
