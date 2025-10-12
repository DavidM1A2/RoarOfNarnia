package com.dslovikosky.narnia.client.particle;

import com.dslovikosky.narnia.client.particle.base.NarniaParticle;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import org.jetbrains.annotations.Nullable;

public class SpellLaserParticle extends NarniaParticle {
    public SpellLaserParticle(ClientLevel clientLevel, double x, double y, double z) {
        super(clientLevel, x, y, z, 0, 0, 0);

        // 1 second lifespan
        setLifetime(20);

        // No motion
        xd = 0.0;
        yd = 0.0;
        zd = 0.0;
    }

    @Override
    public void updateMotionXYZ() {
        // Set scale to be based on time alive
        scale((lifetime - age) / (float) lifetime * 0.5f + 0.005f);
    }

    public record Factory(SpriteSet spriteSet) implements ParticleProvider<SimpleParticleType> {
        @Override
        public @Nullable Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            final SpellLaserParticle particle = new SpellLaserParticle(level, x, y, z);
            particle.pickSprite(spriteSet);
            return particle;
        }
    }
}
