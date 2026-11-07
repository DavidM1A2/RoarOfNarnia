# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project

Roar of Narnia — a NeoForge Minecraft mod that lets players play through C.S. Lewis's "The Chronicles of Narnia"
inside Minecraft. Mod id `narnia`, base package `com.dslovikosky.narnia`.

## Commands

All commands use the Gradle wrapper (`./gradlew`, or `gradlew.bat` on Windows cmd).

| Task | Command |
| --- | --- |
| Compile + build the jar | `./gradlew build` |
| Run the client | `./gradlew runClient` |
| Run a dedicated server | `./gradlew runServer` |
| Run data generation | `./gradlew runClientData` |
| Run all gametests headlessly | `./gradlew runGameTestServer` |
| Reset caches after a dependency problem | `./gradlew --refresh-dependencies`, then `./gradlew clean` |

There is no unit test source set and no linter configured — `build` (compilation) is the whole check, and it is also
all CI runs (`.github/workflows/build.yml`). Tests, if added, are NeoForge **gametests**: they run in-game, are
registered under the `narnia` namespace, and can be invoked from a running client/server with `/test`.

`runClientData` writes into `src/generated/resources`, which is gitignored but added to the main resource set by
`build.gradle.kts`. Never hand-edit files there — change the provider and re-run datagen.

Java toolchain is **25** (Mojang ships Java 25 to users as of 26.1.2). The IntelliJ run configurations the README
mentions are the ones NeoGradle generates from the `runs { }` block.

## Version branches

This repo keeps one long-lived branch per Minecraft version: `MC-1.21.4` (the default/PR target), `MC-1.21.8`,
`MC-1.21.10`, `MC-26.3`, etc. Work on a version branch targets exactly the Minecraft/NeoForge pair in
`gradle.properties` (`minecraft_version`, `neo_version`). Migrating to a new Minecraft version means a new
`MC-*` branch, so expect vanilla API churn between branches and do not assume a signature seen in one branch exists
in another.

`gradle.properties` is the single source of truth for mod metadata: `mod_id`, `mod_version`, `mod_name`, etc. are
interpolated into `META-INF/neoforge.mods.toml` by the `ProcessResources` task, so change them there, not in the toml.

## Architecture

### Registration

`RoarOfNarnia.java` (the `@Mod` entrypoint) is the complete wiring map for the mod: every event handler and every
`DeferredRegister` is registered there, split into mod-bus vs `NeoForge.EVENT_BUS` and gated on
`FMLLoader.getCurrent().getDist() == Dist.CLIENT` for client-only pieces. **A new handler or registry is not live
until it is added to that constructor.**

Registry contents live in `common/constants/Mod*.java`, one class per registry, each holding a
`DeferredRegister` plus `public static final DeferredHolder` fields. Classes without a `DeferredRegister`
(`ModDimensions`, `ModBiomes`, `ModBlockTags`, …) just hold `ResourceKey`/`TagKey` constants for datapack-defined
objects. Use `Constants.modLocation(path)` for every `Identifier` rather than building one by hand.

The mod defines its **own** registries in `ModRegistries` — `schematic`, `spell_power_sources`,
`spell_delivery_methods`, `spell_effects` — created via `RegistryBuilder` and handed to `NewRegistryEvent` by
`RegistryRegister`. Adding another custom registry means adding it to `ModRegistries.REGISTRIES`.

### Spell system

The largest subsystem, under `common/spell`. A `Spell` is a list of `SpellStage`s; each stage has one
`SpellDeliveryMethod` and up to four `SpellEffect`s. Casting goes through a `SpellPowerSource`, which pays the cost
computed from the delivery method and effects and decides success/failure.

The key split is **component vs instance**: `SpellPowerSource` / `SpellDeliveryMethod` / `SpellEffect` are registry
singletons describing behavior; `SpellComponentInstance` holds the per-spell mutable configuration (its properties,
from `component/property`). Code that needs behavior calls `instance.getComponent()`; code that needs tuning reads
the instance. Spells serialize to NBT by hand (`serializeNbt` / `CompoundTag` constructors) with `NBT_*` string
constants — follow that convention rather than introducing a codec for these types.

Per-power-source gameplay logic lives in sibling `Spell*Handler` classes on the Forge bus
(`SpellInnateHandler`, `SpellLunarHandler`, `SpellSolarHandler`, `SpellThermalHandler`, `SpellCharmHandler`,
`SpellFreezeHandler`, plus `DelayedDeliveryEntryHandler` for delayed delivery).

### Player/entity data and networking

Mutable per-entity state uses NeoForge **attachments**, declared in `ModAttachmentTypes` with a `Codec` for
persistence and an optional sync handler (e.g. `SelectedSpellPowerSourceSyncHandler`) for client replication;
attachment payload classes live in `common/model/attachment_type`. Prefer an attachment with a sync handler over a
new packet. Explicit packets are rare and registered in `PacketRegistrationHandler` as
`Packet` + `PacketHandler` pairs under `common/network/packet`.

### Worldgen and schematics

Custom dimensions (`wood_between_the_worlds`, `dark_city_ruins`) are datapack objects whose keys sit in
`ModDimensions` / `ModLevelStems` and whose definitions are generated by `ModDatapackObjectProvider`. Charn's terrain
comes from a hand-written `CharnChunkGenerator` with its passes split into `world/generator/charn/`
(`CharnTerrain`, `CharnRivers`, `CharnRoads`, `CharnBuildings`).

Buildings are **schematics**: entries registered in `ModSchematics` against the custom `schematic` registry, backed
by Sponge-format `.nbt` files in `data/narnia/schematic/`. `SchematicManager` is a reload listener that loads every
registered schematic's blocks/entities into a `SchematicData` on resource reload; `SchematicStructurePiece` and
`GridStructurePlacement` place them during generation. Adding a building = drop the `.nbt` in, register it in
`ModSchematics`.

### Data generation

Two event handlers, both on the mod bus:

- `DataGenerationHandler` (`GatherDataEvent.Client`) → asset/tag providers in `common/event/datagenproviders`
  (language, models, sounds, block/item tags, particles, data maps).
- `ModDatapackObjectProvider` (`GatherDataRegistryEntriesEvent`) → datapack registry contents (biomes, biome
  modifiers, dimension types, level stems, features, placed features, damage types, structures) and, chained off the
  same builder, loot tables (`ModBlockLootTableSubProvider`) and recipes (`ModRecipeProvider`).

### Client GUI framework

`client/gui` is a bespoke retained-mode UI layer, not vanilla widgets. `GuiComponent` is the base node (offset,
preferred size, margins, gravity, hover text, color) with `draw` / `update`; `control/` holds panes
(`StackPane`, `HChainPane`/`VChainPane`, `RatioPane`, `RadialPane`) and leaf components. `BaseScreen` owns a root
`StackPane`, sized in "MC coords" via `GuiUtility`, and re-runs a `negotiateDimensions` → `update` layout pass when
invalidated or when the window resizes — call `invalidate()` after changing anything that affects layout rather than
mutating positions directly. Screens (`SpellwrightingScreen`, `PowerSourceSelectionScreen`) build component trees in
their constructors.

Other client pieces: renderers registered by `*RendererRegister` handlers, render pipelines/shaders in
`client/constants/ModRenderPipelines` + `assets/narnia/shaders`, and `ClientProxy` for client singletons such as the
Charn screen-shake handler.

Entity models authored in Blockbench live in `models/*.bbmodel` at the repo root — the source of truth for the
corresponding Java model classes.
