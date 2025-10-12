package com.dslovikosky.narnia.client.particle;

import com.dslovikosky.narnia.client.particle.base.RotatedNarniaParticle;
import com.dslovikosky.narnia.common.particle.ShieldParticleData;
import com.mojang.math.Axis;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.world.entity.Entity;

import javax.annotation.Nullable;

public class ShieldParticle extends RotatedNarniaParticle {
    private static final double SPIN_SPEED = 0.15;
    private static final float FADE_MAX = 10f;

    private final Entity entity;
    private final float offsetDegrees;
    private final float radius;

    public ShieldParticle(ClientLevel clientLevel, double x, double y, double z, int entityId, int duration, float offsetDegrees, float radius) {
        super(clientLevel, x, y, z, 0, 0, 0);
        this.entity = clientLevel.getEntity(entityId);
        this.offsetDegrees = offsetDegrees;
        this.radius = radius;

        setLifetime(duration);
        scale(radius * 2);
        // No motion
        xd = 0;
        yd = 0;
        zd = 0;
    }

    @Override
    public void tick() {
        super.tick();
        if (entity != null && !entity.isAlive()) {
            remove();
        }
    }

    @Override
    public void updateMotionXYZ() {
        if (entity != null) {
            final double centerX = entity.getX();
            final double centerY = entity.getY();
            final double centerZ = entity.getZ();
            final double newX = centerX + radius * Math.sin(Math.toRadians(offsetDegrees) + age * SPIN_SPEED);
            final double newY = centerY + entity.getBbHeight() / 2;
            final double newZ = centerZ + radius * Math.cos(Math.toRadians(offsetDegrees) + age * SPIN_SPEED);
            setPos(newX, newY, newZ);

            final float fade = Math.min(lifetime / 2f, FADE_MAX);
            setAlphaFadeInLastTicks(fade);
            if (age <= fade) {
                alpha = alpha * age / fade;
            }
            rotation = Axis.YP.rotationDegrees(offsetDegrees - 180f + age * (float) Math.toDegrees(SPIN_SPEED));
        }
    }

    public record Factory(SpriteSet spriteSet) implements ParticleProvider<ShieldParticleData> {
        @Override
        public @Nullable Particle createParticle(ShieldParticleData type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            final ShieldParticle particle = new ShieldParticle(level, x, y, z, type.entityId(), type.duration(), type.offsetDegrees(), type.radius());
            particle.pickSprite(spriteSet);
            return particle;
        }
    }
}
