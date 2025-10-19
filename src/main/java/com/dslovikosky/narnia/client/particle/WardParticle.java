package com.dslovikosky.narnia.client.particle;

import com.dslovikosky.narnia.client.particle.base.RotatedNarniaParticle;
import com.dslovikosky.narnia.common.particle.WardParticleData;
import com.mojang.math.Axis;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class WardParticle extends RotatedNarniaParticle {
    private final Direction direction;
    private final SpriteSet spriteSet;

    public WardParticle(ClientLevel clientLevel, double x, double y, double z, float scale, Direction direction, SpriteSet spriteSet) {
        super(clientLevel, x, y, z, 0, 0, 0, spriteSet);
        this.direction = direction;
        this.spriteSet = spriteSet;
        this.rotation = direction.getRotation().mul(Axis.XP.rotationDegrees(90f));

        // 0.4 second lifespan
        setLifetime(8);
        scale(scale);

        // No motion
        xd = 0.0;
        yd = 0.0;
        zd = 0.0;
    }

    @Override
    public void tick() {
        super.tick();
        setSpriteFromAge(spriteSet);
        final BlockState blockState = level.getBlockState(new BlockPos((int) x, (int) y, (int) z).relative(direction.getOpposite()));
        if (blockState.isAir()) {
            remove();
        }
    }

    public record Factory(SpriteSet spriteSet) implements ParticleProvider<WardParticleData> {
        @Override
        public @Nullable Particle createParticle(WardParticleData type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, RandomSource random) {
            final WardParticle particle = new WardParticle(level, x, y, z, type.scale(), type.direction(), spriteSet);
            particle.setSpriteFromAge(spriteSet);
            return particle;
        }
    }
}
