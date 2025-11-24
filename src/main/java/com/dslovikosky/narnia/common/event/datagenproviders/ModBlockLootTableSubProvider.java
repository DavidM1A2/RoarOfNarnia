package com.dslovikosky.narnia.common.event.datagenproviders;

import com.dslovikosky.narnia.common.constants.ModBlocks;
import com.dslovikosky.narnia.common.constants.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.predicates.BonusLevelTableCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class ModBlockLootTableSubProvider extends BlockLootSubProvider {
    // Order is Fortune 0, 1, 2, 3, 4+
    private static final float[] WORLD_WOOD_LEAVES_DUST_CHANCES = new float[]{0.1F, 0.15F, 0.2F, 0.25F, 0.3F};

    protected ModBlockLootTableSubProvider(final HolderLookup.Provider lookupProvider) {
        super(Set.of(), FeatureFlags.DEFAULT_FLAGS, lookupProvider);
    }

    @Override
    protected @NotNull Iterable<Block> getKnownBlocks() {
        return ModBlocks.BLOCKS.getEntries().stream().map(holder -> (Block) holder.value()).toList();
    }

    @Override
    protected void generate() {
        final HolderLookup.RegistryLookup<Enchantment> registrylookup = this.registries.lookupOrThrow(Registries.ENCHANTMENT);

        dropSelf(ModBlocks.WORLD_WOOD.get());
        dropSelf(ModBlocks.STRIPPED_WORLD_WOOD.get());
        dropSelf(ModBlocks.WORLD_WOOD_LOG.get());
        dropSelf(ModBlocks.STRIPPED_WORLD_WOOD_LOG.get());
        dropSelf(ModBlocks.WORLD_WOOD_PLANKS.get());
        add(ModBlocks.WORLD_WOOD_DOOR.get(), createDoorTable(ModBlocks.WORLD_WOOD_DOOR.get()));
        dropSelf(ModBlocks.WORLD_WOOD_FENCE.get());
        dropSelf(ModBlocks.WORLD_WOOD_FENCE_GATE.get());
        dropSelf(ModBlocks.WORLD_WOOD_STAIR.get());
        dropSelf(ModBlocks.WORLD_WOOD_PRESSURE_PLATE.get());
        dropSelf(ModBlocks.WORLD_WOOD_STANDING_SIGN.get());
        dropSelf(ModBlocks.WORLD_WOOD_WALL_SIGN.get());
        dropSelf(ModBlocks.WORLD_WOOD_WALL_HANGING_SIGN.get());
        dropSelf(ModBlocks.WORLD_WOOD_CEILING_HANGING_SIGN.get());
        dropSelf(ModBlocks.WORLD_WOOD_BUTTON.get());
        add(ModBlocks.WORLD_WOOD_LEAVES.get(), createLeavesDrops(ModBlocks.WORLD_WOOD_LEAVES.get(), ModBlocks.WORLD_WOOD_SAPLING.get(), NORMAL_LEAVES_SAPLING_CHANCES)
                .withPool(LootPool.lootPool()
                        .when(doesNotHaveShearsOrSilkTouch().and(BonusLevelTableCondition.bonusLevelFlatChance(registrylookup.getOrThrow(Enchantments.FORTUNE), WORLD_WOOD_LEAVES_DUST_CHANCES)))
                        .setRolls(ConstantValue.exactly(1.0F))
                        .add(LootItem.lootTableItem(ModItems.SPARKLING_DUST))));
        dropSelf(ModBlocks.WORLD_WOOD_SAPLING.get());
        add(ModBlocks.WORLD_WOOD_SLAB.get(), createSlabItemTable(ModBlocks.WORLD_WOOD_SLAB.get()));
        dropSelf(ModBlocks.WORLD_WOOD_TRAP_DOOR.get());

        dropSelf(ModBlocks.DARK_CITY_COBBLESTONE.get());
        dropSelf(ModBlocks.DARK_CITY_SMOOTH_STONE.get());
        dropSelf(ModBlocks.DARK_CITY_SMOOTH_STONE_SLAB.get());
        dropSelf(ModBlocks.DARK_CITY_WINDOW.get());
        add(ModBlocks.DARK_CITY_DOOR.get(), createDoorTable(ModBlocks.DARK_CITY_DOOR.get()));

        dropSelf(ModBlocks.DARK_CITY_STONE_BRICKS.get());
        dropSelf(ModBlocks.DARK_CITY_STONE_BRICK_STAIRS.get());
        add(ModBlocks.DARK_CITY_STONE_BRICK_SLAB.get(), createSlabItemTable(ModBlocks.DARK_CITY_STONE_BRICK_SLAB.get()));
        dropSelf(ModBlocks.DARK_CITY_STONE_BRICK_WALL.get());
        dropSelf(ModBlocks.MOSSY_DARK_CITY_STONE_BRICKS.get());
        dropSelf(ModBlocks.CHISELED_DARK_CITY_STONE_BRICKS.get());
        dropSelf(ModBlocks.DARK_CITY_STONE_BRICK_TRAPDOOR.get());

        dropSelf(ModBlocks.DARK_CITY_SLATE.get());
        dropSelf(ModBlocks.DARK_CITY_SLATE_SLAB.get());
        dropSelf(ModBlocks.DARK_CITY_SLATE_STAIRS.get());

        add(ModBlocks.CHARN_BELL.get(), noDrop());
    }

    private LootItemCondition.Builder hasShearsOrSilkTouch() {
        return this.hasShears().or(this.hasSilkTouch());
    }

    private LootItemCondition.Builder doesNotHaveShearsOrSilkTouch() {
        return this.hasShearsOrSilkTouch().invert();
    }
}
