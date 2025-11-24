package com.dslovikosky.narnia.common.world.generator.charn;

import net.minecraft.util.Mth;
import net.minecraft.world.level.levelgen.synth.SimplexNoise;

public class CharnRivers {
    public double computeRiverDepth(final int x, final int z, final SimplexNoise noise) {
        // --- Step 1: Base smooth noise field ---
        // Very low frequency → broad, continent-scale curves
        final double riverBase = noise.getValue(x * 0.0015, z * 0.0015);

        // Slight warp to avoid uniform sine-wave patterns
        final double riverWarp = noise.getValue((x + 2000) * 0.006, (z - 2000) * 0.006) * 0.5;

        // Combine base and warp to create gentle direction bending
        final double riverCombined = riverBase + riverWarp * 0.4;

        // --- Step 2: Detect “near-zero” crossings for riverbeds ---
        // The closer riverCombined is to 0, the closer we are to a river
        final double riverVal = Math.abs(riverCombined);

        // Sharper falloff = thinner rivers
        double riverMask = 1.0 - Mth.clamp((riverVal - 0.03) * 10.0, 0.0, 1.0);

        // Make them rare and thinner with a power curve
        riverMask = Math.pow(riverMask, 7.0);

        // --- Step 4: Depth scaling ---
        return riverMask * 6.0;
    }
}
