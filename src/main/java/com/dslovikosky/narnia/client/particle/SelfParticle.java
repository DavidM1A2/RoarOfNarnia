package com.dslovikosky.narnia.client.particle;

import com.dslovikosky.narnia.client.particle.base.DelayedNarniaParticle;
import com.dslovikosky.narnia.common.particle.SelfParticleData;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Nullable;

public class SelfParticle extends DelayedNarniaParticle {
    private final double startX;
    private final double startY;
    private final double startZ;
    private final Entity entity;
    private final float offsetDegrees;
    private final float width;
    private final float height;

    public SelfParticle(ClientLevel clientLevel, double startX, double startY, double startZ, SpriteSet spriteSet, int entityId, float offsetDegrees) {
        super(clientLevel, startX, startY, startZ, 0, 0, 0, spriteSet, Math.round(offsetDegrees) / 20 + 5, 5);
        this.startX = startX;
        this.startY = startY;
        this.startZ = startZ;
        this.entity = clientLevel.getEntity(entityId);
        this.offsetDegrees = offsetDegrees;
        this.width = entity == null ? 1f : entity.getBbWidth();
        this.height = entity == null ? 1f : entity.getBbHeight() / 2;

        // 1 second lifespan
        setLifetime(20 + Math.round(offsetDegrees) / 20);
        scale(entity == null ? 1f : entity.getBbWidth() * 2);
    }

    @Override
    public void updateMotionXYZ() {
        super.updateMotionXYZ();
        final double centerX = entity == null ? startX : entity.getX();
        final double centerY = entity == null ? startY : entity.getY();
        final double centerZ = entity == null ? startZ : entity.getZ();
        setPos(
                centerX + Math.sin(Math.toRadians(offsetDegrees + age * 5.0)) * width / 2 * 1.5,
                centerY + height,
                centerZ + Math.cos(Math.toRadians(offsetDegrees + age * 5.0)) * width / 2 * 1.5
        );
    }

    public record Factory(SpriteSet spriteSet) implements ParticleProvider<SelfParticleData> {
        @Override
        public @Nullable Particle createParticle(SelfParticleData type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, RandomSource random) {
            return new SelfParticle(level, x, y, z, spriteSet, type.entityId(), type.offsetDegrees());
        }
    }
}
