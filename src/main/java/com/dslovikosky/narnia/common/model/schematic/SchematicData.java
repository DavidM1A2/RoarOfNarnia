package com.dslovikosky.narnia.common.model.schematic;

import net.minecraft.nbt.ListTag;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.Map;

public record SchematicData(int width, int height, int length, BlockState[] blocks, ListTag blockEntities, ListTag entities, Map<String, Vec3> markers) {
}
