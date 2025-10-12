package com.dslovikosky.narnia.client.particle;

import com.dslovikosky.narnia.client.particle.base.NarniaParticle;
import com.dslovikosky.narnia.common.particle.FeyParticleData;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class FeyParticle extends NarniaParticle {
    public FeyParticle(ClientLevel clientLevel, double x, double y, double z, float offsetDegrees, float red, float green, float blue) {
        super(clientLevel, x, y, z, 0, 0, 0);

        setLifetime(20 + random.nextInt(10));
        scale(random.nextFloat() * 0.5f + 0.5f);

        final double startOffsetDistance = random.nextDouble() * 0.2 + 0.3;
        final double xOffset = Math.sin(Math.toRadians(offsetDegrees)) * startOffsetDistance;
        final double zOffset = Math.cos(Math.toRadians(offsetDegrees)) * startOffsetDistance;
        setPos(x + xOffset, y, z + zOffset);
        xo = x + xOffset;
        yo = y;
        zo = z + zOffset;

        final Vec3 motionDir = new Vec3(xOffset, 0.0, zOffset)
                .vectorTo(new Vec3(0.0, 0.0, 0.0))
                .normalize()
                .scale(0.06);

        xd = motionDir.x;
        yd = 0.01;
        zd = motionDir.z;

        rCol = red;
        gCol = green;
        bCol = blue;
    }

    @Override
    public void tick() {
        super.tick();
        setAlphaFadeInLastTicks(10f);
    }

    @Override
    public void updateMotionXYZ() {
        xd = xd * 0.9;
        yd = yd * 1.1;
        zd = zd * 0.9;
    }

    public record Factory(SpriteSet spriteSet) implements ParticleProvider<FeyParticleData> {
        @Override
        public @Nullable Particle createParticle(FeyParticleData type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            final FeyParticle particle = new FeyParticle(level, x, y, z, type.offsetDegrees(), type.red(), type.green(), type.blue());
            particle.pickSprite(spriteSet);
            return particle;
        }
    }
}
