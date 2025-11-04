package com.dslovikosky.narnia.client.particle.base;

import com.dslovikosky.narnia.client.constants.ModRenderPipelines;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.texture.TextureAtlas;
import org.jetbrains.annotations.NotNull;

public abstract class NarniaParticle extends SingleQuadParticle {
    protected static final Layer TRANSLUCENT_NO_DEPTH = new Layer(true, TextureAtlas.LOCATION_PARTICLES, ModRenderPipelines.TRANSLUCENT_PARTICLE_NO_DEPTH);

    public NarniaParticle(final ClientLevel clientLevel, final double x, final double y, final double z, final double xSpeed, final double ySpeed, final double zSpeed, final SpriteSet spriteSet) {
        super(clientLevel, x, y, z, xSpeed, ySpeed, zSpeed, spriteSet.first());
        quadSize = 0.2f;
        xd = xSpeed;
        yd = ySpeed;
        zd = zSpeed;
    }

    @Override
    protected @NotNull Layer getLayer() {
        return Layer.TRANSLUCENT;
    }

    @Override
    public void tick() {
        // Update the previous positions to be the current position
        xo = x;
        yo = y;
        zo = z;

        // If the particle is too old kill it off
        if (age++ >= lifetime) {
            remove();
        }

        // Update the x,y,z motion
        updateMotionXYZ();

        // Move the particle based on motion
        move(xd, yd, zd);

        // If the particle is on the ground reduce the motion quickly
        if (onGround) {
            xd *= 0.7;
            zd *= 0.7;
        }
    }

    public void setAlphaFadeInLastTicks(final float ticksToFade) {
        if (lifetime - age < ticksToFade) {
            alpha = (lifetime - age) / ticksToFade;
        } else {
            alpha = 1f;
        }
    }

    public void updateMotionXYZ() {
    }
}
