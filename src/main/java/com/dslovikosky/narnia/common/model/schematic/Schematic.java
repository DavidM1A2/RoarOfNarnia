package com.dslovikosky.narnia.common.model.schematic;

import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;

public class Schematic {
    private final ResourceLocation id;

    // Mutable!
    private SchematicData schematicData;

    public Schematic(final ResourceLocation id) {
        this.id = id;
    }

    public ResourceLocation getId() {
        return id;
    }

    public ResourceLocation getFilePath() {
        return ResourceLocation.fromNamespaceAndPath(id.getNamespace(), String.format("schematic/%s.schem", id.getPath()));
    }

    public int getWidth() {
        return schematicData.width();
    }

    public int getHeight() {
        return schematicData.height();
    }

    public int getLength() {
        return schematicData.length();
    }

    public BlockState[] getBlocks() {
        return schematicData.blocks();
    }

    public BlockState getBlock(final int x, final int y, final int z) {
        return getBlocks()[y * getWidth() * getLength() + z * getWidth() + x];
    }

    public ListTag getBlockEntities() {
        return schematicData.blockEntities();
    }

    public ListTag getEntities() {
        return schematicData.entities();
    }

    protected void setSchematicData(final SchematicData schematicData) {
        this.schematicData = schematicData;
    }
}
