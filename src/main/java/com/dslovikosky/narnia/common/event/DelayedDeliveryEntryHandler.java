package com.dslovikosky.narnia.common.event;

import com.dslovikosky.narnia.common.constants.ModAttachmentTypes;
import com.dslovikosky.narnia.common.model.attachment_type.DelayedDeliveryEntry;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;

import java.util.Iterator;
import java.util.List;

public class DelayedDeliveryEntryHandler {
    @SubscribeEvent
    public void onLevelTickEventPre(final LevelTickEvent.Pre event) {
        final Level level = event.getLevel();
        if (!level.isClientSide()) {
            final List<DelayedDeliveryEntry> delayedDeliveryEntries = level.getData(ModAttachmentTypes.DELAYED_DELIVERY_ENTRIES);
            final Iterator<DelayedDeliveryEntry> iterator = delayedDeliveryEntries.iterator();
            while (iterator.hasNext()) {
                final DelayedDeliveryEntry delayedDeliveryEntry = iterator.next();
                delayedDeliveryEntry.tick();
                if (delayedDeliveryEntry.isReadyToFire()) {
                    delayedDeliveryEntry.fire();
                    iterator.remove();
                }
            }
            level.setData(ModAttachmentTypes.DELAYED_DELIVERY_ENTRIES, delayedDeliveryEntries);
        }
    }
}
