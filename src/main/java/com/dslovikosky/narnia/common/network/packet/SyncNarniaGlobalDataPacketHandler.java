package com.dslovikosky.narnia.common.network.packet;

import com.dslovikosky.narnia.client.proxy.ClientProxy;
import com.dslovikosky.narnia.common.model.NarniaGlobalData;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.handling.IPayloadHandler;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class SyncNarniaGlobalDataPacketHandler implements IPayloadHandler<SyncNarniaGlobalDataPacket> {
    @Override
    public void handle(final SyncNarniaGlobalDataPacket payload, final IPayloadContext context) {
        NarniaGlobalData.getInstance(context.player().level()).setActiveScene(payload.scene().orElse(null));
        ClientProxy.refreshChroniclesOfNarniaBookScreen();
    }
}
