package com.dslovikosky.narnia.client.particle;

import com.dslovikosky.narnia.client.particle.base.NarniaParticle;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;
import org.jetbrains.annotations.Nullable;

public class DigParticle extends NarniaParticle {
    public DigParticle(ClientLevel clientLevel, double x, double y, double z, SpriteSet spriteSet) {
        super(clientLevel, x, y, z, 0, 0, 0, spriteSet);
        // 0.75s
        setLifetime(15);
        // Random outwards motion
        xd = (random.nextDouble() - 0.5) * 0.3;
        yd = random.nextDouble() * 0.2 + 0.4;
        zd = (random.nextDouble() - 0.5) * 0.3;
    }

    @Override
    public void updateMotionXYZ() {
        yd -= 0.08;
        setAlphaFadeInLastTicks(4f);
    }

    public record Factory(SpriteSet spriteSet) implements ParticleProvider<SimpleParticleType> {
        @Override
        public @Nullable Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, RandomSource random) {
            return new DigParticle(level, x, y, z, spriteSet);
        }
    }
}
