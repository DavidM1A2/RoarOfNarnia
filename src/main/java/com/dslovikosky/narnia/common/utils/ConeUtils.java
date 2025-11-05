package com.dslovikosky.narnia.common.utils;

import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.stream.DoubleStream;

public class ConeUtils {
    public static AABB getBoundingBox(final Vec3 position, final Vec3 direction, final double radius, final double length) {
        return getBoundingBox(position, direction, radius, length, new Vec3(0.0, 1.0, 0.0));
    }

    public static AABB getBoundingBox(final Vec3 position, final Vec3 direction, final double radius, final double length, final Vec3 normal) {
        Vec3 upDir = normal;
        Vec3 leftDir = direction.cross(upDir).normalize();
        if (leftDir == Vec3.ZERO) {
            upDir = new Vec3(1.0, 0.0, 0.0);
            leftDir = direction.cross(upDir).normalize();
        }
        final Vec3 downDir = upDir.reverse();
        final Vec3 rightDir = leftDir.reverse();

        // Find four corners of the box that bounds the base circle, as well as the tip position (which we already know)
        final Vec3 baseCenterPos = position.add(direction.scale(length));
        final Vec3 cornerOne = baseCenterPos.add(leftDir.scale(radius)).add(upDir.scale(radius));
        final Vec3 cornerTwo = baseCenterPos.add(leftDir.scale(radius)).add(downDir.scale(radius));
        final Vec3 cornerThree = baseCenterPos.add(rightDir.scale(radius)).add(upDir.scale(radius));
        final Vec3 cornerFour = baseCenterPos.add(rightDir.scale(radius)).add(downDir.scale(radius));

        // Find the smallest (x, y, z) and biggest (x, y, z) coordinates. Ceil/floor the values, so we don't cut off any blocks partially within the cone
        final double minX = DoubleStream.of(position.x, cornerOne.x, cornerTwo.x, cornerThree.x, cornerFour.x).min().getAsDouble();
        final double minY = DoubleStream.of(position.y, cornerOne.y, cornerTwo.y, cornerThree.y, cornerFour.y).min().getAsDouble();
        final double minZ = DoubleStream.of(position.z, cornerOne.z, cornerTwo.z, cornerThree.z, cornerFour.z).min().getAsDouble();
        final double maxX = DoubleStream.of(position.x, cornerOne.x, cornerTwo.x, cornerThree.x, cornerFour.x).max().getAsDouble();
        final double maxY = DoubleStream.of(position.y, cornerOne.y, cornerTwo.y, cornerThree.y, cornerFour.y).max().getAsDouble();
        final double maxZ = DoubleStream.of(position.z, cornerOne.z, cornerTwo.z, cornerThree.z, cornerFour.z).max().getAsDouble();

        return new AABB(minX, minY, minZ, maxX, maxY, maxZ);
    }
}
