package com.dslovikosky.narnia.common.item;

import com.dslovikosky.narnia.common.constants.Constants;
import com.mojang.logging.LogUtils;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import org.slf4j.Logger;

public class DebugItem extends Item {
    private static final Logger LOG = LogUtils.getLogger();

    public DebugItem() {
        super(new Properties().stacksTo(1)
                .setId(ResourceKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "debug"))));
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        return super.useOn(context);
    }

    @Override
    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
        return InteractionResult.SUCCESS;
    }
}
