package com.dslovikosky.narnia.common.model.attachment_type;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class PreRingTeleportData {
    public static final Codec<PreRingTeleportData> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                            Codec.unboundedMap(ResourceKey.codec(Registries.DIMENSION), PreRingTeleportEntry.CODEC)
                                    .fieldOf("dimension_to_entry")
                                    .forGetter(PreRingTeleportData::get)
                    )
                    .apply(instance, PreRingTeleportData::new)
    );

    private final Map<ResourceKey<Level>, PreRingTeleportEntry> dimensionToEntry;

    public PreRingTeleportData() {
        this.dimensionToEntry = new HashMap<>();
    }

    PreRingTeleportData(final Map<ResourceKey<Level>, PreRingTeleportEntry> dimensionToEntry) {
        this.dimensionToEntry = new HashMap<>(dimensionToEntry);
    }

    public void set(final ResourceKey<Level> level, final PreRingTeleportEntry entry) {
        dimensionToEntry.put(level, entry);
    }

    public Optional<PreRingTeleportEntry> get(final ResourceKey<Level> level) {
        return Optional.ofNullable(dimensionToEntry.get(level));
    }

    Map<ResourceKey<Level>, PreRingTeleportEntry> get() {
        return dimensionToEntry;
    }
}
