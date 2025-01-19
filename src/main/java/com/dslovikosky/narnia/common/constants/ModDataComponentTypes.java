package com.dslovikosky.narnia.common.constants;

import com.dslovikosky.narnia.common.model.ChapterGoalStatus;
import com.mojang.serialization.Codec;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.List;

public class ModDataComponentTypes {
    public static final DeferredRegister.DataComponents DATA_COMPONENT_TYPES = DeferredRegister.createDataComponents(Constants.MOD_ID);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<BoundingBox>> UNCLE_ANDREWS_HOUSE_BB = DATA_COMPONENT_TYPES.registerComponentType(
            "uncle_andrews_house_bb", builder -> builder.persistent(BoundingBox.CODEC).networkSynchronized(ByteBufCodecs.fromCodec(BoundingBox.CODEC)));
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Direction>> UNCLE_ANDREWS_HOUSE_DIRECTION = DATA_COMPONENT_TYPES.registerComponentType(
            "uncle_andrews_house_direction", builder -> builder.persistent(Direction.CODEC).networkSynchronized(Direction.STREAM_CODEC));
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Long>> CONVERSATION_START_TIME = DATA_COMPONENT_TYPES.registerComponentType(
            "conversation_start_time", builder -> builder.persistent(Codec.LONG).networkSynchronized(ByteBufCodecs.VAR_LONG));
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<List<ChapterGoalStatus>>> CHAPTER_GOAL_STATUS_LIST = DATA_COMPONENT_TYPES.registerComponentType(
            "chapter_goal_status_list", builder -> builder.persistent(ChapterGoalStatus.CODEC.listOf()).networkSynchronized(ChapterGoalStatus.STREAM_CODEC.apply(ByteBufCodecs.list())));
}
