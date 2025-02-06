package com.dslovikosky.narnia.common.world.structure;

import com.dslovikosky.narnia.common.constants.ModRegistries;
import com.dslovikosky.narnia.common.constants.ModStructurePieces;
import com.dslovikosky.narnia.common.model.schematic.Schematic;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.HangingEntity;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Optional;
import java.util.UUID;

@ParametersAreNonnullByDefault
public class SchematicStructurePiece extends StructurePiece {
    private static final String SCHEMATIC_ID = "SchematicId";

    private final Schematic schematic;

    protected SchematicStructurePiece(final int x, final int y, final int z, final Schematic schematic, final Direction direction) {
        super(ModStructurePieces.SCHEMATIC.get(), 0, makeBoundingBox(x, y, z, direction, schematic.getWidth(), schematic.getHeight(), schematic.getLength()));
        this.schematic = schematic;
        setOrientation(direction);
    }

    public SchematicStructurePiece(final StructurePieceSerializationContext pContext, final CompoundTag pTag) {
        super(ModStructurePieces.SCHEMATIC.get(), pTag);
        this.schematic = ModRegistries.SCHEMATIC.get(ResourceLocation.parse(pTag.getString(SCHEMATIC_ID)));
    }

    @Override
    protected void addAdditionalSaveData(final StructurePieceSerializationContext pContext, final CompoundTag pTag) {
        pTag.putString(SCHEMATIC_ID, schematic.getId().toString());
    }

    @Override
    public void postProcess(final WorldGenLevel pLevel, final StructureManager pStructureManager, final ChunkGenerator pGenerator, final RandomSource pRandom, final BoundingBox pBox, final ChunkPos pChunkPos, final BlockPos pPos) {
        generateBlocks(pLevel, pBox);
        generateTileEntities(pLevel, pBox);
        generateEntities(pLevel, pBox);
    }

    private void generateBlocks(final WorldGenLevel pLevel, final BoundingBox pBox) {
        final int width = schematic.getWidth();
        final int height = schematic.getHeight();
        final int length = schematic.getLength();
        final BlockState[] blocks = schematic.getBlocks();

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                for (int z = 0; z < length; z++) {
                    final int index = (y * length * width) + (z * width) + x;
                    final BlockState nextToPlace = blocks[index];

                    if (nextToPlace.getBlock() == Blocks.STRUCTURE_BLOCK) {
                        placeBlock(pLevel, Blocks.AIR.defaultBlockState(), x, y, length - z - 1, pBox);
                    } else {
                        placeBlock(pLevel, nextToPlace, x, y, length - z - 1, pBox);
                    }
                }
            }
        }
    }

    private void generateTileEntities(final WorldGenLevel pLevel, final BoundingBox pBox) {
        final ListTag blockEntities = schematic.getBlockEntities();
        final int length = schematic.getLength();

        for (int i = 0; i < blockEntities.size(); i++) {
            final CompoundTag blockEntityTag = blockEntities.getCompound(i);
            final int[] posTag = blockEntityTag.getIntArray("Pos");
            final int x = posTag[0];
            final int y = posTag[1];
            final int z = posTag[2];
            final BlockPos tileEntityPosition = new BlockPos(
                    getWorldX(x, length - z - 1),
                    getWorldY(y),
                    getWorldZ(x, length - z - 1));

            if (pBox.isInside(tileEntityPosition)) {
                final BlockEntity blockEntity = pLevel.getBlockEntity(tileEntityPosition);
                if (blockEntity != null) {
                    blockEntity.loadWithComponents(blockEntityTag.getCompound("Data"), pLevel.registryAccess());
                }
            }
        }
    }

    private void generateEntities(final WorldGenLevel pLevel, final BoundingBox pBox) {
        final ListTag entities = schematic.getEntities();
        final int length = schematic.getLength();

        for (int i = 0; i < entities.size(); i++) {
            final CompoundTag entityTag = entities.getCompound(i);

            String entityId = entityTag.getString("Id");
            Optional<EntityType<?>> entityTypeOpt = EntityType.byString(entityId);

            if (entityTypeOpt.isPresent()) {
                final EntityType<?> entityType = entityTypeOpt.get();
                final Entity entity = entityType.create(pLevel.getLevel());

                if (entity != null) {
                    ListTag posTag = entityTag.getList("Pos", Tag.TAG_DOUBLE);
                    double x = posTag.getDouble(0);
                    double y = posTag.getDouble(1);
                    double z = posTag.getDouble(2);
                    double newX = getWorldX(x, length - z - 1);
                    double newY = getWorldY(y);
                    double newZ = getWorldZ(x, length - z - 1);

                    if (pBox.isInside(new BlockPos((int) newX, (int) newY, (int) newZ))) {
                        final CompoundTag dataTag = entityTag.getCompound("Data");
                        dataTag.putString("id", entityId);
                        entity.load(dataTag);
                        entity.setUUID(UUID.randomUUID());
                        // Idk why. For some reason when orientation is west we shouldn't apply the mirror.
                        if (getOrientation() == Direction.WEST && entity instanceof HangingEntity) {
                            float yRotation = entity.rotate(getRotation());
                            entity.moveTo(newX, newY, newZ, yRotation, entity.getXRot());
                        } else {
                            float yRotation = entity.rotate(getRotation());
                            yRotation += entity.mirror(getMirror()) - entity.getYRot();
                            entity.moveTo(newX, newY, newZ, yRotation, entity.getXRot());
                        }
                        pLevel.addFreshEntity(entity);
                    }
                }
            }
        }
    }

    private double getWorldX(double pX, double pZ) {
        Direction direction = this.getOrientation();
        if (direction == null) {
            return pX;
        } else {
            return switch (direction) {
                case NORTH, SOUTH -> this.boundingBox.minX() + pX;
                case WEST -> this.boundingBox.maxX() - pZ;
                case EAST -> this.boundingBox.minX() + pZ;
                default -> pX;
            };
        }
    }

    private double getWorldY(double pY) {
        return this.getOrientation() == null ? pY : pY + this.boundingBox.minY();
    }

    private double getWorldZ(double pX, double pZ) {
        Direction direction = this.getOrientation();
        if (direction == null) {
            return pZ;
        } else {
            return switch (direction) {
                case NORTH -> this.boundingBox.maxZ() - pZ;
                case SOUTH -> this.boundingBox.minZ() + pZ;
                case WEST, EAST -> this.boundingBox.minZ() + pX;
                default -> pZ;
            };
        }
    }
}
