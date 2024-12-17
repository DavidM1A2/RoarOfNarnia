package com.dslovikosky.narnia.common.event.datagenproviders;

import com.dslovikosky.narnia.common.constants.Constants;
import com.dslovikosky.narnia.common.constants.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.HangingSignItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SignItem;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.PressurePlateBlock;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.HashMap;
import java.util.Map;

public class ModItemModelProvider extends ItemModelProvider {
    private final Map<String, ResourceLocation> materialToTexture = new HashMap<>();

    public ModItemModelProvider(final PackOutput output, final ExistingFileHelper existingFileHelper) {
        super(output, Constants.MOD_ID, existingFileHelper);
        materialToTexture.put("world_wood", modLoc(BLOCK_FOLDER + "/world_wood_planks"));
    }

    @Override
    protected void registerModels() {
        ModItems.ITEMS.getEntries().forEach(deferredItem -> {
            final Item item = deferredItem.get();
            if (item instanceof BlockItem blockItem) {
                if (blockItem.getBlock() instanceof ButtonBlock) {
                    buttonInventory(deferredItem.getRegisteredName(), determineTexture(deferredItem));
                } else if (blockItem.getBlock() instanceof DoorBlock) {
                    basicItem(item);
                } else if (blockItem.getBlock() instanceof SaplingBlock) {
                    getBuilder(item.toString())
                            .parent(new ModelFile.UncheckedModelFile("item/generated"))
                            .texture("layer0", modLoc(BLOCK_FOLDER + "/" + deferredItem.getId().getPath()));
                } else if (blockItem.getBlock() instanceof FenceBlock) {
                    fenceInventory(deferredItem.getRegisteredName(), determineTexture(deferredItem));
                } else if (blockItem.getBlock() instanceof FenceGateBlock) {
                    fenceGate(deferredItem.getRegisteredName(), determineTexture(deferredItem));
                } else if (blockItem.getBlock() instanceof SlabBlock) {
                    slab(deferredItem.getRegisteredName(), determineTexture(deferredItem), determineTexture(deferredItem), determineTexture(deferredItem));
                } else if (blockItem.getBlock() instanceof StairBlock) {
                    stairs(deferredItem.getRegisteredName(), determineTexture(deferredItem), determineTexture(deferredItem), determineTexture(deferredItem));
                } else if (blockItem.getBlock() instanceof TrapDoorBlock) {
                    trapdoorBottom(deferredItem.getRegisteredName(), modLoc(BLOCK_FOLDER + "/" + deferredItem.getId().getPath()));
                } else if (blockItem.getBlock() instanceof PressurePlateBlock) {
                    pressurePlate(deferredItem.getRegisteredName(), determineTexture(deferredItem));
                } else if (item instanceof SignItem || item instanceof HangingSignItem) {
                    basicItem(item);
                } else {
                    withExistingParent(deferredItem.getRegisteredName(), modLoc(BLOCK_FOLDER + "/" + deferredItem.getId().getPath()));
                }
            } else {
                basicItem(item);
            }
        });
    }

    private ResourceLocation determineTexture(final DeferredHolder<Item, ? extends Item> item) {
        final String itemName = item.getId().getPath();
        for (final Map.Entry<String, ResourceLocation> entry : materialToTexture.entrySet()) {
            if (itemName.contains(entry.getKey())) {
                return entry.getValue();
            }
        }
        throw new IllegalStateException("No material found for " + itemName);
    }
}
