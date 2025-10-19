package com.dslovikosky.narnia.client.particle;

import com.dslovikosky.narnia.client.particle.base.NarniaParticle;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;
import org.jetbrains.annotations.Nullable;

public class SelfFizzleParticle extends NarniaParticle {
    private final SpriteSet spriteSet;

    public SelfFizzleParticle(ClientLevel clientLevel, double x, double y, double z, SpriteSet spriteSet) {
        super(clientLevel, x, y, z, 0, 0, 0, spriteSet);
        this.spriteSet = spriteSet;
        // 1 second lifespan
        setLifetime(20);
        scale(1.5f);
        // Moves upwards
        xd = 0;
        yd = 0.05;
        zd = 0;
    }

    @Override
    public void tick() {
        super.tick();
        setSpriteFromAge(spriteSet);
        setAlphaFadeInLastTicks(4f);
    }

    public record Factory(SpriteSet spriteSet) implements ParticleProvider<SimpleParticleType> {
        @Override
        public @Nullable Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, RandomSource random) {
            final SelfFizzleParticle particle = new SelfFizzleParticle(level, x, y, z, spriteSet);
            particle.setSpriteFromAge(spriteSet);
            return particle;
        }
    }
}
