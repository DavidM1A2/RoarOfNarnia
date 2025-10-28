package com.dslovikosky.narnia.common.spell.component.deliveryMethod;

import com.dslovikosky.narnia.common.constants.Constants;
import com.dslovikosky.narnia.common.entity.spell.SpellProjectileEntity;
import com.dslovikosky.narnia.common.spell.component.DeliveryTransitionState;
import com.dslovikosky.narnia.common.spell.component.SpellComponentInstance;
import com.dslovikosky.narnia.common.spell.component.deliveryMethod.base.SpellDeliveryMethod;
import com.dslovikosky.narnia.common.spell.component.property.SpellComponentPropertyFactory;

import java.awt.Color;

public class ProjectileSpellDeliveryMethod extends SpellDeliveryMethod {
    private static final String NBT_SPEED = "speed";
    private static final String NBT_RANGE = "range";
    private static final String NBT_COLOR = "color";

    public ProjectileSpellDeliveryMethod() {
        super(Constants.modLocation("projectile"));
        addEditableProperty(
                SpellComponentPropertyFactory.doubleProperty()
                        .withBaseName(getUnlocalizedPropertyBaseName("range"))
                        .withSetter(this::setRange)
                        .withGetter(this::getRange)
                        .withDefaultValue(50.0)
                        .withMinValue(1.0)
                        .withMaxValue(300.0)
                        .build()
        );
        addEditableProperty(
                SpellComponentPropertyFactory.doubleProperty()
                        .withBaseName(getUnlocalizedPropertyBaseName("speed"))
                        .withSetter(this::setSpeed)
                        .withGetter(this::getSpeed)
                        .withDefaultValue(12.0)
                        .withMinValue(1.0)
                        .withMaxValue(100.0)
                        .build()
        );
        addEditableProperty(
                SpellComponentPropertyFactory.colorProperty()
                        .withBaseName(getUnlocalizedPropertyBaseName("color"))
                        .withSetter(this::setColor)
                        .withGetter(this::getColor)
                        .withDefaultValue(new Color(155, 0, 255))
                        .build()
        );
    }

    @Override
    public void execute(DeliveryTransitionState state) {
        final SpellProjectileEntity spellProjectile = new SpellProjectileEntity(
                state.getLevel(),
                state.getSpell(),
                state.getStageIndex(),
                state.getCasterEntity(),
                state.getPosition(),
                state.getDirection(),
                state.getEntity()
        );
        state.getLevel().addFreshEntity(spellProjectile);
    }

    @Override
    public double getDeliveryCost(SpellComponentInstance<SpellDeliveryMethod> instance) {
        // 0.01 to 1.0 speed multiplier
        final double speedCostMultiplier = getSpeed(instance) / 100.0;
        // 10 blocks per vitae
        final double rangeCost = getRange(instance) * 0.1;
        return speedCostMultiplier * rangeCost;
    }

    @Override
    public double getMultiplicity(SpellComponentInstance<SpellDeliveryMethod> instance) {
        return 1.0;
    }

    public void setSpeed(final SpellComponentInstance<?> instance, final double speed) {
        instance.getData().putDouble(NBT_SPEED, speed);
    }

    public double getSpeed(final SpellComponentInstance<?> instance) {
        return instance.getData().getDoubleOr(NBT_SPEED, 1);
    }

    public void setRange(final SpellComponentInstance<?> instance, final double range) {
        instance.getData().putDouble(NBT_RANGE, range);
    }

    public double getRange(final SpellComponentInstance<?> instance) {
        return instance.getData().getDoubleOr(NBT_RANGE, 10);
    }

    public void setColor(final SpellComponentInstance<?> instance, final Color color) {
        instance.getData().putString(NBT_COLOR, color.getRed() + " " + color.getGreen() + " " + color.getBlue());
    }

    public Color getColor(final SpellComponentInstance<?> instance) {
        final String[] rgb = instance.getData().getStringOr(NBT_COLOR, "155 0 255").split("\\s+");
        return new Color(Integer.parseInt(rgb[0]), Integer.parseInt(rgb[1]), Integer.parseInt(rgb[2]));
    }
}
