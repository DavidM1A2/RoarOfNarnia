package com.dslovikosky.narnia.common.world.feature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

public record WorldWoodTreeConfiguration(IntProvider height, BlockStateProvider trunkProvider, BlockStateProvider foliageProvider) implements FeatureConfiguration {
    public static final Codec<WorldWoodTreeConfiguration> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                            IntProvider.CODEC.fieldOf("height").forGetter(WorldWoodTreeConfiguration::height),
                            BlockStateProvider.CODEC.fieldOf("trunk_provider").forGetter(WorldWoodTreeConfiguration::trunkProvider),
                            BlockStateProvider.CODEC.fieldOf("foliage_provider").forGetter(WorldWoodTreeConfiguration::foliageProvider)
                    )
                    .apply(instance, WorldWoodTreeConfiguration::new)
    );
}
