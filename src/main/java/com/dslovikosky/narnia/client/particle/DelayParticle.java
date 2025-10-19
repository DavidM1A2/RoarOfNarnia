package com.dslovikosky.narnia.client.particle;

import com.dslovikosky.narnia.client.particle.base.NarniaParticle;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;
import org.jetbrains.annotations.Nullable;

public class DelayParticle extends NarniaParticle {
    private final SpriteSet spriteSet;

    public DelayParticle(ClientLevel clientLevel, double x, double y, double z, float scale, SpriteSet spriteSet) {
        super(clientLevel, x, y, z, 0, 0, 0, spriteSet);
        this.spriteSet = spriteSet;
        // 1s
        setLifetime(20);
        scale(scale);
        // Moves upwards based on how big it is
        xd = 0;
        yd = 0.12 * scale;
        zd = 0;
    }

    @Override
    public void tick() {
        super.tick();
        setSpriteFromAge(spriteSet);
        setAlphaFadeInLastTicks(15f);
    }

    @Override
    public void updateMotionXYZ() {
        super.updateMotionXYZ();
        yd = yd * 0.9;
    }

    public record Factory(SpriteSet spriteSet) implements ParticleProvider<SimpleParticleType> {
        @Override
        public @Nullable Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, RandomSource random) {
            final DelayParticle particle = new DelayParticle(level, x, y, z, (float) xSpeed, spriteSet);
            particle.setSpriteFromAge(spriteSet);
            return particle;
        }
    }
}
