package com.dslovikosky.narnia.common.spell.component.deliveryMethod;

import com.dslovikosky.narnia.common.constants.Constants;
import com.dslovikosky.narnia.common.constants.ModAttachmentTypes;
import com.dslovikosky.narnia.common.constants.ModParticleTypes;
import com.dslovikosky.narnia.common.model.attachment_type.DelayedDeliveryEntry;
import com.dslovikosky.narnia.common.spell.component.DeliveryTransitionState;
import com.dslovikosky.narnia.common.spell.component.SpellComponentInstance;
import com.dslovikosky.narnia.common.spell.component.deliveryMethod.base.SpellDeliveryMethod;
import com.dslovikosky.narnia.common.spell.component.property.SpellComponentPropertyFactory;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.Vec3;

import java.time.Duration;
import java.util.List;

public class DelaySpellDeliveryMethod extends SpellDeliveryMethod {
    private static final String NBT_DELAY = "delay";

    public DelaySpellDeliveryMethod() {
        super(Constants.modLocation("delay"));
        addEditableProperty(
                SpellComponentPropertyFactory.doubleProperty()
                        .withBaseName(getUnlocalizedPropertyBaseName("delay"))
                        .withSetter(this::setDelay)
                        .withGetter(this::getDelay)
                        .withDefaultValue(1.0)
                        .withMinValue(0.0)
                        .withMaxValue((double) Duration.ofMinutes(60L).getSeconds())
                        .build()
        );
    }

    @Override
    public void execute(DeliveryTransitionState state) {
        // Delayed adds this spell to the queue to wait
        final ServerLevel level = state.getLevel();
        final List<DelayedDeliveryEntry> delayedDeliveryEntries = level.getData(ModAttachmentTypes.DELAYED_DELIVERY_ENTRIES);
        delayedDeliveryEntries.add(new DelayedDeliveryEntry(state));
        level.setData(ModAttachmentTypes.DELAYED_DELIVERY_ENTRIES, delayedDeliveryEntries);

        final Vec3 position = state.getPosition();
        level.sendParticles(ModParticleTypes.DELAY.get(), position.x(), position.y(), position.z(),
                0, 1.0, 0.0, 0.0, state.getEntity() == null ? 1.2 : state.getEntity().getBbWidth() * 1.5);
    }

    @Override
    public double getDeliveryCost(SpellComponentInstance<SpellDeliveryMethod> instance) {
        // Each second of delay costs 0.25 vitae, and a 1 vitae base cost
        return 1.0 + getDelay(instance) * 0.25;
    }

    @Override
    public double getMultiplicity(SpellComponentInstance<SpellDeliveryMethod> instance) {
        return 1.0;
    }

    public void setDelay(final SpellComponentInstance<?> instance, final double delay) {
        instance.getData().putDouble(NBT_DELAY, delay);
    }

    public double getDelay(final SpellComponentInstance<?> instance) {
        return instance.getData().getDoubleOr(NBT_DELAY, 0.0);
    }
}
