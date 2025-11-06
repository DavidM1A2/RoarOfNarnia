package com.dslovikosky.narnia.common.utils;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class UuidUtils {
    public static final Codec<UUID> CODEC = Codec.LONG.sizeLimitedListOf(2)
            .xmap(it -> new UUID(it.get(0), it.get(1)), it -> List.of(it.getMostSignificantBits(), it.getLeastSignificantBits()));
    public static final StreamCodec<ByteBuf, UUID> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.LONG,
            UUID::getMostSignificantBits,
            ByteBufCodecs.LONG,
            UUID::getLeastSignificantBits,
            UUID::new
    );

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
