package com.dslovikosky.narnia.common.event.datagenproviders;

import com.dslovikosky.narnia.common.constants.*;
import com.dslovikosky.narnia.common.world.feature.WaterPoolFeature;
import com.dslovikosky.narnia.common.world.feature.WorldWoodTreeFeature;
import com.dslovikosky.narnia.common.world.generator.CharnChunkGenerator;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.data.worldgen.biome.OverworldBiomes;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TimelineTags;
import net.minecraft.util.ARGB;
import net.minecraft.util.valueproviders.ClampedNormalInt;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.attribute.*;
import net.minecraft.world.clock.WorldClock;
import net.minecraft.world.clock.WorldClocks;
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
import net.minecraft.world.level.levelgen.placement.*;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.timeline.Timeline;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.world.BiomeModifiers;
import net.neoforged.neoforge.common.world.NeoForgeEnvironmentAttributes;
import net.neoforged.neoforge.data.event.GatherDataRegistryEntriesEvent;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.neoforged.neoforge.registries.holdersets.OrHolderSet;

import java.awt.*;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.dslovikosky.narnia.client.constants.ModRenderers.CHARN_SKY_RENDERER_ID;

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
                    // An empty timeline set means the dimension runs no day/night cycle, which is how a fixed
                    // time of day is expressed
                    final HolderGetter<Block> blocks = bootstrap.lookup(Registries.BLOCK);
                    final HolderGetter<Timeline> timelines = bootstrap.lookup(Registries.TIMELINE);
                    final HolderGetter<WorldClock> worldClocks = bootstrap.lookup(Registries.WORLD_CLOCK);
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
                                    // A 0 degree sun angle is noon
                                    .set(EnvironmentAttributes.SUN_ANGLE, 0.0F)
                                    // See the Charn note below: the ambient lightmap floor defaults to black
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
                                    // The lightmap's ambient floor is per-dimension data defaulting to pure
                                    // black. Without this, Charn is literally rgb(0,0,0) wherever block light
                                    // does not reach, since it has no sky light either. This matches the value
                                    // vanilla gives the overworld.
                                    .set(EnvironmentAttributes.AMBIENT_LIGHT_COLOR, ARGB.vector3fFromRGB24(ARGB.color(10, 10, 10)))
                                    // Charn's sunset fan is always this colour, regardless of time of day
                                    .set(EnvironmentAttributes.SUNRISE_SUNSET_COLOR, ARGB.vector4fFromARGB32(ARGB.color(50, 230, 16, 16)))
                                    .set(EnvironmentAttributes.BED_RULE, BedRule.DESTROY_ON_USE)
                                    .set(EnvironmentAttributes.RESPAWN_ANCHOR_WORKS, true)
                                    .set(EnvironmentAttributes.CAN_START_RAID, false)
                                    .build(),
                            HolderSet.direct(),
                            Optional.empty()
                    ));
                    // A deliberate copy of vanilla's overworld dimension type. Every dynamic pool world shares this one type
                    bootstrap.register(ModDimensionTypes.DYNAMIC_POOL_WORLD, new DimensionType(
                            false,
                            true,
                            false,
                            false,
                            1.0,
                            -64,
                            384,
                            384,
                            blocks.getOrThrow(BlockTags.INFINIBURN_OVERWORLD),
                            0.0f,
                            new DimensionType.MonsterSettings(UniformInt.of(0, 7), 0),
                            DimensionType.Skybox.OVERWORLD,
                            CardinalLighting.Type.DEFAULT,
                            EnvironmentAttributeMap.builder()
                                    .set(EnvironmentAttributes.FOG_COLOR, ARGB.vector3fFromRGB24(-4138753))
                                    .set(EnvironmentAttributes.SKY_COLOR, ARGB.vector3fFromRGB24(OverworldBiomes.calculateSkyColor(0.8F)))
                                    .set(EnvironmentAttributes.AMBIENT_LIGHT_COLOR, ARGB.vector3fFromRGB24(-16119286))
                                    .set(EnvironmentAttributes.CLOUD_COLOR, ARGB.vector4fFromARGB32(ARGB.white(0.8F)))
                                    .set(EnvironmentAttributes.CLOUD_HEIGHT, 192.33F)
                                    .set(EnvironmentAttributes.BACKGROUND_MUSIC, BackgroundMusic.OVERWORLD)
                                    .set(EnvironmentAttributes.BED_RULE, BedRule.CAN_SLEEP_WHEN_DARK)
                                    .set(EnvironmentAttributes.STRAW_BED_RULE, BedRule.DESTROY_ON_LEAVE)
                                    .set(EnvironmentAttributes.RESPAWN_ANCHOR_WORKS, false)
                                    .set(EnvironmentAttributes.NETHER_PORTAL_SPAWNS_PIGLINS, true)
                                    .set(EnvironmentAttributes.AMBIENT_SOUNDS, AmbientSounds.LEGACY_CAVE_SETTINGS)
                                    .build(),
                            timelines.getOrThrow(TimelineTags.IN_OVERWORLD),
                            Optional.of(worldClocks.getOrThrow(WorldClocks.OVERWORLD))
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
                            .putAttributes(EnvironmentAttributeMap.builder()
                                    .set(EnvironmentAttributes.FOG_COLOR, ARGB.vector3fFromRGB24(new Color(72, 199, 255, 0).getRGB()))
                                    .set(EnvironmentAttributes.SKY_COLOR, ARGB.vector3fFromRGB24(new Color(80, 188, 255, 0).getRGB()))
                                    .set(EnvironmentAttributes.WATER_FOG_COLOR, ARGB.vector3fFromRGB24(new Color(13, 94, 115, 0).getRGB()))
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
                .lootTable(new LootTableProvider.SubProviderEntry(ModBlockLootTableSubProvider::new, LootContextParamSets.BLOCK))
                .recipe(ModRecipeProvider::new);
    }
}
