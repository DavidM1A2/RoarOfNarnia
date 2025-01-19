package com.dslovikosky.narnia.common.model;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.StringRepresentable;

import java.util.function.IntFunction;

@MethodsReturnNonnullByDefault
public enum ChapterGoalStatus implements StringRepresentable {
    NEW,
    STARTED,
    FINISHED;

    public static final Codec<ChapterGoalStatus> CODEC = StringRepresentable.fromEnum(ChapterGoalStatus::values);
    private static final IntFunction<ChapterGoalStatus> BY_ID = ByIdMap.continuous(ChapterGoalStatus::ordinal, values(), ByIdMap.OutOfBoundsStrategy.WRAP);
    public static final StreamCodec<ByteBuf, ChapterGoalStatus> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, ChapterGoalStatus::ordinal);

    @Override
    public String getSerializedName() {
        return this.name();
    }
}
