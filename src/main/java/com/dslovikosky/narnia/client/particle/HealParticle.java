package com.dslovikosky.narnia.client.particle;

import com.dslovikosky.narnia.client.particle.base.DelayedNarniaParticle;
import com.dslovikosky.narnia.common.particle.HealParticleData;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class HealParticle extends DelayedNarniaParticle {
    private final double startX;
    private final double startY;
    private final double startZ;
    private final float offsetDegrees;
    private final Entity entity;
    private final float width;
    private final float height;
    private final double targetVelocityX;
    private final double targetVelocityY;
    private final double targetVelocityZ;
    private final double stickiness;
    private double targetOffsetX;
    private double targetOffsetY;
    private double targetOffsetZ;

    public HealParticle(ClientLevel clientLevel, double x, double y, double z, int entityId, float offsetDegrees) {
        super(clientLevel, x, y, z, 0, 0, 0, RandomSource.create().nextInt(17), 3);
        this.startX = x;
        this.startY = y;
        this.startZ = z;
        this.entity = clientLevel.getEntity(entityId);
        this.offsetDegrees = offsetDegrees;
        this.width = entity == null ? 0f : entity.getBbWidth();
        this.height = entity == null ? 0f : entity.getBbHeight();
        this.targetOffsetX = 0.0;
        this.targetOffsetY = 0.0;
        this.targetOffsetZ = 0.0;
        this.targetVelocityX = (random.nextFloat() - 0.5) * 0.02;
        this.targetVelocityY = random.nextFloat() * (height / 25) + height / 25;
        this.targetVelocityZ = (random.nextFloat() - 0.5) * 0.02;
        this.stickiness = random.nextDouble() * 5 + 1.0;

        // 0.5-1.0 second lifespan
        setLifetime(10 + random.nextInt(10));
        alpha = 0f;
    }

    @Override
    public void onDelayOver() {
        final Vec3 targetPosition = computeTargetPosition();
        setPos(targetPosition.x, targetPosition.y, targetPosition.z);
    }

    @Override
    public void tickPostDelay() {
        super.tickPostDelay();
        final Vec3 targetPosition = computeTargetPosition();
        targetOffsetX = targetOffsetX + targetVelocityX;
        targetOffsetY = targetOffsetY + targetVelocityY;
        targetOffsetZ = targetOffsetZ + targetVelocityZ;
        xd = (targetPosition.x - x) / stickiness;
        yd = (targetPosition.y - y) / stickiness;
        zd = (targetPosition.z - z) / stickiness;
    }

    private Vec3 computeTargetPosition() {
        final double centerX = entity == null ? startX : entity.getX();
        final double centerY = entity == null ? startY : entity.getY();
        final double centerZ = entity == null ? startZ : entity.getZ();
        final double targetX = centerX + targetOffsetX + Math.sin(Math.toRadians(offsetDegrees)) * width / 2 * 1.5;
        final double targetY = centerY + targetOffsetY;
        final double targetZ = centerZ + targetOffsetZ + Math.cos(Math.toRadians(offsetDegrees)) * width / 2 * 1.5;
        return new Vec3(targetX, targetY, targetZ);
    }

    public record Factory(SpriteSet spriteSet) implements ParticleProvider<HealParticleData> {
        @Override
        public @Nullable Particle createParticle(HealParticleData type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            final HealParticle particle = new HealParticle(level, x, y, z, type.entityId(), type.offsetDegrees());
            particle.pickSprite(spriteSet);
            return particle;
        }
    }
}
