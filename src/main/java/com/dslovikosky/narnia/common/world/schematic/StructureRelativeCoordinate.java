package com.dslovikosky.narnia.common.world.schematic;

import net.minecraft.core.Direction;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.phys.Vec3;

public class StructureRelativeCoordinate {
    private final BoundingBox structureBoundingBox;
    private final Direction orientation;

    public StructureRelativeCoordinate(final BoundingBox structureBoundingBox, final Direction orientation) {
        this.structureBoundingBox = structureBoundingBox;
        this.orientation = orientation;
    }

    public Vec3 relativeToAbsolutePos(final double relX, final double relY, final double relZ) {
        return new Vec3(this.getWorldX(relX, relZ), this.getWorldY(relY), this.getWorldZ(relX, relZ));
    }

    public Vec3 relativeToAbsolutePos(final Vec3 relativePos) {
        return new Vec3(this.getWorldX(relativePos.x(), relativePos.z()), this.getWorldY(relativePos.y()), this.getWorldZ(relativePos.x(), relativePos.z()));
    }

    private double getWorldX(final double relX, final double relZ) {
        return switch (orientation) {
            case NORTH, SOUTH -> structureBoundingBox.minX() + relX;
            case WEST -> structureBoundingBox.maxX() - relZ;
            case EAST -> structureBoundingBox.minX() + relZ;
            default -> relX;
        };
    }

    private double getWorldY(final double relY) {
        return orientation == null ? relY : relY + structureBoundingBox.minY();
    }

    private double getWorldZ(final double relX, final double relZ) {
        return switch (orientation) {
            case NORTH -> structureBoundingBox.maxZ() - relZ;
            case SOUTH -> structureBoundingBox.minZ() + relZ;
            case WEST, EAST -> structureBoundingBox.minZ() + relX;
            default -> relZ;
        };
    }
}
