package com.dslovikosky.narnia.client.particle;

import com.dslovikosky.narnia.client.particle.base.NarniaParticle;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import org.jetbrains.annotations.Nullable;

public class GrowParticle extends NarniaParticle {
    public GrowParticle(ClientLevel clientLevel, double x, double y, double z) {
        super(clientLevel, x, y, z, 0, 0, 0);
        // 1-1.5 second lifespan
        setLifetime(20 + random.nextInt(10));
        scale(0.3f + random.nextFloat());
        // Drift Upwards
        xd = (random.nextDouble() - 0.5) * 0.3;
        yd = random.nextDouble() * 0.3;
        zd = (random.nextDouble() - 0.5) * 0.3;

        rCol = 0.5f;
        gCol = 0.5f;
        bCol = 0.5f;
    }

    @Override
    public void updateMotionXYZ() {
        xd *= 0.9;
        yd *= 0.95;
        zd *= 0.9;
        setAlphaFadeInLastTicks(3f);
        if (age <= 6) {
            rCol = age / 6f;
            gCol = age / 6f;
            bCol = age / 6f;
        }
    }

    public record Factory(SpriteSet spriteSet) implements ParticleProvider<SimpleParticleType> {
        @Override
        public @Nullable Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            final GrowParticle particle = new GrowParticle(level, x, y, z);
            particle.pickSprite(spriteSet);
            return particle;
        }
    }
}
