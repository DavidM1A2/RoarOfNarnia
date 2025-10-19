package com.dslovikosky.narnia.client.particle;

import com.dslovikosky.narnia.client.particle.base.RotatedNarniaParticle;
import com.dslovikosky.narnia.common.utils.MathUtils;
import com.mojang.math.Axis;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.state.QuadParticleRenderState;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;

public class RotateParticle extends RotatedNarniaParticle {
    private static final Vec3 BASE_DIRECTION = new Vec3(1.0, 0.0, 0.0);
    private final Quaternionf baseRotation;
    private Quaternionf ninetyDegreeRotatedRotation = new Quaternionf();

    public RotateParticle(ClientLevel clientLevel, double x, double y, double z, SpriteSet spriteSet, double xDir, double yDir, double zDir) {
        super(clientLevel, x, y, z, 0, 0, 0, spriteSet);
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
    protected void extractRotatedQuad(QuadParticleRenderState reusedState, Camera camera, Quaternionf orientation, float partialTick) {
        // Render the particle twice, once at the standard rotation, and once 90 degrees more rotated.
        super.extractRotatedQuad(reusedState, camera, orientation, partialTick);
        super.extractRotatedQuad(reusedState, camera, rotation, partialTick);
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
        public @Nullable Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, RandomSource random) {
            return new RotateParticle(level, x, y, z, spriteSet, xSpeed, ySpeed, zSpeed);
        }
    }
}
