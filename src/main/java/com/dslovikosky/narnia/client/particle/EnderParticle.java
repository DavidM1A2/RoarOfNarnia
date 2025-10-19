package com.dslovikosky.narnia.client.particle;

import com.dslovikosky.narnia.client.particle.base.NarniaParticle;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;
import org.jetbrains.annotations.Nullable;

public class EnderParticle extends NarniaParticle {
    public EnderParticle(ClientLevel clientLevel, double x, double y, double z, SpriteSet spriteSet) {
        super(clientLevel, x, y, z, 0, 0, 0, spriteSet);

        // 1.5 - 2 second lifespan
        setLifetime(random.nextInt(10) + 30);

        // Random motion
        xd = (random.nextFloat() - 0.5) * 0.2;
        yd = (random.nextFloat() - 0.5) * 0.2;
        zd = (random.nextFloat() - 0.5) * 0.2;
    }

    @Override
    public void updateMotionXYZ() {
        // Random motion
        xd = (random.nextFloat() - 0.5) * 0.2;
        yd = (random.nextFloat() - 0.5) * 0.2;
        zd = (random.nextFloat() - 0.5) * 0.2;
        setAlphaFadeInLastTicks(4f);
    }

    public record Factory(SpriteSet spriteSet) implements ParticleProvider<SimpleParticleType> {
        @Override
        public @Nullable Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, RandomSource random) {
            return new EnderParticle(level, x, y, z, spriteSet);
        }
    }
}
