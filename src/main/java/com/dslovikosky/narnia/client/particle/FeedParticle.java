package com.dslovikosky.narnia.client.particle;

import com.dslovikosky.narnia.client.particle.base.NarniaParticle;
import com.dslovikosky.narnia.common.particle.FeedParticleData;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Nullable;

public class FeedParticle extends NarniaParticle {
    private final float offsetDegrees;
    private final float radius;
    private final Entity entity;
    private final double baseX;
    private final double baseY;
    private final double baseZ;
    private final float height;

    public FeedParticle(ClientLevel clientLevel, double x, double y, double z, SpriteSet spriteSet, int entityId, float offsetDegrees, float radius) {
        super(clientLevel, x, y, z, 0, 0, 0, spriteSet);
        this.offsetDegrees = offsetDegrees;
        this.radius = radius;
        this.entity = clientLevel.getEntity(entityId);
        this.baseX = x;
        this.baseY = y;
        this.baseZ = z;
        this.height = entity == null ? 1f : entity.getBbHeight();

        // 1s lifespan
        setLifetime(20);
        scale(1.6f);

        // Random motion
        xd = 0;
        yd = 0;
        zd = 0;

        alpha = 0;
    }

    @Override
    public void updateMotionXYZ() {
        final double centerX = entity == null ? baseX : entity.getX();
        final double centerY = entity == null ? baseY : entity.getY();
        final double centerZ = entity == null ? baseZ : entity.getZ();
        final double newX = centerX + Math.sin(offsetDegrees) * Mth.lerp(age / (float) lifetime, 2.5f * radius, 0f);
        final double newY = centerY + Mth.lerp(age / (float) lifetime, 0f, height * 0.6f);
        final double newZ = centerZ + Math.cos(offsetDegrees) * Mth.lerp(age / (float) lifetime, 2.5f * radius, 0f);
        setPos(newX, newY, newZ);
        setAlphaFadeInLastTicks(5f);
        if (age < 15) {
            alpha = alpha * age / 15;
        }
    }

    public record Factory(SpriteSet spriteSet) implements ParticleProvider<FeedParticleData> {
        @Override
        public @Nullable Particle createParticle(FeedParticleData type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, RandomSource random) {
            return new FeedParticle(level, x, y, z, spriteSet, type.entityId(), type.offsetDegrees(), type.radius());
        }
    }
}
