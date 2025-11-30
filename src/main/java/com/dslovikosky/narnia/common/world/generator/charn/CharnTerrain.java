package com.dslovikosky.narnia.common.world.generator.charn;

import net.minecraft.util.Mth;
import net.minecraft.world.level.levelgen.synth.SimplexNoise;

public class CharnTerrain {
    public double computeBaseHeight(final int x, final int z, final SimplexNoise noise) {
        // Normal terrain components
        final double mountainHeight = computeMountainHeight(x, z, noise);
        final double detail = computeTerrainDetail(x, z, noise);

        // Apply flattening
        return 64.0 + (mountainHeight + detail) * getFlattenFactor(x, z);
    }

    private double computeTerrainDetail(final int x, final int z, final SimplexNoise noise) {
        // Multi-frequency noise for fractal detail
        final double baseNoise = noise.getValue(x * 0.01, z * 0.01);
        final double midNoise = noise.getValue(x * 0.03, z * 0.03);
        return baseNoise * 3.0 + midNoise * 1.2;
    }

    private double computeMountainHeight(final int x, final int z, final SimplexNoise noise) {
        // Base frequency controls how far apart mountains are (lower = more spaced out)
        final double mountainFrequency = 1.0 / 1400.0;
        final double mountainHeight = 40.0;

        // Multi-octave gentle variation
        final double n1 = noise.getValue(x * mountainFrequency, z * mountainFrequency);
        final double n2 = noise.getValue(x * mountainFrequency * 2.0, z * mountainFrequency * 2.0) * 0.5;
        final double n3 = noise.getValue(x * mountainFrequency * 4.0, z * mountainFrequency * 4.0) * 0.25;

        final double combined = (n1 + n2 + n3) / 1.75;

        // Use a smooth "ridge" function - peaks are more rounded, not sharp
        final double height = Math.pow(Math.abs(combined), 1.3) * mountainHeight;

        // Add slow global slope variation (adds more natural continental feel)
        final double slope = noise.getValue(x * 0.0002, z * 0.0002) * 10.0;
        return height + slope;
    }

    private double getFlattenFactor(int x, int z) {
        final double ax = Math.abs(x);
        final double az = Math.abs(z);

        // Start fading terrain back in between 64 and 128 blocks away
        final double flatRadius = CharnRoads.CITY_CELL_SIZE / 2.0;
        final double normalRadius = flatRadius * 2;

        // Compute fade factor (0 = flat, 1 = full terrain)
        double fx = smoothstep(flatRadius, normalRadius, ax);
        double fz = smoothstep(flatRadius, normalRadius, az);

        // Combine square falloff
        return Math.max(fx, fz);
    }

    private double smoothstep(double edge0, double edge1, double value) {
        value = Mth.clamp((value - edge0) / (edge1 - edge0), 0.0, 1.0);
        return value * value * (3 - 2 * value);
    }
}
