package com.dslovikosky.narnia.client.particle;

import com.dslovikosky.narnia.client.particle.base.NarniaParticle;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import org.jetbrains.annotations.Nullable;

public class ExplosionParticle extends NarniaParticle {
    private static final double FADE_SPEED_MIN = 0.24;
    private static final double FADE_SPEED_MAX = 0.32;

    private final double fadeSpeed;
    private final double sinOffset;

    public ExplosionParticle(ClientLevel clientLevel, double x, double y, double z, double explosionRadius) {
        super(clientLevel, x, y, z, 0, 0, 0);
        this.fadeSpeed = FADE_SPEED_MIN + random.nextDouble() * (FADE_SPEED_MAX - FADE_SPEED_MIN);
        this.sinOffset = random.nextDouble() * 2 * Math.PI;

        // 1.5 - 2 second lifespan
        // setLifetime(Mth.lerp(explosionRadius / ExplosionSpellEffect.MAX_RADIUS, 60.0, 160.0).toInt());
        scale(random.nextFloat() * 3f + 1f);

        // Random motion
        xd = (random.nextFloat() - 0.5) * 0.8 * Math.sqrt(explosionRadius);
        yd = (random.nextFloat() - 0.1) * 0.8 * Math.sqrt(explosionRadius);
        zd = (random.nextFloat() - 0.5) * 0.8 * Math.sqrt(explosionRadius);
    }

    @Override
    public void updateMotionXYZ() {
        xd = xd * 0.95;
        yd = Math.max(-2, yd - 0.08);
        zd = zd * 0.95;

        final float oscillation = ((float) Math.sin(sinOffset + age * fadeSpeed) + 1f) / 2f;
        final float fadeAlpha = oscillation * 0.5f + 0.5f;
        setAlphaFadeInLastTicks(40f);
        alpha = alpha * fadeAlpha;
        setColor(fadeAlpha, fadeAlpha, fadeAlpha);
    }

    public record Factory(SpriteSet spriteSet) implements ParticleProvider<SimpleParticleType> {
        @Override
        public @Nullable Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            final ExplosionParticle particle = new ExplosionParticle(level, x, y, z, xSpeed);
            particle.pickSprite(spriteSet);
            return particle;
        }
    }
}
