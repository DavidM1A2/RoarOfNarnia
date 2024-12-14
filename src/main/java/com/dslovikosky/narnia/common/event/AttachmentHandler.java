package com.dslovikosky.narnia.common.event;

import com.dslovikosky.narnia.common.constants.ModAttachmentTypes;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

public class AttachmentHandler {
    @SubscribeEvent
    public void onPlayerEventClone(final PlayerEvent.Clone event) {
        if (event.isWasDeath()) {
            ModAttachmentTypes.ATTACHMENT_TYPES.getEntries().forEach(type -> copyAttachment(event, type.get()));
        }
    }

    private <T> void copyAttachment(final PlayerEvent.Clone event, final AttachmentType<T> attachment) {
        if (event.getOriginal().hasData(attachment)) {
            event.getEntity().setData(attachment, event.getOriginal().getData(attachment));
        }
    }
}
