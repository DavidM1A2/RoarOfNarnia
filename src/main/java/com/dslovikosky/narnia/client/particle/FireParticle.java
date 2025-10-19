package com.dslovikosky.narnia.client.particle;

import com.dslovikosky.narnia.client.particle.base.NarniaParticle;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;
import org.jetbrains.annotations.Nullable;

public class FireParticle extends NarniaParticle {
    private static final double FADE_SPEED_MIN = 0.24;
    private static final double FADE_SPEED_MAX = 0.32;
    private static final double WIGGLE_SPEED = 0.004;

    private final double fadeSpeed;
    private final double sinOffset;

    public FireParticle(ClientLevel clientLevel, double x, double y, double z, SpriteSet spriteSet, double ySpeed) {
        super(clientLevel, x, y, z, 0, ySpeed, 0, spriteSet);
        this.fadeSpeed = FADE_SPEED_MIN + random.nextDouble() * (FADE_SPEED_MAX - FADE_SPEED_MIN);
        this.sinOffset = random.nextDouble() * 2 * Math.PI;

        // 1.5 - 2 second lifespan
        setLifetime(40 + random.nextInt(40));
    }

    @Override
    public void updateMotionXYZ() {
        xd = xd + WIGGLE_SPEED * Math.sin(sinOffset + age * 0.2);
        yd = yd * 0.95;
        zd = zd + WIGGLE_SPEED * Math.cos(sinOffset + age * 0.2);
        final float oscillation = ((float) Math.sin(sinOffset + age * fadeSpeed) + 1f) / 2f;
        final float fadeAlpha = oscillation * 0.8f + 0.2f;
        setAlphaFadeInLastTicks(40f);
        alpha = alpha * fadeAlpha;
    }

    public record Factory(SpriteSet spriteSet) implements ParticleProvider<SimpleParticleType> {
        @Override
        public @Nullable Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, RandomSource random) {
            return new FireParticle(level, x, y, z, spriteSet, ySpeed);
        }
    }
}
