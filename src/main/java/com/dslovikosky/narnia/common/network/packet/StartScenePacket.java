package com.dslovikosky.narnia.common.network.packet;

import com.dslovikosky.narnia.common.constants.Constants;
import com.dslovikosky.narnia.common.constants.ModRegistries;
import com.dslovikosky.narnia.common.model.chapter.Book;
import com.dslovikosky.narnia.common.model.chapter.Chapter;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public record StartScenePacket(Book book, ResourceLocation chapterId) implements CustomPacketPayload {
    public static final StreamCodec<RegistryFriendlyByteBuf, StartScenePacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.registry(ModRegistries.BOOK_KEY), StartScenePacket::book,
            ResourceLocation.STREAM_CODEC, StartScenePacket::chapterId,
            StartScenePacket::new
    );
    public static final CustomPacketPayload.Type<StartScenePacket> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "start_scene"));

    public StartScenePacket(final Book book, final Chapter chapter) {
        this(book, chapter.id());
    }

    @Override
    public @NotNull Type<StartScenePacket> type() {
        return TYPE;
    }
}
