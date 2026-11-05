package com.dslovikosky.narnia.common.constants;

import com.dslovikosky.narnia.common.world.feature.WaterPoolFeature;
import com.dslovikosky.narnia.common.world.feature.WorldWoodTreeFeature;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * 26.3 split the old feature registry in two. A feature type is just the codec that deserializes a feature,
 * and lives here in the static worldgen/feature_type registry. The features themselves are datapack objects
 * now, keyed from {@link ModFeatures}, and carry the settings that used to sit in a separate configuration.
 */
public class ModFeatureTypes {
    public static final DeferredRegister<MapCodec<? extends Feature>> FEATURE_TYPES = DeferredRegister.create(Registries.FEATURE_TYPE, Constants.MOD_ID);

    public static final DeferredHolder<MapCodec<? extends Feature>, MapCodec<WaterPoolFeature>> WATER_POOL = FEATURE_TYPES.register("water_pool", it -> WaterPoolFeature.CODEC);
    public static final DeferredHolder<MapCodec<? extends Feature>, MapCodec<WorldWoodTreeFeature>> WORLD_WOOD_TREE = FEATURE_TYPES.register("world_wood_tree", it -> WorldWoodTreeFeature.CODEC);
}
