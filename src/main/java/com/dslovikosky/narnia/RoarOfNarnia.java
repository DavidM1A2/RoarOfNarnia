package com.dslovikosky.narnia;

import com.dslovikosky.narnia.common.constants.Constants;
import com.dslovikosky.narnia.common.constants.ModAttachmentTypes;
import com.dslovikosky.narnia.common.constants.ModBlockEntities;
import com.dslovikosky.narnia.common.constants.ModBlocks;
import com.dslovikosky.narnia.common.constants.ModBooks;
import com.dslovikosky.narnia.common.constants.ModChapters;
import com.dslovikosky.narnia.common.constants.ModCharacters;
import com.dslovikosky.narnia.common.constants.ModCreativeTabs;
import com.dslovikosky.narnia.common.constants.ModDataComponentTypes;
import com.dslovikosky.narnia.common.constants.ModFeatures;
import com.dslovikosky.narnia.common.constants.ModItems;
import com.dslovikosky.narnia.common.constants.ModSoundEvents;
import com.dslovikosky.narnia.common.constants.ModStructurePieces;
import com.dslovikosky.narnia.common.constants.ModStructureTypes;
import com.dslovikosky.narnia.common.event.ActiveSceneHandler;
import com.dslovikosky.narnia.common.event.AttachmentHandler;
import com.dslovikosky.narnia.common.event.DataGenerationHandler;
import com.dslovikosky.narnia.common.event.GivePlayerMagiciansNephewHandler;
import com.dslovikosky.narnia.common.event.ModColorRegister;
import com.dslovikosky.narnia.common.event.NarniaGlobalDataHandler;
import com.dslovikosky.narnia.common.event.PacketRegistrationHandler;
import com.dslovikosky.narnia.common.event.RegistryRegister;
import com.dslovikosky.narnia.common.event.RendererRegister;
import com.dslovikosky.narnia.common.event.SignBlockRegister;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.neoforge.common.NeoForge;

@Mod(Constants.MOD_ID)
public class RoarOfNarnia {
    public RoarOfNarnia(final IEventBus modBus) {
        modBus.register(new DataGenerationHandler());
        modBus.register(new RendererRegister());
        modBus.register(new SignBlockRegister());
        modBus.register(new PacketRegistrationHandler());
        modBus.register(new RegistryRegister());
        if (FMLLoader.getDist() == Dist.CLIENT) {
            modBus.register(new ModColorRegister());
        }

        ModBlocks.BLOCKS.register(modBus);
        ModItems.ITEMS.register(modBus);
        ModCreativeTabs.CREATIVE_TABS.register(modBus);
        ModDataComponentTypes.DATA_COMPONENT_TYPES.register(modBus);
        ModFeatures.FEATURES.register(modBus);
        ModAttachmentTypes.ATTACHMENT_TYPES.register(modBus);
        ModBlockEntities.BLOCK_ENTITIES.register(modBus);
        ModStructurePieces.STRUCTURE_PIECES.register(modBus);
        ModStructureTypes.STRUCTURE_TYPES.register(modBus);
        ModSoundEvents.SOUND_EVENTS.register(modBus);
        ModBooks.BOOKS.register(modBus);
        ModChapters.CHAPTERS.register(modBus);
        ModCharacters.CHARACTERS.register(modBus);

        final IEventBus forgeBus = NeoForge.EVENT_BUS;

        forgeBus.register(new AttachmentHandler());
        forgeBus.register(new GivePlayerMagiciansNephewHandler());
        forgeBus.register(new NarniaGlobalDataHandler());
        forgeBus.register(new ActiveSceneHandler());
    }
}
