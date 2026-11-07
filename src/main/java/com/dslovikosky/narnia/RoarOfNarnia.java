package com.dslovikosky.narnia;

import com.dslovikosky.narnia.client.event.*;
import com.dslovikosky.narnia.client.proxy.ClientProxy;
import com.dslovikosky.narnia.common.constants.*;
import com.dslovikosky.narnia.common.event.*;
import com.dslovikosky.narnia.common.event.datagenproviders.ModDatapackObjectProvider;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.neoforge.common.NeoForge;

@Mod(Constants.MOD_ID)
public class RoarOfNarnia {
    public RoarOfNarnia(final IEventBus modBus) {
        final FMLLoader fmlLoader = FMLLoader.getCurrent();

        modBus.register(new DataGenerationHandler());
        modBus.register(new ModDatapackObjectProvider());
        modBus.register(new SignBlockRegister());
        modBus.register(new PacketRegistrationHandler());
        modBus.register(new RegistryRegister());
        modBus.register(new KeyMappingRegister());
        modBus.register(new EntityRegistrationHandler());
        if (fmlLoader.getDist() == Dist.CLIENT) {
            modBus.register(new BlockEntityRendererRegister());
            modBus.register(new EntityRendererRegister());
            modBus.register(new EnvironmentEffectRendererRegister());
            modBus.register(new ModColorRegister());
            modBus.register(new ClientReloadHandler());
            modBus.register(new RenderPipelineRegister());
            modBus.register(new ParticleRegister());
        }

        ModBlocks.BLOCKS.register(modBus);
        ModItems.ITEMS.register(modBus);
        ModCreativeTabs.CREATIVE_TABS.register(modBus);
        ModDataComponentTypes.DATA_COMPONENT_TYPES.register(modBus);
        ModFeatureTypes.FEATURE_TYPES.register(modBus);
        ModAttachmentTypes.ATTACHMENT_TYPES.register(modBus);
        ModBlockEntities.BLOCK_ENTITY_TYPES.register(modBus);
        ModStructurePieces.STRUCTURE_PIECES.register(modBus);
        ModStructureTypes.STRUCTURE_TYPES.register(modBus);
        ModSoundEvents.SOUND_EVENTS.register(modBus);
        ModSchematics.SCHEMATICS.register(modBus);
        ModEntityTypes.ENTITY_TYPES.register(modBus);
        ModStructurePlacementTypes.STRUCTURE_PLACEMENTS.register(modBus);
        ModMobEffects.MOB_EFFECTS.register(modBus);
        ModSpellEffects.SPELL_EFFECTS.register(modBus);
        ModSpellPowerSources.SPELL_POWER_SOURCES.register(modBus);
        ModSpellDeliveryMethods.SPELL_DELIVERY_METHODS.register(modBus);
        ModParticleTypes.PARTICLES.register(modBus);
        ModEntityDataSerializers.ENTITY_DATA_SERIALIZERS.register(modBus);
        ModChunkGenerators.CHUNK_GENERATORS.register(modBus);

        final IEventBus forgeBus = NeoForge.EVENT_BUS;

        forgeBus.register(new AttachmentHandler());
        forgeBus.register(new SchematicHandler());
        forgeBus.register(new WoodBetweenTheWorldsHandler());
        forgeBus.register(new DynamicPoolWorldHandler());
        forgeBus.register(new RingHandler());
        forgeBus.register(new SpellInnateHandler());
        forgeBus.register(new SpellLunarHandler());
        forgeBus.register(new SpellSolarHandler());
        forgeBus.register(new SpellThermalHandler());
        forgeBus.register(new DelayedDeliveryEntryHandler());
        forgeBus.register(new SpellCharmHandler());
        forgeBus.register(new SpellFreezeHandler());
        if (fmlLoader.getDist() == Dist.CLIENT) {
            forgeBus.register(new WoodBetweenTheWorldsClientHandler());
            forgeBus.register(new KeyInputEventHandler());
            forgeBus.register(new ClientSpellFreezeHandler());
            forgeBus.register(ClientProxy.CHARN_SCREEN_SHAKE_HANDLER);
        }
    }
}
