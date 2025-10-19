package com.dslovikosky.narnia.client.particle.base;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.SpriteSet;

public abstract class DelayedNarniaParticle extends NarniaParticle {
    private final int delayTicks;
    private final int fadeTicks;

    public DelayedNarniaParticle(final ClientLevel clientLevel,
                                 final double x, final double y, final double z,
                                 final double xSpeed, final double ySpeed, final double zSpeed,
                                 final SpriteSet spriteSet, final int delayTicks, final int fadeTicks) {
        super(clientLevel, x, y, z, xSpeed, ySpeed, zSpeed, spriteSet);
        alpha = 0f;
        this.delayTicks = delayTicks;
        this.fadeTicks = fadeTicks;
    }

    @Override
    public void tick() {
        super.tick();
        if (age == delayTicks) {
            onDelayOver();
            tickPreDelay();
        } else if (age > delayTicks) {
            tickPostDelay();
        } else {
            tickPreDelay();
        }
    }

    public void onDelayOver() {
    }

    public void tickPostDelay() {
        if (age <= delayTicks + fadeTicks) {
            alpha = (float) (age - delayTicks) / fadeTicks;
        } else {
            setAlphaFadeInLastTicks(fadeTicks);
        }
    }

    public void tickPreDelay() {
    }

    @Override
    public void setLifetime(int newLifetime) {
        super.setLifetime(newLifetime + delayTicks + fadeTicks);
    }
}
