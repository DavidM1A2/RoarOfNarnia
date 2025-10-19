package com.dslovikosky.narnia.common.event;

import com.dslovikosky.narnia.common.network.packet.SyncSelectedSpellPowerSourcePacket;
import com.dslovikosky.narnia.common.network.packet.SyncSelectedSpellPowerSourcePacketHandler;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class PacketRegistrationHandler {
    @SubscribeEvent
    public void onRegisterPayloadHandlersEvent(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar("1");
        registrar.playToServer(SyncSelectedSpellPowerSourcePacket.TYPE, SyncSelectedSpellPowerSourcePacket.STREAM_CODEC, new SyncSelectedSpellPowerSourcePacketHandler());
    }
}
