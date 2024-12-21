package com.dslovikosky.narnia;

import com.dslovikosky.narnia.common.constants.Constants;
import com.dslovikosky.narnia.common.constants.ModAttachmentTypes;
import com.dslovikosky.narnia.common.constants.ModBlockEntities;
import com.dslovikosky.narnia.common.constants.ModBlocks;
import com.dslovikosky.narnia.common.constants.ModCreativeTabs;
import com.dslovikosky.narnia.common.constants.ModDataComponentTypes;
import com.dslovikosky.narnia.common.constants.ModFeatures;
import com.dslovikosky.narnia.common.constants.ModItems;
import com.dslovikosky.narnia.common.constants.ModStructurePieces;
import com.dslovikosky.narnia.common.constants.ModStructureTypes;
import com.dslovikosky.narnia.common.event.AttachmentHandler;
import com.dslovikosky.narnia.common.event.DataGenerationHandler;
import com.dslovikosky.narnia.common.event.ModColorRegister;
import com.dslovikosky.narnia.common.event.RendererRegister;
import com.dslovikosky.narnia.common.event.SignBlockRegister;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;

@Mod(Constants.MOD_ID)
public class RoarOfNarnia {
    public RoarOfNarnia(final IEventBus modBus) {
        modBus.register(new DataGenerationHandler());
        modBus.register(new ModColorRegister());
        modBus.register(new RendererRegister());
        modBus.register(new SignBlockRegister());

        ModBlocks.BLOCKS.register(modBus);
        ModItems.ITEMS.register(modBus);
        ModCreativeTabs.CREATIVE_TABS.register(modBus);
        ModDataComponentTypes.DATA_COMPONENT_TYPES.register(modBus);
        ModFeatures.FEATURES.register(modBus);
        ModAttachmentTypes.ATTACHMENT_TYPES.register(modBus);
        ModBlockEntities.BLOCK_ENTITIES.register(modBus);
        ModStructurePieces.STRUCTURE_PIECES.register(modBus);
        ModStructureTypes.STRUCTURE_TYPES.register(modBus);

        final IEventBus forgeBus = NeoForge.EVENT_BUS;

        forgeBus.register(new AttachmentHandler());
    }
}
