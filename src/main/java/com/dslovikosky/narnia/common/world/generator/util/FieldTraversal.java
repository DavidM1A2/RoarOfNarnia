package com.dslovikosky.narnia.common.world.generator.util;

import java.util.function.BiFunction;

public class FieldTraversal {
    public static LocalMaxima findLocalMaxima(int x, int z, final int maxSteps, final BiFunction<Integer, Integer, Double> func) {
        double maxValue = 0.0;
        for (int step = 0; step < maxSteps; step++) {
            double currentValue = func.apply(x, z);
            double xPositive = func.apply(x + 1, z);
            double xNegative = func.apply(x - 1, z);
            double zPositive = func.apply(x, z + 1);
            double zNegative = func.apply(x, z - 1);

            // move toward steepest ascent
            maxValue = currentValue;
            int dx = 0, dz = 0;
            if (xPositive > maxValue) {
                maxValue = xPositive;
                dx = 1;
                dz = 0;
            }
            if (xNegative > maxValue) {
                maxValue = xNegative;
                dx = -1;
                dz = 0;
            }
            if (zPositive > maxValue) {
                maxValue = zPositive;
                dx = 0;
                dz = 1;
            }
            if (zNegative > maxValue) {
                maxValue = zNegative;
                dx = 0;
                dz = -1;
            }

            if (dx == 0 && dz == 0) {
                // Found the peak
                break;
            }
            x += dx;
            z += dz;
        }
        return new LocalMaxima(x, z, maxValue);
    }

    public record LocalMaxima(int x, int z, double value) {
    }
}
