package com.dslovikosky.narnia.client.particle;

import com.dslovikosky.narnia.client.particle.base.NarniaParticle;
import com.dslovikosky.narnia.common.particle.ProjectileParticleData;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ProjectileParticle extends NarniaParticle {
    private final float scale;
    private final float baseQuadSize;

    public ProjectileParticle(ClientLevel clientLevel, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, SpriteSet spriteSet, float scale, float red, float green, float blue) {
        super(clientLevel, x, y, z, 0, 0, 0, spriteSet);
        this.scale = scale;

        // 0.5 second lifespan
        setLifetime(10);
        scale(scale);
        this.baseQuadSize = quadSize;

        xd = xSpeed;
        yd = ySpeed;
        zd = zSpeed;

        rCol = red;
        gCol = green;
        bCol = blue;
    }

    @Override
    protected @NotNull Layer getLayer() {
        return TRANSLUCENT_NO_DEPTH;
    }

    @Override
    public void tick() {
        super.tick();

        // Shrink the particle over time
        final float newScale = Mth.lerp((float) age / lifetime, scale, scale / 2);
        // For whatever reason "scale" does quadSize *= newScale, so reset it to avoid exponential quad size growth
        scale(newScale);
        quadSize = baseQuadSize * newScale;
    }

    @Override
    public void updateMotionXYZ() {
        setAlphaFadeInLastTicks(5f);
        xd = xd * 0.99;
        yd = yd - 0.01;
        zd = zd * 0.99;
    }

    public record Factory(SpriteSet spriteSet) implements ParticleProvider<ProjectileParticleData> {
        @Override
        public @Nullable Particle createParticle(ProjectileParticleData type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, RandomSource random) {
            return new ProjectileParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, spriteSet, type.scale(), type.red(), type.green(), type.blue());
        }
    }
}
