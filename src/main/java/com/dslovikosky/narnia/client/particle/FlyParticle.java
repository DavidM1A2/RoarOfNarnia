package com.dslovikosky.narnia.client.particle;

import com.dslovikosky.narnia.client.particle.base.DelayedNarniaParticle;
import com.dslovikosky.narnia.common.particle.FlyParticleData;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.world.entity.Entity;

import javax.annotation.Nullable;

public class FlyParticle extends DelayedNarniaParticle {
    private final double startX;
    private final double startY;
    private final double startZ;
    private final Entity entity;
    private final double sinOffset1;
    private final double sinOffset2;
    private final double sideSpeed;
    private final double sideDistance;
    private final double dropSpeed;

    public FlyParticle(ClientLevel clientLevel, double startX, double startY, double startZ, double xSpeed, double ySpeed, double zSpeed, int entityId, int delayTicks) {
        super(clientLevel, startX, startY, startZ, xSpeed, ySpeed, zSpeed, delayTicks, 8);
        this.startX = startX;
        this.startY = startY;
        this.startZ = startZ;
        this.entity = clientLevel.getEntity(entityId);
        this.sinOffset1 = random.nextDouble() * 2 * Math.PI;
        this.sinOffset2 = random.nextDouble() * 2 * Math.PI;
        this.sideSpeed = random.nextDouble() * 0.5 + 0.15;

        this.sideDistance = random.nextDouble() * 0.05 + 0.05;
        this.dropSpeed = random.nextDouble() * 0.004 + 0.002;

        // 2-3 second lifespan
        setLifetime(40 + random.nextInt(20));
        setPos(
                entity == null ? startX : entity.getX(),
                entity == null ? startY : entity.getY(),
                entity == null ? startZ : entity.getZ()
        );
    }

    @Override
    public void onDelayOver() {
        setPos(
                entity == null ? startX : entity.getX(),
                entity == null ? startY : entity.getY(),
                entity == null ? startZ : entity.getZ()
        );
        if (entity != null && entity.onGround()) {
            remove();
        }
    }

    @Override
    public void tickPostDelay() {
        super.tickPostDelay();
        xd = Math.sin(age * sideSpeed + sinOffset1) * sideDistance;
        yd = Math.min(yd - dropSpeed, 0.1);
        zd = Math.sin(age * sideSpeed + sinOffset2) * sideDistance;
    }

    public record Factory(SpriteSet spriteSet) implements ParticleProvider<FlyParticleData> {
        @Override
        public @Nullable Particle createParticle(FlyParticleData type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            final FlyParticle particle = new FlyParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, type.entityId(), type.delayTicks());
            particle.pickSprite(spriteSet);
            return particle;
        }
    }
}
