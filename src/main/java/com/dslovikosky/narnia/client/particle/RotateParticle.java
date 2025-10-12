package com.dslovikosky.narnia.client.particle;

import com.dslovikosky.narnia.client.particle.base.RotatedNarniaParticle;
import com.dslovikosky.narnia.common.utils.MathUtils;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;

import javax.annotation.Nullable;

public class RotateParticle extends RotatedNarniaParticle {
    private static final Vec3 BASE_DIRECTION = new Vec3(1.0, 0.0, 0.0);
    private final Quaternionf baseRotation;
    private Quaternionf ninetyDegreeRotatedRotation = new Quaternionf();

    public RotateParticle(ClientLevel clientLevel, double x, double y, double z, double xDir, double yDir, double zDir) {
        super(clientLevel, x, y, z, 0, 0, 0);
        this.baseRotation = MathUtils.computeRotationTo(BASE_DIRECTION, new Vec3(xDir, yDir, zDir));
        // 0.5 second lifespan
        setLifetime(10);
        scale(1f);
        // No movement
        xd = 0.0;
        yd = 0.0;
        zd = 0.0;
    }

    @Override
    public void render(VertexConsumer buffer, Camera renderInfo, float partialTicks) {
        // Render the particle twice, once at the standard rotation, and once 90 degrees more rotated.
        super.render(buffer, renderInfo, partialTicks);
        renderRotatedQuad(buffer, renderInfo, rotation, partialTicks);
    }

    @Override
    public void updateMotionXYZ() {
        setAlphaFadeInLastTicks(2f);
        final Quaternionf newRotation = new Quaternionf(baseRotation);
        newRotation.mul(Axis.XP.rotationDegrees(age * 10));
        rotation = newRotation;

        ninetyDegreeRotatedRotation = new Quaternionf(newRotation);
        ninetyDegreeRotatedRotation.mul(Axis.XP.rotationDegrees(90f));
    }

    public record Factory(SpriteSet spriteSet) implements ParticleProvider<SimpleParticleType> {
        @Override
        public @Nullable Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            final RotateParticle particle = new RotateParticle(level, x, y, z, xSpeed, ySpeed, zSpeed);
            particle.pickSprite(spriteSet);
            return particle;
        }
    }
}
