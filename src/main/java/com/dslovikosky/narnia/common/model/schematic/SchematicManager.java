package com.dslovikosky.narnia.common.model.schematic;

import com.dslovikosky.narnia.common.constants.ModBlocks;
import com.dslovikosky.narnia.common.constants.ModRegistries;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.commands.arguments.blocks.BlockStateParser;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtAccounter;
import net.minecraft.nbt.NbtIo;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.FastBufferedInputStream;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import javax.annotation.ParametersAreNonnullByDefault;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class SchematicManager extends SimplePreparableReloadListener<Map<Schematic, SchematicData>> {
    private final RegistryAccess registryAccess;

    public SchematicManager(final RegistryAccess registryAccess) {
        this.registryAccess = registryAccess;
    }

    @Override
    protected Map<Schematic, SchematicData> prepare(final ResourceManager resourceManager, final ProfilerFiller profilerFiller) {
        final Map<Schematic, SchematicData> result = new HashMap<>();

        for (Map.Entry<ResourceKey<Schematic>, Schematic> registryEntry : ModRegistries.SCHEMATIC.entrySet()) {
            final Schematic schematic = registryEntry.getValue();
            result.put(schematic, load(schematic, resourceManager));
        }

        return result;
    }

    @Override
    protected void apply(Map<Schematic, SchematicData> newSchematicData, ResourceManager resourceManager, ProfilerFiller profilerFiller) {
        newSchematicData.forEach(Schematic::setSchematicData);
    }

    private SchematicData load(final Schematic schematic, final ResourceManager resourceManager) {
        final CompoundTag compoundTag;
        try (final InputStream inputStream = resourceManager.open(schematic.getFilePath());
             final InputStream fastInputStream = new FastBufferedInputStream(inputStream)) {
            compoundTag = NbtIo.readCompressed(fastInputStream, NbtAccounter.unlimitedHeap());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        final CompoundTag schematicTag = compoundTag.getCompound("Schematic").get();

        final int width = schematicTag.getInt("Width").get();
        final int height = schematicTag.getInt("Height").get();
        final int length = schematicTag.getInt("Length").get();
        final ListTag entities = schematicTag.getListOrEmpty("Entities");

        final CompoundTag blocksCompound = schematicTag.getCompoundOrEmpty("Blocks");
        final ListTag blockEntities = blocksCompound.getListOrEmpty("BlockEntities");
        final int[] blockData = decodeLEB128(blocksCompound.getByteArray("Data").orElse(new byte[0]));
        final CompoundTag blockPalette = blocksCompound.getCompoundOrEmpty("Palette");

        final Map<Integer, BlockState> blockPaletteLookup = new HashMap<>();
        for (final String blockStateString : blockPalette.keySet()) {
            final int key = blockPalette.getInt(blockStateString).get();
            blockPaletteLookup.put(key, parseBlockState(registryAccess, blockStateString));
        }

        final BlockState[] blocks = new BlockState[blockData.length];
        for (int i = 0; i < blockData.length; i++) {
            blocks[i] = blockPaletteLookup.get(blockData[i]);
        }

        // Schematic pre-processing - Extract PositionalMarker data out from the schematic and remove unnecessary TEs
        final boolean generateMarkerBlocks = false;
        final Map<String, Vec3> markers = new HashMap<>();
        if (!generateMarkerBlocks) {
            for (int i = 0; i < blocks.length; i++) {
                if (blocks[i].getBlock() == ModBlocks.POSITIONAL_MARKER.get()) {
                    blocks[i] = Blocks.AIR.defaultBlockState();
                }
            }
        }

        return new SchematicData(width, height, length, blocks, blockEntities, entities, markers);
    }

    private BlockState parseBlockState(final RegistryAccess registryAccess, final String blockStateString) {
        try {
            return BlockStateParser.parseForBlock(registryAccess.lookup(Registries.BLOCK).get(), blockStateString, true).blockState();
        } catch (final CommandSyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    // Decodes a byte[] array that contains variable-length integers (e.g., LEB128) into an int[] array.
    // Adapted from https://github.com/EngineHub/WorldEdit/blob/version/7.3.x/worldedit-core/src/main/java/com/sk89q/worldedit/internal/util/VarIntIterator.java#L28
    // Thanks ChatGPT. Haha
    private int[] decodeLEB128(byte[] byteArray) {
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
