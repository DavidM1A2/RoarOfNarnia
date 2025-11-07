package com.dslovikosky.narnia.client.event;

import com.dslovikosky.narnia.common.constants.ModAttachmentTypes;
import com.dslovikosky.narnia.common.model.attachment_type.SpellFreezeData;
import net.minecraft.client.player.ClientInput;
import net.minecraft.world.entity.player.Input;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.MovementInputUpdateEvent;

public class ClientSpellFreezeHandler {
    @SubscribeEvent
    public void onMovementInputUpdateEvent(final MovementInputUpdateEvent event) {
        final Player player = event.getEntity();
        final SpellFreezeData freezeData = player.getData(ModAttachmentTypes.SPELL_FREEZE_DATA);
        if (freezeData.getFreezeTicks() > 0) {
            final ClientInput input = event.getInput();
            input.keyPresses = Input.EMPTY;
        }
    }
}
