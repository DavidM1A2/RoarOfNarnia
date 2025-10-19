package com.dslovikosky.narnia.client.particle;

import com.dslovikosky.narnia.client.particle.base.NarniaParticle;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;
import org.jetbrains.annotations.Nullable;

public class LightningParticle extends NarniaParticle {
    private static final int FULLBRIGHT = LightTexture.pack(15, 15);

    private int bounceCount = 0;

    public LightningParticle(ClientLevel clientLevel, double x, double y, double z, SpriteSet spriteSet) {
        super(clientLevel, x, y, z, 0, 0, 0, spriteSet);
        scale(0.8f);
        // 10 second lifespan, or 3 bounces
        setLifetime(10 * 20);
        // Random outwards motion
        xd = (random.nextDouble() - 0.5) * 0.4;
        yd = random.nextDouble() + 0.2;
        zd = (random.nextDouble() - 0.5) * 0.4;
    }

    @Override
    public void updateMotionXYZ() {
        super.updateMotionXYZ();

        if (onGround) {
            xd = xd * (1 + (random.nextFloat() - 0.5) * 0.2);
            yd = -yd * 0.4;
            zd = zd * (1 + (random.nextFloat() - 0.5) * 0.2);
            bounceCount = bounceCount + 1;
        } else {
            yd -= 0.14;
        }

        if (bounceCount == 4) {
            remove();
        }
    }

    @Override
    protected int getLightColor(float partialTick) {
        return FULLBRIGHT;
    }

    public record Factory(SpriteSet spriteSet) implements ParticleProvider<SimpleParticleType> {
        @Override
        public @Nullable Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, RandomSource random) {
            return new LightningParticle(level, x, y, z, spriteSet);
        }
    }
}
