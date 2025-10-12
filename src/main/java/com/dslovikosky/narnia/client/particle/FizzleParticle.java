package com.dslovikosky.narnia.client.particle;

import com.dslovikosky.narnia.client.particle.base.NarniaParticle;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import org.jetbrains.annotations.Nullable;

public class FizzleParticle extends NarniaParticle {
    private static final float SCALE_FACTOR_PER_TICK = 0.98f;

    private final SpriteSet spriteSet;

    public FizzleParticle(ClientLevel clientLevel, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, SpriteSet spriteSet) {
        super(clientLevel, x, y, z, xSpeed, ySpeed, zSpeed);
        this.spriteSet = spriteSet;

        // 1s
        setLifetime(20);
        scale(2f);
    }

    @Override
    public void tick() {
        super.tick();
        yd = yd * 0.8;
        scale(SCALE_FACTOR_PER_TICK);
        if (!removed) {
            setSpriteFromAge(spriteSet);
        }
    }

    @Override
    public void updateMotionXYZ() {
        super.updateMotionXYZ();
        yd = yd * 0.9;
    }

    public record Factory(SpriteSet spriteSet) implements ParticleProvider<SimpleParticleType> {
        @Override
        public @Nullable Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            final FizzleParticle particle = new FizzleParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, spriteSet);
            particle.setSpriteFromAge(spriteSet);
            return particle;
        }
    }
}
