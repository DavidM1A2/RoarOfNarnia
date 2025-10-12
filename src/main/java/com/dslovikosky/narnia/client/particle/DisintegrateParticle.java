package com.dslovikosky.narnia.client.particle;

import com.dslovikosky.narnia.client.particle.base.NarniaParticle;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import org.jetbrains.annotations.Nullable;

public class DisintegrateParticle extends NarniaParticle {
    public DisintegrateParticle(ClientLevel clientLevel, double x, double y, double z) {
        super(clientLevel, x, y, z, 0, 0, 0);
        // 1s
        setLifetime(20);
        scale(3f);
        xd = 0.0;
        yd = 0.0;
        zd = 0.0;
    }

    @Override
    public void updateMotionXYZ() {
        scale((lifetime - age) / (float) lifetime);
        setAlphaFadeInLastTicks(lifetime);
    }

    public record Factory(SpriteSet spriteSet) implements ParticleProvider<SimpleParticleType> {
        @Override
        public @Nullable Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            final DisintegrateParticle particle = new DisintegrateParticle(level, x, y, z);
            particle.pickSprite(spriteSet);
            return particle;
        }
    }
}
