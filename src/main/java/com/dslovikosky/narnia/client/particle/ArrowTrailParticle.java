package com.dslovikosky.narnia.client.particle;

import com.dslovikosky.narnia.client.particle.base.DelayedNarniaParticle;
import com.dslovikosky.narnia.common.particle.ArrowTrailParticleData;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Nullable;

public class ArrowTrailParticle extends DelayedNarniaParticle {
    private final Entity entity;
    private final double baseX;
    private final double baseY;
    private final double baseZ;

    public ArrowTrailParticle(ClientLevel clientLevel, double x, double y, double z, int delayTicks, int entityId) {
        super(clientLevel, x, y, z, 0, 0, 0, delayTicks, 2);
        this.entity = clientLevel.getEntity(entityId);
        this.baseX = x;
        this.baseY = y;
        this.baseZ = z;
        scale(1.2f);
        // 0.4s
        setLifetime(8);
        xd = 0;
        yd = 0;
        zd = 0;
    }

    @Override
    public void onDelayOver() {
        final double centerX = entity == null ? baseX : entity.getX();
        final double centerY = entity == null ? baseY : entity.getY();
        final double centerZ = entity == null ? baseZ : entity.getZ();
        setPos(centerX, centerY, centerZ);
    }

    public record Factory(SpriteSet spriteSet) implements ParticleProvider<ArrowTrailParticleData> {
        @Override
        public @Nullable Particle createParticle(ArrowTrailParticleData type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            final ArrowTrailParticle particle = new ArrowTrailParticle(level, x, y, z, type.entityId(), type.delayTicks());
            particle.pickSprite(spriteSet);
            return particle;
        }
    }
}
