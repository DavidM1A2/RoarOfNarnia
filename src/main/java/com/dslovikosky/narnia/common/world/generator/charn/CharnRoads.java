package com.dslovikosky.narnia.common.world.generator.charn;

import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.PositionalRandomFactory;

public class CharnRoads {
    static final int CITY_CELL_SIZE = 128;
    private static final double ROAD_WIDTH = 8.0;
    private static final double ALLEY_WIDTH = 5.0;
    private static final int MIN_BLOCKS_BETWEEN_ALLEYS = 24;

    private final int centerX;
    private final int centerZ;

    public CharnRoads(final int centerX, final int centerZ) {
        this.centerX = centerX;
        this.centerZ = centerZ;
    }

    /**
     * Roads for the giant grid in the city
     */
    public double computeRoadMask(final int x, final int z) {
        // Find distance to the nearest vertical and horizontal road centerlines
        final double distToXCenter = Math.abs(Mth.positiveModulo((x + centerX) + CITY_CELL_SIZE / 2.0, CITY_CELL_SIZE) - CITY_CELL_SIZE / 2.0);
        final double distToZCenter = Math.abs(Mth.positiveModulo((z + centerZ) + CITY_CELL_SIZE / 2.0, CITY_CELL_SIZE) - CITY_CELL_SIZE / 2.0);

        // Distance to the closest road (either along X or Z)
        final double dist = Math.min(distToXCenter, distToZCenter);

        // Convert distance to mask using a smooth falloff:
        // 1.0 at dist = 0, fades smoothly to 0.0 at dist = roadHalfWidth
        if (dist >= ROAD_WIDTH / 2) {
            return 0.0;
        } else {
            return 0.5 * (Math.cos(Math.PI * dist / (ROAD_WIDTH / 2)) + 1.0);
        }
    }

    /**
     * Alleys are thinner than roads, and go from road to road cutting through the grid squares
     */
    public double computeAlleyMask(final int x, final int z, final PositionalRandomFactory randomFactory) {
        // Which city cell are we in
        final int cellX = getCellX(x);
        final int cellZ = getCellZ(z);

        final double[] verticals = computeAlleyVerticals(x, z, randomFactory);
        final double[] horizontals = computeAlleyHorizontals(x, z, randomFactory);

        if (verticals.length == 0 || horizontals.length == 0) {
            return 0.0;
        }

        // Convert to local coordinates
        final double localX = (x + centerX) - cellX * CITY_CELL_SIZE;
        final double localZ = (z + centerZ) - cellZ * CITY_CELL_SIZE;

        // Distance to nearest vertical/horizontal alleys
        double nearestVertical = Double.POSITIVE_INFINITY;
        for (double vertical : verticals) {
            nearestVertical = Math.min(nearestVertical, Math.abs(vertical - localX));
        }
        double nearestHorizontal = Double.POSITIVE_INFINITY;
        for (double horizontal : horizontals) {
            nearestHorizontal = Math.min(nearestHorizontal, Math.abs(horizontal - localZ));
        }

        // Smooth cosine falloff
        final double falloffScale = 0.56; // computed for threshold 0.4 to give ~5 block width
        final double halfWidth = ALLEY_WIDTH / 2.0;

        final double maskV = nearestVertical < halfWidth * falloffScale
                ? 0.5 * (Math.cos(Math.PI * nearestVertical / (halfWidth * falloffScale)) + 1.0)
                : 0.0;

        final double maskH = nearestHorizontal < halfWidth * falloffScale
                ? 0.5 * (Math.cos(Math.PI * nearestHorizontal / (halfWidth * falloffScale)) + 1.0)
                : 0.0;

        return Math.max(maskV, maskH);
    }

    public Plot getPlotAt(final int x, final int z, final PositionalRandomFactory randomFactory) {
        final int cellX = getCellX(x);
        final int cellZ = getCellZ(z);

        final double[] alleyVerticals = computeAlleyVerticals(x, z, randomFactory);
        final double[] alleyHorizontals = computeAlleyHorizontals(x, z, randomFactory);

        // Convert to local coordinates
        final int localX = (x + centerX) - cellX * CITY_CELL_SIZE;
        final int localZ = (z + centerZ) - cellZ * CITY_CELL_SIZE;

        // Compute consistent “effective” half-widths (to be on block center)
        final double effectiveRoadHalfWidth = ROAD_WIDTH / 2.0;
        final double effectiveAlleyHalfWidth = ALLEY_WIDTH / 2.0;

        double minX = 0.0;
        double maxX = 0.0;
        for (int i = 0; i < alleyVerticals.length + 1; i++) {
            double potentialMinX = i == 0 ? effectiveRoadHalfWidth : alleyVerticals[i - 1] + effectiveAlleyHalfWidth;
            double potentialMaxX = i == alleyVerticals.length ? CITY_CELL_SIZE - effectiveRoadHalfWidth : alleyVerticals[i] - effectiveAlleyHalfWidth;
            if (potentialMinX <= localX && localX <= potentialMaxX) {
                minX = potentialMinX;
                maxX = potentialMaxX + 0.5;
                break;
            }
        }

        double minZ = 0.0;
        double maxZ = 0.0;
        for (int i = 0; i < alleyHorizontals.length + 1; i++) {
            double potentialMinZ = i == 0 ? effectiveRoadHalfWidth : alleyHorizontals[i - 1] + effectiveAlleyHalfWidth;
            double potentialMaxZ = i == alleyHorizontals.length ? CITY_CELL_SIZE - effectiveRoadHalfWidth : alleyHorizontals[i] - effectiveAlleyHalfWidth;
            if (potentialMinZ <= localZ && localZ <= potentialMaxZ) {
                minZ = potentialMinZ;
                maxZ = potentialMaxZ + 0.5;
                break;
            }
        }

        return new Plot(
                (int) (minX + cellX * CITY_CELL_SIZE - centerX),
                (int) (minZ + cellZ * CITY_CELL_SIZE - centerZ),
                (int) Math.round(maxX - minX),
                (int) Math.round(maxZ - minZ)
        );
    }

    public int getCellX(final int x) {
        return Math.floorDiv((x + centerX), CITY_CELL_SIZE);
    }

    public int getCellZ(final int z) {
        return Math.floorDiv((z + centerZ), CITY_CELL_SIZE);
    }

    private double[] computeAlleyVerticals(final int x, final int z, final PositionalRandomFactory randomFactory) {
        // Which city cell are we in
        final int cellX = getCellX(x);
        final int cellZ = getCellZ(z);

        // Special case, spawn Jadis' hall of images
        if (cellX == 0 && cellZ == 0) {
            return new double[0];
        }

        final RandomSource rand = randomFactory.at(cellX, 0, cellZ);

        // Evenly space them but add random jitter.
        // Choose how many alleys to spawn in this cell (1–3 each direction)
        final int numVerticals = 1 + rand.nextInt(3);
        final double[] verticals = new double[numVerticals];
        final double spacingVertical = (double) CITY_CELL_SIZE / (numVerticals + 1);
        for (int i = 0; i < numVerticals; i++) {
            verticals[i] = (i + 1) * spacingVertical + rand.nextDouble() * (spacingVertical - MIN_BLOCKS_BETWEEN_ALLEYS);
            // Snap all alley lines to half-block centers for consistency
            verticals[i] = Math.floor(verticals[i]) + 0.5;
        }

        return verticals;
    }

    private double[] computeAlleyHorizontals(final int x, final int z, final PositionalRandomFactory randomFactory) {
        // Which city cell are we in
        final int cellX = getCellX(x);
        final int cellZ = getCellZ(z);

        // Special case, spawn Jadis' hall of images
        if (cellX == 0 && cellZ == 0) {
            return new double[0];
        }

        final RandomSource rand = randomFactory.at(cellX, 0, cellZ);

        // Evenly space them but add random jitter.
        // Choose how many alleys to spawn in this cell (1–3 each direction)
        final int numHorizontals = 1 + rand.nextInt(3);
        final double[] horizontals = new double[numHorizontals];
        final double spacingHorizontal = (double) CITY_CELL_SIZE / (numHorizontals + 1);
        for (int i = 0; i < numHorizontals; i++) {
            horizontals[i] = (i + 1) * spacingHorizontal + rand.nextDouble() * (spacingHorizontal - MIN_BLOCKS_BETWEEN_ALLEYS);
            // Snap all alley lines to half-block centers for consistency
            horizontals[i] = Math.floor(horizontals[i]) + 0.5;
        }
        return horizontals;
    }

    /**
     * A plot is a rectangle bordered by roads and alleys
     */
    public record Plot(int x, int z, int width, int height) {
    }
}
