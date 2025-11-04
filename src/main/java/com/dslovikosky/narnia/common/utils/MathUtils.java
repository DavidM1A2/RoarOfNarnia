package com.dslovikosky.narnia.common.utils;

import net.minecraft.util.Tuple;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix3d;
import org.joml.Quaternionf;
import org.joml.Vector3d;

public class MathUtils {
    private static final Vec3 X_UNIT_VECTOR = new Vec3(1.0, 0.0, 0.0);
    private static final Vec3 Y_UNIT_VECTOR = new Vec3(0.0, 1.0, 0.0);

    public static double round(final double it, final int decimalPlaces) {
        if (decimalPlaces < 0) {
            return it;
        }
        final double power = Math.pow(10, decimalPlaces);
        return Math.round(it * power) / power;
    }

    public static Vec3 rotateAround(Vec3 baseVec, Vec3 axis, double radians) {
        final Vector3d result = new Vector3d(baseVec.x, baseVec.y, baseVec.z).mul(new Matrix3d().rotate(-radians, axis.x(), axis.y(), axis.z()));
        return new Vec3(result.x, result.y, result.z);
    }

    /**
     * Computes the rotation needed to go from the source vector to the target vector as a Quaternion. For more info see:
     * https://stackoverflow.com/questions/1171849/finding-quaternion-representing-the-rotation-from-one-vector-to-another/1171995#1171995
     */
    public static Quaternionf computeRotationTo(final Vec3 source, final Vec3 target) {
        return new Quaternionf().rotateTo(source.toVector3f(), target.toVector3f());
    }

    public static Vec3 getNormal(final Vec3 source) {
        return getNormal(source, new Vec3(0.0, 1.0, 0.0));
    }

    public static Vec3 getNormal(final Vec3 source, final Vec3 upBasis) {
        final Vec3 leftRightDir = source.cross(upBasis).normalize();
        // Edge case when the vector we're getting the normal for is 0, 1, 0
        if (leftRightDir == Vec3.ZERO) {
            return Vec3.ZERO;
        }
        return leftRightDir.cross(source).normalize();
    }

    // Returns a pair of vectors that are both orthogonal to the original vector. The called on vector must be unit length
    public static Tuple<Vec3, Vec3> getOrthogonalVectors(final Vec3 vec3) {
        if (vec3 == Vec3.ZERO) {
            return new Tuple<>(vec3, vec3);
        }

        Vec3 leftRightDirection = Y_UNIT_VECTOR.cross(vec3);
        if (leftRightDirection == Vec3.ZERO) {
            leftRightDirection = X_UNIT_VECTOR;
        }
        final Vec3 upDownDirection = leftRightDirection.cross(vec3);

        return new Tuple<>(leftRightDirection, upDownDirection);
    }

    public static double log(double base, double logNumber) {
        return Math.log(logNumber) / Math.log(base);
    }
}