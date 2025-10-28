package com.dslovikosky.narnia.common.utils;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import java.util.Optional;
import java.util.UUID;

public class UuidUtils {
    private static final String NBT_MOST = "_most";
    private static final String NBT_LEAST = "_least";

    public static Optional<UUID> read(final String name, final CompoundTag nbt) {
        final Optional<Long> least = nbt.getLong(name + NBT_MOST);
        final Optional<Long> most = nbt.getLong(name + NBT_LEAST);
        if (least.isPresent() && most.isPresent()) {
            return Optional.of(new UUID(most.get(), least.get()));
        }
        return Optional.empty();
    }

    public static void write(final String name, final CompoundTag nbt, final UUID uuid) {
        nbt.putLong(name + NBT_LEAST, uuid.getLeastSignificantBits());
        nbt.putLong(name + NBT_MOST, uuid.getMostSignificantBits());
    }

    public static Optional<UUID> read(final String name, final ValueInput input) {
        final Optional<Long> least = input.getLong(name + NBT_MOST);
        final Optional<Long> most = input.getLong(name + NBT_LEAST);
        if (least.isPresent() && most.isPresent()) {
            return Optional.of(new UUID(most.get(), least.get()));
        }
        return Optional.empty();
    }

    public static void write(final String name, final ValueOutput output, final UUID uuid) {
        output.putLong(name + NBT_LEAST, uuid.getLeastSignificantBits());
        output.putLong(name + NBT_MOST, uuid.getMostSignificantBits());
    }
}
