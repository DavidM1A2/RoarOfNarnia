package com.dslovikosky.narnia.client.particle;

import com.dslovikosky.narnia.client.particle.base.NarniaParticle;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;
import org.jetbrains.annotations.Nullable;

public class FreezeParticle extends NarniaParticle {
    public FreezeParticle(ClientLevel clientLevel, double x, double y, double z, SpriteSet spriteSet, int freezeDurationTicks) {
        super(clientLevel, x, y, z, 0, 0, 0, spriteSet);
        // Up to 1 second longer than the freeze duration
        setLifetime(freezeDurationTicks + random.nextInt(20));
        scale(random.nextFloat() + 1f);
        // No motion
        xd = 0.0;
        yd = 0.0;
        zd = 0.0;
    }

    @Override
    public void updateMotionXYZ() {
        setAlphaFadeInLastTicks(20f);
    }

    public record Factory(SpriteSet spriteSet) implements ParticleProvider<SimpleParticleType> {
        @Override
        public @Nullable Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, RandomSource random) {
            return new FreezeParticle(level, x, y, z, spriteSet, (int) xSpeed);
        }
    }
}
