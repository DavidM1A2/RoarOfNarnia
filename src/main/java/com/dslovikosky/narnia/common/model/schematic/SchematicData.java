package com.dslovikosky.narnia.common.model.schematic;

import com.dslovikosky.narnia.common.model.PositionalMarker;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public record SchematicData(int width, int height, int length, BlockState[] blocks, ListTag blockEntities, ListTag entities, List<PositionalMarker> markers) {
}
