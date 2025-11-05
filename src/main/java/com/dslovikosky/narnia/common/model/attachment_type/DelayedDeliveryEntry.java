package com.dslovikosky.narnia.common.model.attachment_type;

import com.dslovikosky.narnia.common.constants.ModSpellDeliveryMethods;
import com.dslovikosky.narnia.common.spell.component.DeliveryTransitionState;
import com.dslovikosky.narnia.common.spell.component.SpellComponentInstance;
import com.dslovikosky.narnia.common.spell.component.deliveryMethod.base.SpellDeliveryMethod;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public class DelayedDeliveryEntry {
    public static final Codec<DelayedDeliveryEntry> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                            Codec.LONG.fieldOf("ticks_left").forGetter(DelayedDeliveryEntry::getTicksLeft),
                            CompoundTag.CODEC.xmap(DeliveryTransitionState::new, DeliveryTransitionState::toTag).fieldOf("state").forGetter(DelayedDeliveryEntry::getState)
                    )
                    .apply(instance, DelayedDeliveryEntry::new)
    );

    private DeliveryTransitionState state;
    private long ticksLeft;

    public DelayedDeliveryEntry(final DeliveryTransitionState state) {
        // Grab the delivery method and get the number of ticks to delay
        final SpellComponentInstance<SpellDeliveryMethod> deliveryMethod = state.getCurrentStage().getDeliveryInstance();
        final double delayInSeconds = ModSpellDeliveryMethods.DELAY.get().getDelay(deliveryMethod);
        this.ticksLeft = (long) Math.ceil(delayInSeconds * 20.0);
        this.state = state;
    }

    private DelayedDeliveryEntry(final long ticksLeft, final DeliveryTransitionState state) {
        this.ticksLeft = ticksLeft;
        this.state = state;
    }

    public void tick() {
        this.ticksLeft = this.ticksLeft - 1;
    }

    public boolean isReadyToFire() {
        return ticksLeft <= 0;
    }

    public void fire() {
        // Update the state to reflect the current world position if the entity targeted has moved
        final Entity entity = state.getEntity();
        if (entity != null) {
            final Vec3 position = entity.getEyePosition(1.0f);
            state = state.copy(position, BlockPos.containing(position), entity.getLookAngle(), entity.getUpVector(1f));
        }
        // Proc effects and transition, then return true since we delivered
        ModSpellDeliveryMethods.DELAY.get().procEffects(state);
        ModSpellDeliveryMethods.DELAY.get().transitionFrom(state);
    }

    public long getTicksLeft() {
        return ticksLeft;
    }

    public DeliveryTransitionState getState() {
        return state;
    }
}
