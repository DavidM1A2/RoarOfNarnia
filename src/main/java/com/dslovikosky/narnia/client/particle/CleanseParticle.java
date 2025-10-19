package com.dslovikosky.narnia.client.particle;

import com.dslovikosky.narnia.client.particle.base.NarniaParticle;
import com.dslovikosky.narnia.common.particle.CleanseParticleData;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Nullable;

public class CleanseParticle extends NarniaParticle {
    private final Entity entity;
    private final double baseX;
    private final double baseY;
    private final double baseZ;
    private final double height;
    private final float offsetDegrees;
    private final float radius;

    public CleanseParticle(final ClientLevel clientLevel,
                           final double x, final double y, final double z, SpriteSet spriteSet,
                           final int entityId, final float offsetDegrees, final float radius) {
        super(clientLevel, x, y, z, 0, 0, 0, spriteSet);
        this.entity = clientLevel.getEntity(entityId);
        this.baseX = x;
        this.baseY = y;
        this.baseZ = z;
        if (this.entity != null) {
            this.height = entity.getBbHeight();
        } else {
            this.height = 1f;
        }
        this.offsetDegrees = offsetDegrees;
        this.radius = radius;
        scale(1.2f);
        // 1s
        setLifetime(20);
        xd = 0.0;
        yd = 0.0;
        zd = 0.0;
    }

    @Override
    public void updateMotionXYZ() {
        final double centerX;
        final double centerY;
        final double centerZ;
        if (entity != null) {
            centerX = entity.getX();
            centerY = entity.getY();
            centerZ = entity.getZ();
        } else {
            centerX = baseX;
            centerY = baseY;
            centerZ = baseZ;
        }
        final double newX = centerX + radius * Math.sin(Math.toRadians(offsetDegrees) + age * 0.2);
        final double newY = centerY + ((double) age / lifetime) * height;
        final double newZ = centerZ + radius * Math.cos(Math.toRadians(offsetDegrees) + age * 0.2);
        setPos(newX, newY, newZ);
        setAlphaFadeInLastTicks(14f);
    }

    public record Factory(SpriteSet spriteSet) implements ParticleProvider<CleanseParticleData> {
        @Override
        public @Nullable Particle createParticle(CleanseParticleData type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, RandomSource random) {
            return new CleanseParticle(level, x, y, z, spriteSet, type.entityId(), type.offsetDegrees(), type.radius());
        }
    }
}
