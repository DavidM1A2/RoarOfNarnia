package com.dslovikosky.narnia.common.network.packet;

import com.dslovikosky.narnia.common.model.NarniaGlobalData;
import com.dslovikosky.narnia.common.model.chapter.Scene;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.handling.IPayloadHandler;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class StopScenePacketHandler implements IPayloadHandler<StopScenePacket> {
    @Override
    public void handle(final StopScenePacket payload, final IPayloadContext context) {
        final NarniaGlobalData data = NarniaGlobalData.getInstance(context.player().level());
        final Scene oldScene = data.getActiveScene();
        if (oldScene != null) {
            oldScene.getChapter().stop(oldScene, context.player().level(), false);
        }
        data.setActiveScene(null);
        data.markDirty();
        data.syncAll();
    }
}
