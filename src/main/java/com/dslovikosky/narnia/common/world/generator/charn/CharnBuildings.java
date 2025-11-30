package com.dslovikosky.narnia.common.world.generator.charn;

import com.dslovikosky.narnia.common.constants.ModBlocks;
import com.dslovikosky.narnia.common.constants.ModSchematics;
import com.dslovikosky.narnia.common.model.schematic.Schematic;
import com.dslovikosky.narnia.common.world.generator.util.FieldTraversal;
import com.google.common.collect.ImmutableList;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.PositionalRandomFactory;
import net.minecraft.world.level.levelgen.synth.SimplexNoise;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

public class CharnBuildings {
    private static final List<Pair<Supplier<Schematic>, Integer>> DARK_CITY_SCHEMATICS = ImmutableList.<Pair<Supplier<Schematic>, Integer>>builder()
            .add(Pair.of(ModSchematics.DARK_CITY_SMALL_1, 0))
            .add(Pair.of(ModSchematics.DARK_CITY_GARDEN_1, -5))
            .add(Pair.of(ModSchematics.DARK_CITY_LARGE_1, 0))
            .add(Pair.of(ModSchematics.DARK_CITY_TOWER_1, 0))
            .add(Pair.of(ModSchematics.DARK_CITY_PYRAMID_1, 0))
            .build();

    private final int centerX;
    private final int centerZ;
    private final CharnTerrain charnTerrain;
    private final CharnRivers charnRivers;
    private final CharnRoads charnRoads;

    public CharnBuildings(final int centerX, final int centerZ,
                          final CharnTerrain charnTerrain,
                          final CharnRivers charnRivers,
                          final CharnRoads charnRoads) {
        this.centerX = centerX;
        this.centerZ = centerZ;
        this.charnTerrain = charnTerrain;
        this.charnRivers = charnRivers;
        this.charnRoads = charnRoads;
    }

    public List<BuildingPlacement> placeBuildingsInPlot(CharnRoads.Plot plot, SimplexNoise noise, PositionalRandomFactory randomFactory) {
        final List<BuildingPlacement> result = new ArrayList<>();

        final int plotX = plot.x();
        final int plotZ = plot.z();
        final int plotWidth = plot.width();
        final int plotHeight = plot.height();

        final int cellX = charnRoads.getCellX(plotX);
        final int cellZ = charnRoads.getCellZ(plotZ);
        if (cellX == 0 && cellZ == 0) {
            return Collections.emptyList();
        }

        final RandomSource randomSource = randomFactory.at(plotX, 0, plotZ);

        // Track occupied tiles with a 2D boolean array
        final boolean[][] occupied = new boolean[plotWidth][plotHeight];

        // Mark river tiles as occupied
        for (int dx = 0; dx < plotWidth; dx++) {
            for (int dz = 0; dz < plotHeight; dz++) {
                final int worldX = plotX + dx;
                final int worldZ = plotZ + dz;
                final double riverDepth = charnRivers.computeRiverDepth(worldX, worldZ, noise);
                if (riverDepth >= 1.0) {
                    // mark river tile as occupied
                    occupied[dx][dz] = true;
                }
            }
        }

        final List<Direction> edges = Direction.Plane.HORIZONTAL.shuffledCopy(randomSource);
        for (final Direction edge : edges) {
            // Face the edge
            final Rotation rotation = switch (edge) {
                case SOUTH -> Rotation.NONE;
                case NORTH -> Rotation.CLOCKWISE_180;
                case WEST -> Rotation.CLOCKWISE_90;
                case EAST -> Rotation.COUNTERCLOCKWISE_90;
                default -> throw new RuntimeException();
            };

            final int maxCursor = (edge == Direction.NORTH || edge == Direction.SOUTH) ? plotWidth : plotHeight;
            int cursor = 0;

            while (cursor < maxCursor) {
                // Filter schematics that fit within the remaining space and plot bounds
                int finalCursor = cursor;
                final List<Pair<Supplier<Schematic>, Integer>> fittingSchematics = DARK_CITY_SCHEMATICS.stream().filter(entry -> {
                    final Schematic schematic = entry.getLeft().get();
                    final int schematicWidth = (rotation == Rotation.NONE || rotation == Rotation.CLOCKWISE_180) ? schematic.getWidth() : schematic.getLength();
                    final int schematicLength = (rotation == Rotation.NONE || rotation == Rotation.CLOCKWISE_180) ? schematic.getLength() : schematic.getWidth();
                    return switch (edge) {
                        case NORTH, SOUTH -> schematicWidth <= maxCursor - finalCursor && schematicLength <= plotHeight;
                        case WEST, EAST -> schematicWidth <= plotWidth && schematicLength <= maxCursor - finalCursor;
                        default -> false;
                    };
                }).toList();

                if (fittingSchematics.isEmpty()) {
                    break; // No schematic fits, move to next edge
                }

                final Pair<Supplier<Schematic>, Integer> schematicEntry = fittingSchematics.get(randomSource.nextInt(fittingSchematics.size()));
                final Schematic schematic = schematicEntry.getLeft().get();
                final int placementYOffset = schematicEntry.getRight();
                final int schematicWidth = (rotation == Rotation.NONE || rotation == Rotation.CLOCKWISE_180) ? schematic.getWidth() : schematic.getLength();
                final int schematicLength = (rotation == Rotation.NONE || rotation == Rotation.CLOCKWISE_180) ? schematic.getLength() : schematic.getWidth();

                // Calculate placement coordinates
                int placementX = plotX;
                int placementZ = plotZ;

                switch (edge) {
                    case NORTH -> {
                        placementX = plotX + cursor;
                        placementZ = plotZ;
                    }
                    case SOUTH -> {
                        placementX = plotX + cursor;
                        placementZ = plotZ + plotHeight - schematicLength;
                    }
                    case WEST -> {
                        placementX = plotX;
                        placementZ = plotZ + cursor;
                    }
                    case EAST -> {
                        placementX = plotX + plotWidth - schematicWidth;
                        placementZ = plotZ + cursor;
                    }
                }

                // Check footprint against plot bounds
                if (placementX < plotX || placementZ < plotZ
                        || placementX + schematicWidth > plotX + plotWidth
                        || placementZ + schematicLength > plotZ + plotHeight) {
                    cursor += 1;
                    continue;
                }

                // Check if footprint overlaps any occupied tiles
                boolean overlaps = false;
                outer:
                for (int x = 0; x < schematicWidth; x++) {
                    for (int z = 0; z < schematicLength; z++) {
                        int relX = placementX - plotX + x;
                        int relZ = placementZ - plotZ + z;
                        if (occupied[relX][relZ]) {
                            overlaps = true;
                            break outer;
                        }
                    }
                }

                if (overlaps) {
                    cursor += 1;
                    continue;
                }

                // Sample ground height specifically at the door instead of footprint
                int doorX = placementX;
                int doorZ = placementZ;

                switch (edge) {
                    case NORTH -> doorZ = plotZ - 1;               // just north of the plot
                    case SOUTH -> doorZ = plotZ + plotHeight;      // just south of the plot
                    case WEST -> doorX = plotX - 1;               // just west of the plot
                    case EAST -> doorX = plotX + plotWidth;       // just east of the plot
                }

                // Center the door along the building side
                if (edge == Direction.NORTH || edge == Direction.SOUTH) {
                    doorX = placementX + schematicWidth / 2;
                } else {
                    doorZ = placementZ + schematicLength / 2;
                }

                // Sample height at the door position
                final int groundHeight = sampleGroundHeightAtRoad(doorX, doorZ, noise, randomFactory);

                result.add(new BuildingPlacement(schematic, placementX, groundHeight + placementYOffset, placementZ, rotation));

                // Mark tiles as occupied
                for (int x = 0; x < schematicWidth; x++) {
                    for (int z = 0; z < schematicLength; z++) {
                        int relX = placementX - plotX + x;
                        int relZ = placementZ - plotZ + z;
                        occupied[relX][relZ] = true;
                    }
                }

                if (edge == Direction.NORTH || edge == Direction.SOUTH) {
                    cursor += schematicWidth + 1;
                } else { // WEST or EAST
                    cursor += schematicLength + 1;
                }
            }
        }

        return result;
    }

    private int sampleGroundHeightAtRoad(int doorX, int doorZ, SimplexNoise noise, PositionalRandomFactory randomFactory) {
        final FieldTraversal.LocalMaxima bestMax = FieldTraversal.findLocalMaxima(doorX, doorZ, 4, (x, z) -> {
            double roadMask = charnRoads.computeRoadMask(x, z);
            double alleyMask = charnRoads.computeAlleyMask(x, z, randomFactory);
            return Math.max(roadMask, alleyMask);
        });
        return (int) Math.floor(charnTerrain.computeBaseHeight(bestMax.x(), bestMax.z(), noise));
    }

    public record BuildingPlacement(Schematic schematic, int x, int y, int z, Rotation rotation) {
        public boolean contains(int xPos, int zPos) {
            final int width = switch (rotation) {
                case NONE, CLOCKWISE_180 -> schematic.getWidth();
                case CLOCKWISE_90, COUNTERCLOCKWISE_90 -> schematic.getLength();
            };
            final int length = switch (rotation) {
                case NONE, CLOCKWISE_180 -> schematic.getLength();
                case CLOCKWISE_90, COUNTERCLOCKWISE_90 -> schematic.getWidth();
            };
            return xPos >= x && xPos < x + width && zPos >= z && zPos < z + length;
        }

        // Paste a single column of this building into a chunk
        public void pasteColumn(ChunkAccess chunk, int worldX, int worldZ) {
            final int relativeX = worldX - x;
            final int relativeZ = worldZ - z;
            final int schematicWidth = schematic.getWidth();
            final int schematicLength = schematic.getLength();

            // --- Calculate rotated schematic coordinates ---
            int schematicX = 0;
            int schematicZ = 0;
            switch (rotation) {
                case NONE -> {
                    schematicX = relativeX;
                    schematicZ = relativeZ;
                }
                case CLOCKWISE_90 -> {
                    schematicX = relativeZ;
                    schematicZ = (schematicLength - 1) - relativeX;
                }
                case CLOCKWISE_180 -> {
                    schematicX = (schematicWidth - 1) - relativeX;
                    schematicZ = (schematicLength - 1) - relativeZ;
                }
                case COUNTERCLOCKWISE_90 -> {
                    schematicX = (schematicWidth - 1) - relativeZ;
                    schematicZ = relativeX;
                }
            }

            // Skip if out of bounds
            if (schematicX < 0 || schematicZ < 0 || schematicX >= schematicWidth || schematicZ >= schematicLength) return;

            // --- Paste blocks ---
            for (int schematicY = 0; schematicY < schematic.getHeight(); schematicY++) {
                BlockState state = schematic.getBlock(schematicX, schematicY, schematicZ);
                chunk.setBlockState(new BlockPos(worldX, y + schematicY, worldZ), state.rotate(rotation));
            }

            // --- Paste buffer blocks under structure ---
            for (int bufferY = -5; bufferY < 0; bufferY++) {
                chunk.setBlockState(new BlockPos(worldX, y + bufferY, worldZ),
                        ModBlocks.DARK_CITY_COBBLESTONE.get().defaultBlockState());
            }

            // --- Paste tile entities ---
            final ListTag blockEntities = schematic.getBlockEntities();
            for (int i = 0; i < blockEntities.size(); i++) {
                final CompoundTag blockEntityTag = blockEntities.getCompoundOrEmpty(i);
                final int[] posTag = blockEntityTag.getIntArray("Pos").orElse(new int[3]);
                final int sx = posTag[0];
                final int sy = posTag[1];
                final int sz = posTag[2];

                // Transform schematic coordinates (sx, sz) into world coordinates
                final int teWorldX;
                final int teWorldZ;
                switch (rotation) {
                    case NONE -> {
                        teWorldX = x + sx;
                        teWorldZ = z + sz;
                    }
                    case CLOCKWISE_90 -> {
                        teWorldX = x + (schematicLength - 1) - sz;
                        teWorldZ = z + sx;
                    }
                    case CLOCKWISE_180 -> {
                        teWorldX = x + (schematicWidth - 1) - sx;
                        teWorldZ = z + (schematicLength - 1) - sz;
                    }
                    case COUNTERCLOCKWISE_90 -> {
                        teWorldX = x + sz;
                        teWorldZ = z + (schematicWidth - 1) - sx;
                    }
                    default -> throw new IllegalStateException("Unexpected rotation: " + rotation);
                }

                // Only place tile-entities for this column (match worldX/worldZ)
                if (teWorldX != worldX || teWorldZ != worldZ) continue;

                // Set tile entity NBT
                final CompoundTag data = blockEntityTag.getCompound("Data").get();
                data.putInt("x", teWorldX);
                data.putInt("y", y + sy);
                data.putInt("z", teWorldZ);
                chunk.setBlockEntityNbt(data);
            }
        }
    }
}
