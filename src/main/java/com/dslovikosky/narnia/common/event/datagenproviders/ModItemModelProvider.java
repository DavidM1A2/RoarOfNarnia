package com.dslovikosky.narnia.common.event.datagenproviders;

import com.dslovikosky.narnia.common.block.worldwood.HasCustomBlockItemModel;
import com.dslovikosky.narnia.common.constants.Constants;
import com.dslovikosky.narnia.common.constants.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.DoorBlock;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(final PackOutput output, final ExistingFileHelper existingFileHelper) {
        super(output, Constants.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        ModItems.ITEMS.getEntries().forEach(deferredItem -> {
            final Item item = deferredItem.get();
            if (item instanceof BlockItem blockItem) {
                if (blockItem.getBlock() instanceof HasCustomBlockItemModel specialBlockItemModel) {
                    specialBlockItemModel.applyBlockItemModel(this, deferredItem);
                } else if (blockItem.getBlock() instanceof DoorBlock) {
                    basicItem(item);
                } else {
                    withExistingParent(deferredItem.getRegisteredName(), modLoc(BLOCK_FOLDER + "/" + deferredItem.getId().getPath()));
                }
            } else {
                basicItem(item);
            }
        });
    }
}
