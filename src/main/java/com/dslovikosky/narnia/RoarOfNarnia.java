package com.dslovikosky.narnia;

import com.dslovikosky.narnia.client.event.ClientReloadHandler;
import com.dslovikosky.narnia.client.event.RenderPipelineRegister;
import com.dslovikosky.narnia.client.event.WoodBetweenTheWorldsClientHandler;
import com.dslovikosky.narnia.common.constants.Constants;
import com.dslovikosky.narnia.common.constants.ModAttachmentTypes;
import com.dslovikosky.narnia.common.constants.ModBlockEntities;
import com.dslovikosky.narnia.common.constants.ModBlocks;
import com.dslovikosky.narnia.common.constants.ModCreativeTabs;
import com.dslovikosky.narnia.common.constants.ModDataComponentTypes;
import com.dslovikosky.narnia.common.constants.ModEntityTypes;
import com.dslovikosky.narnia.common.constants.ModFeatures;
import com.dslovikosky.narnia.common.constants.ModItems;
import com.dslovikosky.narnia.common.constants.ModMobEffects;
import com.dslovikosky.narnia.common.constants.ModSchematics;
import com.dslovikosky.narnia.common.constants.ModSoundEvents;
import com.dslovikosky.narnia.common.constants.ModStructurePieces;
import com.dslovikosky.narnia.common.constants.ModStructurePlacementTypes;
import com.dslovikosky.narnia.common.constants.ModStructureTypes;
import com.dslovikosky.narnia.common.event.AttachmentHandler;
import com.dslovikosky.narnia.common.event.BlockEntityRendererRegister;
import com.dslovikosky.narnia.common.event.DataGenerationHandler;
import com.dslovikosky.narnia.common.event.EntityRegistrationHandler;
import com.dslovikosky.narnia.common.event.ModColorRegister;
import com.dslovikosky.narnia.common.event.PacketRegistrationHandler;
import com.dslovikosky.narnia.common.event.RegistryRegister;
import com.dslovikosky.narnia.common.event.SchematicHandler;
import com.dslovikosky.narnia.common.event.SignBlockRegister;
import com.dslovikosky.narnia.common.event.WoodBetweenTheWorldsHandler;
import com.dslovikosky.narnia.common.event.datagenproviders.ModDatapackObjectProvider;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.neoforge.common.NeoForge;

@Mod(Constants.MOD_ID)
public class RoarOfNarnia {
    public RoarOfNarnia(final IEventBus modBus) {
        modBus.register(new DataGenerationHandler());
        modBus.register(new ModDatapackObjectProvider());
        modBus.register(new EntityRegistrationHandler());
        modBus.register(new BlockEntityRendererRegister());
        modBus.register(new SignBlockRegister());
        modBus.register(new PacketRegistrationHandler());
        modBus.register(new RegistryRegister());
        if (FMLLoader.getDist() == Dist.CLIENT) {
            modBus.register(new ModColorRegister());
            modBus.register(new ClientReloadHandler());
            modBus.register(new RenderPipelineRegister());
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
        ModSchematics.SCHEMATICS.register(modBus);
        ModEntityTypes.ENTITY_TYPES.register(modBus);
        ModStructurePlacementTypes.STRUCTURE_PLACEMENTS.register(modBus);
        ModMobEffects.MOB_EFFECTS.register(modBus);

        final IEventBus forgeBus = NeoForge.EVENT_BUS;

        forgeBus.register(new AttachmentHandler());
        forgeBus.register(new SchematicHandler());
        forgeBus.register(new WoodBetweenTheWorldsHandler());
        if (FMLLoader.getDist() == Dist.CLIENT) {
            forgeBus.register(new WoodBetweenTheWorldsClientHandler());
        }
    }
}
