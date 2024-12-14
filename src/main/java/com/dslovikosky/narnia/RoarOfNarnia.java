package com.dslovikosky.narnia;

import com.dslovikosky.narnia.common.constants.Constants;
import com.dslovikosky.narnia.common.constants.ModAttachmentTypes;
import com.dslovikosky.narnia.common.constants.ModCreativeTabs;
import com.dslovikosky.narnia.common.constants.ModDataComponentTypes;
import com.dslovikosky.narnia.common.constants.ModFeatures;
import com.dslovikosky.narnia.common.constants.ModItems;
import com.dslovikosky.narnia.common.event.AttachmentHandler;
import com.dslovikosky.narnia.common.event.DataGenerationHandler;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;

@Mod(Constants.MOD_ID)
public class RoarOfNarnia {
    public RoarOfNarnia(final IEventBus modBus) {
        modBus.register(new DataGenerationHandler());
        ModItems.ITEMS.register(modBus);
        ModCreativeTabs.CREATIVE_TABS.register(modBus);
        ModDataComponentTypes.DATA_COMPONENT_TYPES.register(modBus);
        ModFeatures.FEATURES.register(modBus);
        ModAttachmentTypes.ATTACHMENT_TYPES.register(modBus);

        final IEventBus forgeBus = NeoForge.EVENT_BUS;

        forgeBus.register(new AttachmentHandler());
    }
}
