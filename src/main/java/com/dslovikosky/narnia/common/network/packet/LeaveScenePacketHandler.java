package com.dslovikosky.narnia.common.network.packet;

import com.dslovikosky.narnia.common.model.NarniaGlobalData;
import com.dslovikosky.narnia.common.model.chapter.Scene;
import com.mojang.logging.LogUtils;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.handling.IPayloadHandler;
import org.slf4j.Logger;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class LeaveScenePacketHandler implements IPayloadHandler<LeaveScenePacket> {
    private static final Logger LOG = LogUtils.getLogger();

    @Override
    public void handle(final LeaveScenePacket payload, final IPayloadContext context) {
        final NarniaGlobalData data = NarniaGlobalData.getInstance(context.player().level());

        final Scene activeScene = data.getActiveScene();
        if (activeScene == null) {
            LOG.warn("No active scene to leave for {}.", context.player().getName());
            return;
        }

        activeScene.getChapter().tryLeave(activeScene, context.player());
    }
}
