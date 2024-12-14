package com.dslovikosky.narnia.common.utils;

public class EllipsoidUtils {
    public static boolean isInsideEllipsoid(final double centerX, final double centerY, final double centerZ,
                                            final double width, final double height, final double length,
                                            final double x, final double y, final double z) {
        final double term1 = (x - centerX) * (x - centerX) / (width * width);
        final double term2 = (y - centerY) * (y - centerY) / (height * height);
        final double term3 = (z - centerZ) * (z - centerZ) / (length * length);
        return term1 + term2 + term3 <= 1;
    }
}
