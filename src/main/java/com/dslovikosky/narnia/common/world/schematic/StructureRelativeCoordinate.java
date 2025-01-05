package com.dslovikosky.narnia.common.world.schematic;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.levelgen.structure.BoundingBox;

public class StructureRelativeCoordinate {
    private final BoundingBox structureBoundingBox;
    private final Direction orientation;

    public StructureRelativeCoordinate(final BoundingBox structureBoundingBox, final Direction orientation) {
        this.structureBoundingBox = structureBoundingBox;
        this.orientation = orientation;
    }

    public BlockPos relativeToAbsolutePos(final int relX, final int relY, final int relZ) {
        return new BlockPos(this.getWorldX(relX, relZ), this.getWorldY(relY), this.getWorldZ(relX, relZ));
    }

    private int getWorldX(final int relX, final int relZ) {
        return switch (orientation) {
            case NORTH, SOUTH -> structureBoundingBox.minX() + relX;
            case WEST -> structureBoundingBox.maxX() - relZ;
            case EAST -> structureBoundingBox.minX() + relZ;
            default -> relX;
        };
    }

    private int getWorldY(final int relY) {
        return orientation == null ? relY : relY + structureBoundingBox.minY();
    }

    private int getWorldZ(final int relX, final int relZ) {
        return switch (orientation) {
            case NORTH -> structureBoundingBox.maxZ() - relZ;
            case SOUTH -> structureBoundingBox.minZ() + relZ;
            case WEST, EAST -> structureBoundingBox.minZ() + relX;
            default -> relZ;
        };
    }
}
