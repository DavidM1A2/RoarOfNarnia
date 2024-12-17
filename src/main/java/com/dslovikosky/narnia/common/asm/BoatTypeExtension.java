package com.dslovikosky.narnia.common.asm;

import com.dslovikosky.narnia.common.constants.Constants;
import com.dslovikosky.narnia.common.constants.ModBlocks;
import com.dslovikosky.narnia.common.constants.ModItems;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.neoforged.fml.common.asm.enumextension.EnumProxy;

import java.util.function.Supplier;

public class BoatTypeExtension {
    public static final EnumProxy<Boat.Type> WORLD_WOOD_BOAT_TYPE_PROXY = new EnumProxy<>(Boat.Type.class,
            (Supplier<Block>) ModBlocks.WORLD_WOOD_PLANKS::get,
            ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "world_wood").toString(),
            (Supplier<Item>) ModItems.WORLD_WOOD_BOAT::get,
            (Supplier<Item>) ModItems.WORLD_WOOD_CHEST_BOAT::get,
            (Supplier<Item>) () -> Items.STICK,
            false);
}
