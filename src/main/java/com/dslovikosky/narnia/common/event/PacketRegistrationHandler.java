package com.dslovikosky.narnia.common.event;

import com.dslovikosky.narnia.common.network.packet.JoinScenePacket;
import com.dslovikosky.narnia.common.network.packet.JoinScenePacketHandler;
import com.dslovikosky.narnia.common.network.packet.LeaveScenePacket;
import com.dslovikosky.narnia.common.network.packet.LeaveScenePacketHandler;
import com.dslovikosky.narnia.common.network.packet.StartScenePacket;
import com.dslovikosky.narnia.common.network.packet.StartScenePacketHandler;
import com.dslovikosky.narnia.common.network.packet.StopScenePacket;
import com.dslovikosky.narnia.common.network.packet.StopScenePacketHandler;
import com.dslovikosky.narnia.common.network.packet.SyncNarniaGlobalDataPacket;
import com.dslovikosky.narnia.common.network.packet.SyncNarniaGlobalDataPacketHandler;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class PacketRegistrationHandler {
    @SubscribeEvent
    public void onRegisterPayloadHandlersEvent(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar("1");
        registrar.playToClient(SyncNarniaGlobalDataPacket.TYPE, SyncNarniaGlobalDataPacket.STREAM_CODEC, new SyncNarniaGlobalDataPacketHandler());
        registrar.playToServer(StartScenePacket.TYPE, StartScenePacket.STREAM_CODEC, new StartScenePacketHandler());
        registrar.playToServer(StopScenePacket.TYPE, StopScenePacket.STREAM_CODEC, new StopScenePacketHandler());
        registrar.playToServer(JoinScenePacket.TYPE, JoinScenePacket.STREAM_CODEC, new JoinScenePacketHandler());
        registrar.playToServer(LeaveScenePacket.TYPE, LeaveScenePacket.STREAM_CODEC, new LeaveScenePacketHandler());
    }
}
