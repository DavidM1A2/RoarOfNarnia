package com.dslovikosky.narnia.client.particle;

import com.dslovikosky.narnia.client.particle.base.NarniaParticle;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import org.jetbrains.annotations.Nullable;

public class PoisonParticle extends NarniaParticle {
    public PoisonParticle(ClientLevel clientLevel, double x, double y, double z) {
        super(clientLevel, x, y, z, 0, 0, 0);
        // 0.5-1.0 second lifespan
        setLifetime(random.nextInt(10) + 10);
        // Random motion
        xd = (random.nextFloat() - 0.5) * 0.05;
        yd = random.nextFloat() * 0.1;
        zd = (random.nextFloat() - 0.5) * 0.05;
    }

    public record Factory(SpriteSet spriteSet) implements ParticleProvider<SimpleParticleType> {
        @Override
        public @Nullable Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            final PoisonParticle particle = new PoisonParticle(level, x, y, z);
            particle.pickSprite(spriteSet);
            return particle;
        }
    }
}
