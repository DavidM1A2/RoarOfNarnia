package com.dslovikosky.narnia.common.event.datagenproviders;

import com.dslovikosky.narnia.common.constants.ModBiomeModifiers;
import com.dslovikosky.narnia.common.constants.ModBiomes;
import com.dslovikosky.narnia.common.constants.ModBlocks;
import com.dslovikosky.narnia.common.constants.ModDamageTypes;
import com.dslovikosky.narnia.common.constants.ModDimensionTypes;
import com.dslovikosky.narnia.common.constants.ModFeatures;
import com.dslovikosky.narnia.common.constants.ModLevelStems;
import com.dslovikosky.narnia.common.constants.ModPlacedFeatures;
import com.dslovikosky.narnia.common.world.feature.WaterPoolFeature;
import com.dslovikosky.narnia.common.world.feature.WorldWoodTreeFeature;
import com.dslovikosky.narnia.common.world.generator.CharnChunkGenerator;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.ARGB;
import net.minecraft.util.valueproviders.ClampedNormalInt;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.attribute.BedRule;
import net.minecraft.world.attribute.EnvironmentAttributeMap;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.level.CardinalLighting;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.biome.BiomeSpecialEffects;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.FlatLevelSource;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.carver.WorldCarver;
import net.minecraft.world.level.levelgen.feature.Feature;
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
import net.minecraft.world.level.levelgen.placement.OffsetPlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.RarityFilter;
import net.minecraft.world.level.levelgen.placement.SurfaceWaterDepthFilter;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.world.BiomeModifiers;
import net.neoforged.neoforge.common.world.NeoForgeEnvironmentAttributes;
import net.neoforged.neoforge.data.event.GatherDataRegistryEntriesEvent;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.neoforged.neoforge.registries.holdersets.OrHolderSet;

import java.awt.Color;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.dslovikosky.narnia.client.constants.ModRenderers.CHARN_SKY_RENDERER_ID;

/**
 * 26.3 bootstraps datapack registry entries through GatherDataRegistryEntriesEvent rather than handing a
 * RegistrySetBuilder to the data generator. The gatherer sorts entries into the world and reloadable registry
 * layers itself, so recipes and loot tables can be gathered alongside biomes and features.
 */
public class ModDatapackObjectProvider {
    @SubscribeEvent
    public void onGatherDataRegistryEntriesEvent(final GatherDataRegistryEntriesEvent event) {
        event
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
                    // 1.21.11 replaced the fixed-time/bed/anchor flags and the effects location with the
                    // environment attribute system. An empty timeline set means the dimension does not run a
                    // day/night cycle, which is how a fixed time of day is now expressed.
                    // 26.2 takes infiniburn as a resolved HolderSet rather than a TagKey
                    final HolderGetter<Block> blocks = bootstrap.lookup(Registries.BLOCK);
                    bootstrap.register(ModDimensionTypes.WOOD_BETWEEN_THE_WORLDS, new DimensionType(
                            true,
                            true,
                            false,
                            false,
                            1.0,
                            0,
                            128,
                            64,
                            blocks.getOrThrow(BlockTags.INFINIBURN_OVERWORLD),
                            1.0f,
                            new DimensionType.MonsterSettings(ConstantInt.of(0), 0),
                            DimensionType.Skybox.OVERWORLD,
                            CardinalLighting.Type.DEFAULT,
                            EnvironmentAttributeMap.builder()
                                    // The old fixed time of 6000 ticks is noon, which the day timeline maps to a 0 degree sun angle
                                    .set(EnvironmentAttributes.SUN_ANGLE, 0.0F)
                                    // See the Charn note below: 26.1 defaults the ambient lightmap floor to black
                                    .set(EnvironmentAttributes.AMBIENT_LIGHT_COLOR, ARGB.vector3fFromRGB24(ARGB.color(10, 10, 10)))
                                    .set(EnvironmentAttributes.RESPAWN_ANCHOR_WORKS, true)
                                    .set(EnvironmentAttributes.CAN_START_RAID, false)
                                    .build(),
                            HolderSet.direct(),
                            Optional.empty()
                    ));
                    bootstrap.register(ModDimensionTypes.DARK_CITY_RUINS, new DimensionType(
                            false,
                            false,
                            false,
                            false,
                            1.0,
                            0,
                            256,
                            64,
                            blocks.getOrThrow(BlockTags.INFINIBURN_OVERWORLD),
                            0.0f,
                            new DimensionType.MonsterSettings(ConstantInt.of(0), 0),
                            // The custom skybox renderer draws Charn's sky in full, so vanilla contributes none of it
                            DimensionType.Skybox.NONE,
                            CardinalLighting.Type.DEFAULT,
                            EnvironmentAttributeMap.builder()
                                    .set(NeoForgeEnvironmentAttributes.CUSTOM_SKYBOX, CHARN_SKY_RENDERER_ID)
                                    // 26.1 turned the lightmap's ambient floor into per-dimension data that
                                    // defaults to pure black. Without this, Charn is literally rgb(0,0,0)
                                    // wherever block light does not reach, since it has no sky light either.
                                    // This matches the value vanilla gives the overworld.
                                    .set(EnvironmentAttributes.AMBIENT_LIGHT_COLOR, ARGB.vector3fFromRGB24(ARGB.color(10, 10, 10)))
                                    // Charn's sunset fan was previously always drawn in this colour regardless of time
                                    .set(EnvironmentAttributes.SUNRISE_SUNSET_COLOR, ARGB.vector4fFromARGB32(ARGB.color(50, 230, 16, 16)))
                                    .set(EnvironmentAttributes.BED_RULE, BedRule.DESTROY_ON_USE)
                                    .set(EnvironmentAttributes.RESPAWN_ANCHOR_WORKS, true)
                                    .set(EnvironmentAttributes.CAN_START_RAID, false)
                                    .build(),
                            HolderSet.direct(),
                            Optional.empty()
                    ));
                })
                .add(Registries.BIOME, bootstrap -> {
                    final HolderGetter<PlacedFeature> placedFeatures = bootstrap.lookup(Registries.PLACED_FEATURE);
                    final HolderGetter<WorldCarver> configuredWorldCarver = bootstrap.lookup(Registries.CARVER);
                    bootstrap.register(ModBiomes.WOOD_BETWEEN_THE_WORLDS, new Biome.BiomeBuilder()
                            .hasPrecipitation(false)
                            .temperature(0.7f)
                            .temperatureAdjustment(Biome.TemperatureModifier.NONE)
                            .downfall(0.5f)
                            // 1.21.11 moved the fog, sky and water fog colours out of BiomeSpecialEffects
                            // and into environment attributes
                            .putAttributes(EnvironmentAttributeMap.builder()
                                    .set(EnvironmentAttributes.FOG_COLOR, ARGB.vector3fFromRGB24(new Color(72, 199, 255, 0).getRGB()))
                                    .set(EnvironmentAttributes.SKY_COLOR, ARGB.vector3fFromRGB24(new Color(80, 188, 255, 0).getRGB()))
                                    .set(EnvironmentAttributes.WATER_FOG_COLOR, ARGB.vector3fFromRGB24(new Color(13, 94, 115, 0).getRGB()))
                                    // 26.3 moved the world gen creature spawn chance out of MobSpawnSettings into an attribute
                                    .set(EnvironmentAttributes.CREATURE_WORLD_GEN_SPAWN_PROBABILITY, 0.0F))
                            .specialEffects(new BiomeSpecialEffects.Builder()
                                    .waterColor(new Color(23, 209, 255, 0).getRGB())
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
                                    .build())
                            .build());
                    bootstrap.register(ModBiomes.DARK_CITY_RUINS, new Biome.BiomeBuilder()
                            .hasPrecipitation(false)
                            .temperature(0.0f)
                            .temperatureAdjustment(Biome.TemperatureModifier.NONE)
                            .downfall(0.0f)
                            .putAttributes(EnvironmentAttributeMap.builder()
                                    .set(EnvironmentAttributes.FOG_COLOR, ARGB.vector3fFromRGB24(new Color(50, 0, 0, 0).getRGB()))
                                    .set(EnvironmentAttributes.SKY_COLOR, ARGB.vector3fFromRGB24(new Color(0, 0, 0, 0).getRGB()))
                                    .set(EnvironmentAttributes.WATER_FOG_COLOR, ARGB.vector3fFromRGB24(new Color(50, 0, 0, 0).getRGB()))
                                    // 26.3 moved the world gen creature spawn chance out of MobSpawnSettings into an attribute
                                    .set(EnvironmentAttributes.CREATURE_WORLD_GEN_SPAWN_PROBABILITY, 0.0F))
                            .specialEffects(new BiomeSpecialEffects.Builder()
                                    .waterColor(new Color(50, 0, 0, 0).getRGB())
                                    .foliageColorOverride(new Color(44, 0, 23, 0).getRGB())
                                    .grassColorOverride(new Color(44, 0, 23, 0).getRGB())
                                    .grassColorModifier(BiomeSpecialEffects.GrassColorModifier.NONE)
                                    .build())
                            .generationSettings(new BiomeGenerationSettings.Builder(placedFeatures, configuredWorldCarver).build())
                            .mobSpawnSettings(new MobSpawnSettings.Builder()
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
                    final CharnChunkGenerator charnChunkGenerator = new CharnChunkGenerator(biomes.getOrThrow(ModBiomes.DARK_CITY_RUINS));
                    bootstrap.register(ModLevelStems.DARK_CITY_RUINS,
                            new LevelStem(dimensionTypes.getOrThrow(ModDimensionTypes.DARK_CITY_RUINS), charnChunkGenerator));
                })
                // 26.3 dropped ConfiguredFeature. A feature holds its own settings and is registered straight
                // into the feature registry, which is where the configured features used to live.
                .add(Registries.FEATURE, bootstrap -> {
                    bootstrap.register(ModFeatures.SMALL_WATER_POOL, new WaterPoolFeature(
                            BlockStateProvider.holderOf(Blocks.WATER),
                            ClampedNormalInt.of(8f, 1f, 6, 10),
                            ClampedNormalInt.of(8f, 1f, 6, 10),
                            ClampedNormalInt.of(10f, 1f, 8, 12)
                    ));
                    bootstrap.register(ModFeatures.WORLD_WOOD_TREE, new WorldWoodTreeFeature(
                            UniformInt.of(3, 24),
                            BlockStateProvider.holderOf(ModBlocks.WORLD_WOOD_LOG.get()
                                    .defaultBlockState()
                                    .setValue(RotatedPillarBlock.AXIS, Direction.Axis.Y)),
                            BlockStateProvider.holderOf(ModBlocks.WORLD_WOOD_LEAVES.get()
                                    .defaultBlockState()
                                    .setValue(LeavesBlock.DISTANCE, 7)
                                    .setValue(LeavesBlock.PERSISTENT, false)
                                    .setValue(LeavesBlock.WATERLOGGED, false))
                    ));
                })
                .add(Registries.PLACED_FEATURE, bootstrap -> {
                    final HolderGetter<Feature> configuredFeatures = bootstrap.lookup(Registries.FEATURE);
                    bootstrap.register(ModPlacedFeatures.SPARSE_WORLD_WOOD_TREES, new PlacedFeature(
                            configuredFeatures.getOrThrow(ModFeatures.WORLD_WOOD_TREE),
                            List.of(
                                    CountPlacement.of(1),
                                    OffsetPlacement.of(UniformInt.of(6, 10), UniformInt.of(6, 10)),
                                    RarityFilter.onAverageOnceEvery(30),
                                    SurfaceWaterDepthFilter.forMaxDepth(0),
                                    HeightmapPlacement.onHeightmap(Heightmap.Types.OCEAN_FLOOR),
                                    BlockPredicateFilter.forPredicate(BlockPredicate.wouldSurvive(ModBlocks.WORLD_WOOD_SAPLING.get()))
                            )));
                    bootstrap.register(ModPlacedFeatures.WOOD_BETWEEN_THE_WORLDS_TREES, new PlacedFeature(
                            configuredFeatures.getOrThrow(ModFeatures.WORLD_WOOD_TREE),
                            List.of(
                                    CountPlacement.of(6),
                                    InSquarePlacement.spread(),
                                    HeightRangePlacement.of(ConstantHeight.of(VerticalAnchor.absolute(31))),
                                    BiomeFilter.biome(),
                                    SurfaceWaterDepthFilter.forMaxDepth(0)
                            )));
                    bootstrap.register(ModPlacedFeatures.WOOD_BETWEEN_THE_WORLDS_WATER_POOL, new PlacedFeature(
                            configuredFeatures.getOrThrow(ModFeatures.SMALL_WATER_POOL),
                            List.of(
                                    OffsetPlacement.of(UniformInt.of(3, 13), ConstantInt.of(0)),
                                    HeightmapPlacement.onHeightmap(Heightmap.Types.WORLD_SURFACE_WG)
                            )));
                })
                .add(Registries.DAMAGE_TYPE, bootstrap -> {
                    bootstrap.register(ModDamageTypes.SPELL, new DamageType("narnia.spell", 0f));
                })
                // Loot tables and recipes became reloadable datapack registries in 26.3, so they are gathered
                // here rather than being registered as standalone data providers
                .lootTable(new LootTableProvider.SubProviderEntry(ModBlockLootTableSubProvider::new, LootContextParamSets.BLOCK))
                .recipe(ModRecipeProvider::new);
    }
}
