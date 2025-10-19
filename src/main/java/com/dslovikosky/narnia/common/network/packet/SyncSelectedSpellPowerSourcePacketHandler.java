package com.dslovikosky.narnia.common.network.packet;

import com.dslovikosky.narnia.common.constants.ModAttachmentTypes;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.handling.IPayloadHandler;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class SyncSelectedSpellPowerSourcePacketHandler implements IPayloadHandler<SyncSelectedSpellPowerSourcePacket> {
    @Override
    public void handle(SyncSelectedSpellPowerSourcePacket payload, IPayloadContext context) {
        context.player().setData(ModAttachmentTypes.SELECTED_SPELL_POWER_SOURCE, payload.selectedSpellPowerSource());
    }
}
