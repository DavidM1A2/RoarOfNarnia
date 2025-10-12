package com.dslovikosky.narnia.client.particle.base;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.TextureSheetParticle;

public abstract class NarniaParticle extends TextureSheetParticle {
    public NarniaParticle(final ClientLevel clientLevel, final double x, final double y, final double z, final double xSpeed, final double ySpeed, final double zSpeed) {
        super(clientLevel, x, y, z, xSpeed, ySpeed, zSpeed);
        quadSize = 0.2f;
        xd = xSpeed;
        yd = ySpeed;
        zd = zSpeed;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
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
