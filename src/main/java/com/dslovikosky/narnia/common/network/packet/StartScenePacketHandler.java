package com.dslovikosky.narnia.common.network.packet;

import com.dslovikosky.narnia.common.model.NarniaGlobalData;
import com.dslovikosky.narnia.common.model.chapter.Scene;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.handling.IPayloadHandler;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class StartScenePacketHandler implements IPayloadHandler<StartScenePacket> {
    @Override
    public void handle(final StartScenePacket payload, final IPayloadContext context) {
        final NarniaGlobalData data = NarniaGlobalData.getInstance(context.player().level());
        final Scene oldScene = data.getActiveScene();
        if (oldScene != null) {
            return;
        }

        final Scene newScene = new Scene(payload.chapter());
        data.setActiveScene(newScene);
        data.markDirty();
        data.syncAll();
    }
}
