package com.dslovikosky.narnia.common.event.datagenproviders;

import com.dslovikosky.narnia.common.constants.ModBiomeModifiers;
import com.dslovikosky.narnia.common.constants.ModBiomes;
import com.dslovikosky.narnia.common.constants.ModBlocks;
import com.dslovikosky.narnia.common.constants.ModConfiguredFeatures;
import com.dslovikosky.narnia.common.constants.ModDimensionTypes;
import com.dslovikosky.narnia.common.constants.ModFeatures;
import com.dslovikosky.narnia.common.constants.ModLevelStems;
import com.dslovikosky.narnia.common.constants.ModPlacedFeatures;
import com.dslovikosky.narnia.common.world.feature.WaterPoolFeature;
import com.dslovikosky.narnia.common.world.feature.WorldWoodTreeConfiguration;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.Vec3i;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.ClampedNormalInt;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.biome.BiomeSpecialEffects;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.FlatLevelSource;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.flat.FlatLayerInfo;
import net.minecraft.world.level.levelgen.flat.FlatLevelGeneratorSettings;
import net.minecraft.world.level.levelgen.heightproviders.ConstantHeight;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.BlockPredicateFilter;
import net.minecraft.world.level.levelgen.placement.CountPlacement;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.HeightmapPlacement;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.RandomOffsetPlacement;
import net.minecraft.world.level.levelgen.placement.RarityFilter;
import net.minecraft.world.level.levelgen.placement.SurfaceWaterDepthFilter;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.world.BiomeModifiers;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.neoforged.neoforge.registries.holdersets.OrHolderSet;

import java.awt.Color;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.OptionalLong;

import static com.dslovikosky.narnia.client.constants.ModRenderers.CHARN_SKY_RENDERER_ID;

public class ModDatapackObjectProvider {
    @SubscribeEvent
    public void onDataGatherEvent(final GatherDataEvent.Client event) {
        final RegistrySetBuilder builder = new RegistrySetBuilder()
                .add(NeoForgeRegistries.Keys.BIOME_MODIFIERS, bootstrap -> {
                    final HolderGetter<Biome> biomes = bootstrap.lookup(Registries.BIOME);
                    final HolderGetter<PlacedFeature> placedFeatures = bootstrap.lookup(Registries.PLACED_FEATURE);
                    bootstrap.register(ModBiomeModifiers.SCATTERED_WORLD_WOOD_TREES,
                            new BiomeModifiers.AddFeaturesBiomeModifier(
                                    new OrHolderSet<>(biomes.getOrThrow(Tags.Biomes.IS_FOREST), biomes.getOrThrow(Tags.Biomes.IS_PLAINS)),
                                    HolderSet.direct(placedFeatures.getOrThrow(ModPlacedFeatures.SPARSE_WORLD_WOOD_TREES)),
                                    GenerationStep.Decoration.VEGETAL_DECORATION
                            )
                    );
                })
                .add(Registries.DIMENSION_TYPE, bootstrap -> {
                    bootstrap.register(ModDimensionTypes.WOOD_BETWEEN_THE_WORLDS, new DimensionType(
                            OptionalLong.of(6000),
                            true,
                            false,
                            false,
                            false,
                            1.0,
                            true,
                            true,
                            0,
                            128,
                            64,
                            BlockTags.INFINIBURN_OVERWORLD,
                            BuiltinDimensionTypes.OVERWORLD_EFFECTS,
                            1.0f,
                            Optional.empty(),
                            new DimensionType.MonsterSettings(false, false, ConstantInt.of(0), 0)
                    ));
                    bootstrap.register(ModDimensionTypes.DARK_CITY_RUINS, new DimensionType(
                            OptionalLong.empty(),
                            false,
                            false,
                            false,
                            false,
                            1.0,
                            false,
                            true,
                            0,
                            128,
                            64,
                            BlockTags.INFINIBURN_OVERWORLD,
                            CHARN_SKY_RENDERER_ID,
                            0.0f,
                            Optional.empty(),
                            new DimensionType.MonsterSettings(false, false, ConstantInt.of(0), 0)
                    ));
                })
                .add(Registries.BIOME, bootstrap -> {
                    final HolderGetter<PlacedFeature> placedFeatures = bootstrap.lookup(Registries.PLACED_FEATURE);
                    final HolderGetter<ConfiguredWorldCarver<?>> configuredWorldCarver = bootstrap.lookup(Registries.CONFIGURED_CARVER);
                    bootstrap.register(ModBiomes.WOOD_BETWEEN_THE_WORLDS, new Biome.BiomeBuilder()
                            .hasPrecipitation(false)
                            .temperature(0.7f)
                            .temperatureAdjustment(Biome.TemperatureModifier.NONE)
                            .downfall(0.5f)
                            .specialEffects(new BiomeSpecialEffects.Builder()
                                    .fogColor(new Color(72, 199, 255, 0).getRGB())
                                    .skyColor(new Color(80, 188, 255, 0).getRGB())
                                    .waterColor(new Color(23, 209, 255, 0).getRGB())
                                    .waterFogColor(new Color(13, 94, 115, 0).getRGB())
                                    .foliageColorOverride(new Color(43, 199, 23, 0).getRGB())
                                    .grassColorOverride(new Color(43, 199, 23, 0).getRGB())
                                    .grassColorModifier(BiomeSpecialEffects.GrassColorModifier.NONE)
                                    .build())
                            .generationSettings(new BiomeGenerationSettings.Builder(placedFeatures, configuredWorldCarver)
                                    .addFeature(GenerationStep.Decoration.FLUID_SPRINGS, ModPlacedFeatures.WOOD_BETWEEN_THE_WORLDS_WATER_POOL)
                                    .addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.FOREST_FLOWERS)
                                    .addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.FLOWER_DEFAULT)
                                    .addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_GRASS_FOREST)
                                    .addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.BROWN_MUSHROOM_NORMAL)
                                    .addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.RED_MUSHROOM_NORMAL)
                                    .addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ModPlacedFeatures.WOOD_BETWEEN_THE_WORLDS_TREES)
                                    .build())
                            .mobSpawnSettings(new MobSpawnSettings.Builder()
                                    .creatureGenerationProbability(0f)
                                    .build())
                            .build());
                    bootstrap.register(ModBiomes.DARK_CITY_RUINS, new Biome.BiomeBuilder()
                            .hasPrecipitation(false)
                            .temperature(0.0f)
                            .temperatureAdjustment(Biome.TemperatureModifier.NONE)
                            .downfall(0.0f)
                            .specialEffects(new BiomeSpecialEffects.Builder()
                                    .fogColor(new Color(50, 0, 0, 0).getRGB())
                                    .skyColor(new Color(0, 0, 0, 0).getRGB())
                                    .waterColor(new Color(50, 0, 0, 0).getRGB())
                                    .waterFogColor(new Color(50, 0, 0, 0).getRGB())
                                    .foliageColorOverride(new Color(44, 0, 23, 0).getRGB())
                                    .grassColorOverride(new Color(44, 0, 23, 0).getRGB())
                                    .grassColorModifier(BiomeSpecialEffects.GrassColorModifier.NONE)
                                    .build())
                            .generationSettings(new BiomeGenerationSettings.Builder(placedFeatures, configuredWorldCarver).build())
                            .mobSpawnSettings(new MobSpawnSettings.Builder()
                                    .creatureGenerationProbability(0f)
                                    .build())
                            .build());
                })
                .add(Registries.LEVEL_STEM, bootstrap -> {
                    final HolderGetter<Biome> biomes = bootstrap.lookup(Registries.BIOME);
                    final HolderGetter<DimensionType> dimensionTypes = bootstrap.lookup(Registries.DIMENSION_TYPE);

                    // Wood between the Worlds
                    final List<FlatLayerInfo> wbwFlatLayerInfos = List.of(
                            new FlatLayerInfo(1, Blocks.BEDROCK),
                            new FlatLayerInfo(30, Blocks.DIRT),
                            new FlatLayerInfo(1, Blocks.GRASS_BLOCK));
                    final FlatLevelGeneratorSettings wbwFlatLevelGeneratorSettings =
                            new FlatLevelGeneratorSettings(Optional.of(HolderSet.direct()), biomes.getOrThrow(ModBiomes.WOOD_BETWEEN_THE_WORLDS), Collections.emptyList())
                                    .withBiomeAndLayers(wbwFlatLayerInfos, Optional.empty(), biomes.getOrThrow(ModBiomes.WOOD_BETWEEN_THE_WORLDS));
                    wbwFlatLevelGeneratorSettings.setDecoration();
                    final FlatLevelSource wbwFlatLevelSource = new FlatLevelSource(wbwFlatLevelGeneratorSettings);
                    bootstrap.register(ModLevelStems.WOOD_BETWEEN_THE_WORLDS,
                            new LevelStem(dimensionTypes.getOrThrow(ModDimensionTypes.WOOD_BETWEEN_THE_WORLDS), wbwFlatLevelSource));

                    // Dark City Ruins
                    final List<FlatLayerInfo> dcrFlatLayerInfos = List.of(
                            new FlatLayerInfo(1, Blocks.BEDROCK),
                            new FlatLayerInfo(30, Blocks.SANDSTONE),
                            new FlatLayerInfo(1, Blocks.SAND));
                    final FlatLevelGeneratorSettings dcrFlatLevelGeneratorSettings =
                            new FlatLevelGeneratorSettings(Optional.of(HolderSet.direct()), biomes.getOrThrow(ModBiomes.DARK_CITY_RUINS), Collections.emptyList())
                                    .withBiomeAndLayers(dcrFlatLayerInfos, Optional.empty(), biomes.getOrThrow(ModBiomes.DARK_CITY_RUINS));
                    final FlatLevelSource dcrFlatLevelSource = new FlatLevelSource(dcrFlatLevelGeneratorSettings);
                    bootstrap.register(ModLevelStems.DARK_CITY_RUINS,
                            new LevelStem(dimensionTypes.getOrThrow(ModDimensionTypes.DARK_CITY_RUINS), dcrFlatLevelSource));
                })
                .add(Registries.CONFIGURED_FEATURE, bootstrap -> {
                    bootstrap.register(ModConfiguredFeatures.SMALL_WATER_POOL, new ConfiguredFeature<>(
                            ModFeatures.WATER_POOL.get(),
                            new WaterPoolFeature.Configuration(
                                    BlockStateProvider.simple(Blocks.WATER),
                                    ClampedNormalInt.of(8f, 1f, 6, 10),
                                    ClampedNormalInt.of(8f, 1f, 6, 10),
                                    ClampedNormalInt.of(10f, 1f, 8, 12)
                            )));
                    bootstrap.register(ModConfiguredFeatures.WORLD_WOOD_TREE, new ConfiguredFeature<>(
                            ModFeatures.WORLD_WOOD_TREE.get(),
                            new WorldWoodTreeConfiguration(
                                    UniformInt.of(3, 24),
                                    BlockStateProvider.simple(ModBlocks.WORLD_WOOD_LOG.get()
                                            .defaultBlockState()
                                            .setValue(RotatedPillarBlock.AXIS, Direction.Axis.Y)),
                                    BlockStateProvider.simple(ModBlocks.WORLD_WOOD_LEAVES.get()
                                            .defaultBlockState()
                                            .setValue(LeavesBlock.DISTANCE, 7)
                                            .setValue(LeavesBlock.PERSISTENT, false)
                                            .setValue(LeavesBlock.WATERLOGGED, false))
                            )));
                })
                .add(Registries.PLACED_FEATURE, bootstrap -> {
                    final HolderGetter<ConfiguredFeature<?, ?>> configuredFeatures = bootstrap.lookup(Registries.CONFIGURED_FEATURE);
                    bootstrap.register(ModPlacedFeatures.SPARSE_WORLD_WOOD_TREES, new PlacedFeature(
                            configuredFeatures.getOrThrow(ModConfiguredFeatures.WORLD_WOOD_TREE),
                            List.of(
                                    CountPlacement.of(1),
                                    RandomOffsetPlacement.of(UniformInt.of(6, 10), UniformInt.of(6, 10)),
                                    RarityFilter.onAverageOnceEvery(30),
                                    SurfaceWaterDepthFilter.forMaxDepth(0),
                                    HeightmapPlacement.onHeightmap(Heightmap.Types.OCEAN_FLOOR),
                                    BlockPredicateFilter.forPredicate(BlockPredicate.wouldSurvive(ModBlocks.WORLD_WOOD_SAPLING.get().defaultBlockState(), Vec3i.ZERO))
                            )));
                    bootstrap.register(ModPlacedFeatures.WOOD_BETWEEN_THE_WORLDS_TREES, new PlacedFeature(
                            configuredFeatures.getOrThrow(ModConfiguredFeatures.WORLD_WOOD_TREE),
                            List.of(
                                    CountPlacement.of(6),
                                    InSquarePlacement.spread(),
                                    HeightRangePlacement.of(ConstantHeight.of(VerticalAnchor.absolute(31))),
                                    BiomeFilter.biome(),
                                    SurfaceWaterDepthFilter.forMaxDepth(0)
                            )));
                    bootstrap.register(ModPlacedFeatures.WOOD_BETWEEN_THE_WORLDS_WATER_POOL, new PlacedFeature(
                            configuredFeatures.getOrThrow(ModConfiguredFeatures.SMALL_WATER_POOL),
                            List.of(
                                    RandomOffsetPlacement.of(UniformInt.of(3, 13), ConstantInt.of(0)),
                                    HeightmapPlacement.onHeightmap(Heightmap.Types.WORLD_SURFACE_WG)
                            )));
                });
        event.createDatapackRegistryObjects(builder);
    }
}
