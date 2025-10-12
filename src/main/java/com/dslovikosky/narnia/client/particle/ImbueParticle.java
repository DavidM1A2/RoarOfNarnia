package com.dslovikosky.narnia.client.particle;

import com.dslovikosky.narnia.client.particle.base.NarniaParticle;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import org.jetbrains.annotations.Nullable;

public class ImbueParticle extends NarniaParticle {
    public ImbueParticle(ClientLevel clientLevel, double x, double y, double z) {
        super(clientLevel, x, y, z, 0, 0, 0);
        // 1.5 second lifespan
        setLifetime(30);
        scale(1.5f);
        xd = (random.nextDouble() - 0.5) * 0.02;
        yd = (random.nextDouble() - 0.5) * 0.02;
        zd = (random.nextDouble() - 0.5) * 0.02;
    }

    @Override
    public void tick() {
        super.tick();
        setAlphaFadeInLastTicks(4f);
    }

    @Override
    public void updateMotionXYZ() {
        super.updateMotionXYZ();
        xd = xd * 1.05;
        yd = yd * 1.05;
        zd = zd * 1.05;
    }

    public record Factory(SpriteSet spriteSet) implements ParticleProvider<SimpleParticleType> {
        @Override
        public @Nullable Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            final ImbueParticle particle = new ImbueParticle(level, x, y, z);
            particle.pickSprite(spriteSet);
            return particle;
        }
    }
}
