package com.dslovikosky.narnia.common.network.packet;

import com.dslovikosky.narnia.common.model.NarniaGlobalData;
import com.dslovikosky.narnia.common.model.chapter.Book;
import com.dslovikosky.narnia.common.model.chapter.Chapter;
import com.dslovikosky.narnia.common.model.chapter.Scene;
import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.handling.IPayloadHandler;
import org.slf4j.Logger;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class StartScenePacketHandler implements IPayloadHandler<StartScenePacket> {
    private static final Logger LOG = LogUtils.getLogger();

    @Override
    public void handle(final StartScenePacket payload, final IPayloadContext context) {
        LOG.warn("START SCENE");

        final Book book = payload.book();
        final ResourceLocation chapterId = payload.chapterId();
        final Chapter chapter = book.getChapter(chapterId);

        if (chapter == null) {
            LOG.warn("Chapter with id {} not found", chapterId);
            return;
        }

        final NarniaGlobalData data = NarniaGlobalData.getInstance(context.player().level());
        final Scene oldScene = data.getActiveScene();
        if (oldScene != null) {
            oldScene.getChapter().stop(oldScene);
        }
        final Scene newScene = new Scene(book, chapter);
        newScene.getChapter().start(newScene);
        data.setActiveScene(newScene);
        data.markDirty();
        data.syncAll();
    }
}
