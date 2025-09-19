package com.dslovikosky.narnia.common.utils;

public class MathUtils {
    public static double round(final double it, final int decimalPlaces) {
        if (decimalPlaces < 0) {
            return it;
        }
        final double power = Math.pow(10, decimalPlaces);
        return Math.round(it * power) / power;
    }
}
