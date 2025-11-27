package com.dslovikosky.narnia.common.event;

import com.dslovikosky.narnia.common.constants.ModEntityTypes;
import com.dslovikosky.narnia.common.entity.JadisEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;

public class EntityRegistrationHandler {
    @SubscribeEvent
    public void onEntityAttributeCreationEvent(final EntityAttributeCreationEvent event) {
        event.put(ModEntityTypes.JADIS.get(), JadisEntity.createAttributes().build());
    }
}
