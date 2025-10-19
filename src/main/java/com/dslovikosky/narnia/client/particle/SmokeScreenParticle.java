package com.dslovikosky.narnia.client.particle;

import com.dslovikosky.narnia.client.particle.base.NarniaParticle;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import org.jetbrains.annotations.Nullable;

public class SmokeScreenParticle extends NarniaParticle {
    private static final double SPEED = 0.05;

    private final float minScale;
    private final float maxScale;
    private final float baseQuadSize;

    public SmokeScreenParticle(ClientLevel clientLevel, double x, double y, double z, SpriteSet spriteSet) {
        super(clientLevel, x, y, z, 0, 0, 0, spriteSet);
        this.minScale = 3.75f + random.nextFloat() * 1.25f;
        this.maxScale = 7.5f + minScale;
        this.baseQuadSize = quadSize / 5;

        // 15-20 second lifespan
        setLifetime(random.nextInt(100) + 300);
        scale(minScale);

        // Particle moves outwards
        xd = (random.nextDouble() - 0.5) * SPEED;
        yd = (random.nextDouble() - 0.5) * SPEED;
        zd = (random.nextDouble() - 0.5) * SPEED;
    }

    @Override
    public void updateMotionXYZ() {
        // Slowly reduce motion
        xd = xd * 0.95;
        yd = yd * 0.95;
        zd = zd * 0.95;
        // Expand the particle over time
        final float newScale = Mth.lerp((float) age / lifetime, minScale, maxScale);
        // For whatever reason "scale" does quadSize *= newScale, so reset it to avoid exponential quad size growth
        scale(newScale);
        quadSize = baseQuadSize * newScale;
    }

    public record Factory(SpriteSet spriteSet) implements ParticleProvider<SimpleParticleType> {
        @Override
        public @Nullable Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, RandomSource random) {
            return new SmokeScreenParticle(level, x, y, z, spriteSet);
        }
    }
}
