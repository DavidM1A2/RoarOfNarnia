package com.dslovikosky.narnia.common.world.schematic;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.arguments.blocks.BlockStateParser;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtAccounter;
import net.minecraft.nbt.NbtIo;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.FastBufferedInputStream;
import net.minecraft.world.level.block.state.BlockState;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SchematicLoader {
    public static Schematic load(final RegistryAccess registryAccess, final ResourceManager resourceManager, final ResourceLocation location) {
        final CompoundTag compoundTag;
        try (final InputStream inputStream = resourceManager.open(location);
             final InputStream fastInputStream = new FastBufferedInputStream(inputStream)) {
            compoundTag = NbtIo.readCompressed(fastInputStream, NbtAccounter.unlimitedHeap());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        final CompoundTag schematicTag = compoundTag.getCompound("Schematic");

        final int width = schematicTag.getInt("Width");
        final int height = schematicTag.getInt("Height");
        final int length = schematicTag.getInt("Length");
        final ListTag entities = schematicTag.getList("Entities", ListTag.TAG_COMPOUND);

        final CompoundTag blocksCompound = schematicTag.getCompound("Blocks");
        final ListTag blockEntities = blocksCompound.getList("BlockEntities", ListTag.TAG_COMPOUND);
        final int[] blockData = decodeLEB128(blocksCompound.getByteArray("Data"));
        final CompoundTag blockPalette = blocksCompound.getCompound("Palette");

        final Map<Integer, BlockState> blockPaletteLookup = new HashMap<>();
        for (final String blockStateString : blockPalette.getAllKeys()) {
            final int key = blockPalette.getInt(blockStateString);
            blockPaletteLookup.put(key, parseBlockState(registryAccess, blockStateString));
        }

        final BlockState[] blocks = new BlockState[blockData.length];
        for (int i = 0; i < blockData.length; i++) {
            blocks[i] = blockPaletteLookup.get(blockData[i]);
        }

        return new Schematic(location, width, height, length, blocks, blockEntities, entities);
    }

    private static BlockState parseBlockState(final RegistryAccess registryAccess, final String blockStateString) {
        try {
            return BlockStateParser.parseForBlock(registryAccess.lookup(Registries.BLOCK).get(), blockStateString, true).blockState();
        } catch (final CommandSyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    // Decodes a byte[] array that contains variable-length integers (e.g., LEB128) into an int[] array.
    // Adapted from https://github.com/EngineHub/WorldEdit/blob/version/7.3.x/worldedit-core/src/main/java/com/sk89q/worldedit/internal/util/VarIntIterator.java#L28
    // Thanks ChatGPT. Haha
    private static int[] decodeLEB128(byte[] byteArray) {
        final List<Integer> decodedIntegers = new ArrayList<>();
        int index = 0;

        while (index < byteArray.length) {
            int value = 0;
            int shift = 0;
            byte currentByte;

            // Read each byte, and process according to LEB128
            do {
                currentByte = byteArray[index++];
                value |= (currentByte & 0x7F) << shift;  // Extract lower 7 bits and shift
                shift += 7;
            } while ((currentByte & 0x80) != 0); // Continue until the MSB is 0
            decodedIntegers.add(value);
        }

        return decodedIntegers.stream().mapToInt(i -> i).toArray();
    }
}
