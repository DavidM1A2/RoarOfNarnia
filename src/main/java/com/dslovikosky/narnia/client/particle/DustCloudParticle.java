package com.dslovikosky.narnia.client.particle;

import com.dslovikosky.narnia.client.particle.base.NarniaParticle;
import com.google.common.collect.ImmutableMap;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.biome.Biome;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class DustCloudParticle extends NarniaParticle {
    private final float minScale;
    private final float maxScale;
    private final float baseQuadSize;

    public DustCloudParticle(ClientLevel clientLevel, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, SpriteSet spriteSet) {
        super(clientLevel, x, y, z, xSpeed, ySpeed, zSpeed, spriteSet);
        this.minScale = 1f + random.nextFloat() * 0.5f;
        this.maxScale = 4.5f + minScale;
        this.baseQuadSize = quadSize;

        // 1s
        setLifetime(random.nextInt(5) + 20);
        scale(minScale);
    }

    @Override
    public void tick() {
        super.tick();

        if (age < lifetime - 5) {
            alpha = alpha * 0.95f;
        } else {
            alpha = alpha * 0.5f;
        }

        // Expand the particle over time
        final float newScale = Mth.lerp((float) age / lifetime, minScale, maxScale);
        // For whatever reason "scale" does quadSize *= newScale, so reset it to avoid exponential quad size growth
        scale(newScale);
        quadSize = baseQuadSize * newScale;
    }

    @Override
    public void updateMotionXYZ() {
        super.updateMotionXYZ();
        yd = yd - 0.03;
    }

    public record Factory(SpriteSet spriteSet) implements ParticleProvider<SimpleParticleType> {
        private static final Map<TagKey<Biome>, ParticleColor> TAG_TO_COLOR = ImmutableMap.<TagKey<Biome>, ParticleColor>builder()
                .put(BiomeTags.IS_TAIGA, ParticleColor.GREEN)
                .put(BiomeTags.IS_JUNGLE, ParticleColor.GREEN)
                .put(BiomeTags.IS_FOREST, ParticleColor.GREEN)
                .put(BiomeTags.IS_SAVANNA, ParticleColor.BROWN)
                .put(BiomeTags.IS_END, ParticleColor.GREY)
                .put(BiomeTags.IS_NETHER, ParticleColor.GREY)
                .put(BiomeTags.IS_BEACH, ParticleColor.BLUE)
                .put(BiomeTags.IS_OCEAN, ParticleColor.BLUE)
                .put(BiomeTags.IS_RIVER, ParticleColor.BLUE)
                .put(BiomeTags.IS_BADLANDS, ParticleColor.BROWN)
                .put(BiomeTags.IS_DEEP_OCEAN, ParticleColor.BLUE)
                .build();

        @Override
        public @Nullable Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, RandomSource random) {
            // Color the particle based on biome
            final Holder<Biome> biome = level.getBiome(new BlockPos((int) x, (int) y, (int) z));
            final ParticleColor particleColor = biome.tags()
                    .filter(TAG_TO_COLOR::containsKey)
                    .map(TAG_TO_COLOR::get)
                    .findFirst()
                    .orElse(ParticleColor.GREY);
            final DustCloudParticle particle = new DustCloudParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, spriteSet);
            particle.setSprite(spriteSet.get(particleColor.index, ParticleColor.values().length));
            return particle;
        }

        private enum ParticleColor {
            BLUE(1),
            BROWN(2),
            GREEN(3),
            GREY(4);

            private final int index;

            ParticleColor(int index) {
                this.index = index;
            }
        }
    }
}
