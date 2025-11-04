package com.dslovikosky.narnia.common.spell.component.deliveryMethod;

import com.dslovikosky.narnia.common.constants.Constants;
import com.dslovikosky.narnia.common.constants.ModParticleTypes;
import com.dslovikosky.narnia.common.spell.component.DeliveryTransitionState;
import com.dslovikosky.narnia.common.spell.component.SpellComponentInstance;
import com.dslovikosky.narnia.common.spell.component.deliveryMethod.base.SpellDeliveryMethod;
import com.dslovikosky.narnia.common.spell.component.property.SpellComponentPropertyFactory;
import com.dslovikosky.narnia.common.utils.MathUtils;
import net.minecraft.world.phys.Vec3;

public class RotateSpellDeliveryMethod extends SpellDeliveryMethod {
    private static final String NBT_YAW = "yaw";
    private static final String NBT_PITCH = "pitch";

    public RotateSpellDeliveryMethod() {
        super(Constants.modLocation("rotate"));
        addEditableProperty(
                SpellComponentPropertyFactory.doubleProperty()
                        .withBaseName(getUnlocalizedPropertyBaseName("yaw"))
                        .withSetter(this::setYaw)
                        .withGetter(this::getYaw)
                        .withDefaultValue(0.0)
                        .withMinValue(-180.0)
                        .withMaxValue(180.0)
                        .build()
        );
        addEditableProperty(
                SpellComponentPropertyFactory.doubleProperty()
                        .withBaseName(getUnlocalizedPropertyBaseName("pitch"))
                        .withSetter(this::setPitch)
                        .withGetter(this::getPitch)
                        .withDefaultValue(0.0)
                        .withMinValue(-90.0)
                        .withMaxValue(90.0)
                        .build()
        );
    }

    @Override
    public void execute(DeliveryTransitionState state) {
        final SpellComponentInstance<SpellDeliveryMethod> instance = state.getCurrentStage().getDeliveryInstance();
        final double yaw = Math.toRadians(getYaw(instance));
        final double pitch = Math.toRadians(getPitch(instance));

        final Vec3 forwardBackwardDir = state.getDirection();

        final Vec3 forwardBackwardDirAfterYaw = MathUtils.rotateAround(forwardBackwardDir, state.getNormal(), yaw);
        final Vec3 leftRightDir = state.getNormal().cross(forwardBackwardDirAfterYaw).normalize();

        final Vec3 newDir = MathUtils.rotateAround(forwardBackwardDirAfterYaw, leftRightDir, pitch);
        final Vec3 newNormal = MathUtils.rotateAround(state.getNormal(), leftRightDir, pitch);

        final DeliveryTransitionState newState = state.copy(newDir, newNormal);

        procEffects(newState);
        transitionFrom(newState);

        final Vec3 position = state.getPosition();
        newState.getLevel().sendParticles(ModParticleTypes.ROTATE.get(), position.x(), position.y(), position.z(),
                1, 0.0, 0.0, 0.0, 0.0);
    }

    @Override
    public double getDeliveryCost(SpellComponentInstance<SpellDeliveryMethod> instance) {
        return 0.2;
    }

    @Override
    public double getMultiplicity(SpellComponentInstance<SpellDeliveryMethod> instance) {
        return 1.0;
    }

    public void setYaw(SpellComponentInstance<?> instance, double yaw) {
        instance.getData().putDouble(NBT_YAW, yaw);
    }

    public double getYaw(SpellComponentInstance<?> instance) {
        return instance.getData().getDoubleOr(NBT_YAW, 0.0);
    }

    public void setPitch(SpellComponentInstance<?> instance, double pitch) {
        instance.getData().putDouble(NBT_PITCH, pitch);
    }

    public double getPitch(SpellComponentInstance<?> instance) {
        return instance.getData().getDoubleOr(NBT_PITCH, 0.0);
    }
}
