package com.dslovikosky.narnia.client.renderer;

import com.dslovikosky.narnia.common.constants.Constants;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.client.renderer.state.LevelRenderState;
import net.minecraft.client.renderer.state.SkyRenderState;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class CharnSkyRenderer extends DimensionSpecialEffects {
    private static final ResourceLocation SUN_TEXTURE = Constants.modLocation("textures/environment/red_sun.png");

    public CharnSkyRenderer() {
        super(SkyType.OVERWORLD, false, false);
    }

    @Override
    public boolean renderSky(LevelRenderState levelRenderState, SkyRenderState skyRenderState, Matrix4f modelViewMatrix, Runnable setupFog) {
        return true;
    }

    @Override
    public Vec3 getBrightnessDependentFogColor(Vec3 fogColor, float brightness) {
        return fogColor;
    }

    @Override
    public boolean isFoggyAt(int x, int y) {
        return false;
    }
}
