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

public class SonicDisruptionParticle extends NarniaParticle {
    private static final float MIN_SIZE = 0.5f;
    private static final float MAX_SIZE = 2f;

    public SonicDisruptionParticle(ClientLevel clientLevel, double x, double y, double z, SpriteSet spriteSet, float sizePercent) {
        super(clientLevel, x, y, z, 0, 0, 0, spriteSet);
        scale(Mth.lerp(sizePercent, MIN_SIZE, MAX_SIZE));
        setLifetime(20);
        // No motion
        xd = 0.0;
        yd = 0.0;
        zd = 0.0;
    }

    @Override
    public void tick() {
        super.tick();
        setAlphaFadeInLastTicks(lifetime);
    }

    public record Factory(SpriteSet spriteSet) implements ParticleProvider<SimpleParticleType> {
        @Override
        public @Nullable Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, RandomSource random) {
            return new SonicDisruptionParticle(level, x, y, z, spriteSet, (float) xSpeed);
        }
    }
}
