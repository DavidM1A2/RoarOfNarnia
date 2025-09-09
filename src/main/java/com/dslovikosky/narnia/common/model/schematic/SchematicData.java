package com.dslovikosky.narnia.common.model.schematic;

import net.minecraft.nbt.ListTag;
import net.minecraft.world.level.block.state.BlockState;

public record SchematicData(int width, int height, int length, BlockState[] blocks, ListTag blockEntities, ListTag entities) {
}
