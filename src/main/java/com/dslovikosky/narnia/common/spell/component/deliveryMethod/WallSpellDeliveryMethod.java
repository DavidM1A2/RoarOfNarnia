package com.dslovikosky.narnia.common.spell.component.deliveryMethod;

import com.dslovikosky.narnia.common.constants.Constants;
import com.dslovikosky.narnia.common.constants.ModParticleTypes;
import com.dslovikosky.narnia.common.entity.spell.SpellWallEntity;
import com.dslovikosky.narnia.common.spell.component.DeliveryTransitionState;
import com.dslovikosky.narnia.common.spell.component.SpellComponentInstance;
import com.dslovikosky.narnia.common.spell.component.deliveryMethod.base.SpellDeliveryMethod;
import com.dslovikosky.narnia.common.spell.component.effect.base.ProcResult;
import com.dslovikosky.narnia.common.spell.component.property.SpellComponentPropertyFactory;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.Vec3;

import java.awt.Color;
import java.util.Random;

public class WallSpellDeliveryMethod extends SpellDeliveryMethod {
    private static final Random RANDOM = new Random();

    private static final String NBT_WIDTH = "width";
    private static final String NBT_HEIGHT = "height";
    private static final String NBT_COLOR = "color";

    public WallSpellDeliveryMethod() {
        super(Constants.modLocation("wall"));
        addEditableProperty(
                SpellComponentPropertyFactory.doubleProperty()
                        .withBaseName(getUnlocalizedPropertyBaseName("width"))
                        .withSetter(this::setWidth)
                        .withGetter(this::getWidth)
                        .withDefaultValue(2.0)
                        .withMinValue(1.0)
                        .withMaxValue(20.0)
                        .build()
        );
        addEditableProperty(
                SpellComponentPropertyFactory.doubleProperty()
                        .withBaseName(getUnlocalizedPropertyBaseName("height"))
                        .withSetter(this::setHeight)
                        .withGetter(this::getHeight)
                        .withDefaultValue(2.0)
                        .withMinValue(1.0)
                        .withMaxValue(20.0)
                        .build()
        );
        addEditableProperty(
                SpellComponentPropertyFactory.colorProperty()
                        .withBaseName(getUnlocalizedPropertyBaseName("color"))
                        .withSetter(this::setColor)
                        .withGetter(this::getColor)
                        .withDefaultValue(Color.LIGHT_GRAY)
                        .build()
        );
    }

    @Override
    public void execute(final DeliveryTransitionState state) {
        final ServerLevel level = state.getLevel();
        final SpellComponentInstance<SpellDeliveryMethod> deliveryMethod = state.getCurrentStage().getDeliveryInstance();
        final double width = getWidth(deliveryMethod);
        final double height = getHeight(deliveryMethod);
        final Vec3 position = state.getPosition();
        final Vec3 forwardBackwardDir = state.getDirection();
        final Vec3 upDownDir = state.getNormal();
        final Vec3 leftRightDir = forwardBackwardDir.cross(upDownDir).normalize();

        level.addFreshEntity(new SpellWallEntity(level, position, leftRightDir.scale(width), upDownDir.scale(height), getColor(deliveryMethod)));
        // Add 0.5 to center on a block
        boolean oneEffectProcd = false;
        double xOffset = -width / 2;
        while (xOffset < width / 2) {
            double zOffset = -height / 2;
            while (zOffset < height / 2) {
                final Vec3 wallPos = position
                        .add(leftRightDir.scale(xOffset))
                        .add(upDownDir.scale(zOffset));

                final DeliveryTransitionState newState = new DeliveryTransitionState(
                        state.getSpell(),
                        state.getStageIndex(),
                        level,
                        wallPos,
                        BlockPos.containing(wallPos),
                        forwardBackwardDir,
                        upDownDir,
                        state.getCasterEntity(),
                        null,
                        null
                );
                final ProcResult procResult = procEffects(newState, false);
                oneEffectProcd = oneEffectProcd || procResult.isSuccess();
                transitionFrom(newState);

                zOffset = zOffset + 1.0;
            }
            xOffset = xOffset + 1.0;
        }

        // Create fizzle particles if no effect procd successfully
        if (!oneEffectProcd) {
            final int numParticles = (int) (1 + width + height) * 2;
            for (int i = 0; i < numParticles; i++) {
                final Vec3 particlePosition = position.add(upDownDir.scale((RANDOM.nextDouble() - 0.5) * height))
                        .add(leftRightDir.scale((RANDOM.nextDouble() - 0.5) * width));
                level.sendParticles(ModParticleTypes.FIZZLE.get(),
                        particlePosition.x(), particlePosition.y(), particlePosition.z(), 0, 0.0, 1.0, 0.0, 0.1);
            }
        }
    }

    @Override
    public double getDeliveryCost(final SpellComponentInstance<SpellDeliveryMethod> instance) {
        return 1.0;
    }

    @Override
    public double getMultiplicity(final SpellComponentInstance<SpellDeliveryMethod> instance) {
        return getWidth(instance) * getHeight(instance);
    }

    public void setWidth(final SpellComponentInstance<?> instance, final double width) {
        instance.getData().putDouble(NBT_WIDTH, width);
    }

    public double getWidth(final SpellComponentInstance<?> instance) {
        return instance.getData().getDoubleOr(NBT_WIDTH, 0.0);
    }

    public void setHeight(final SpellComponentInstance<?> instance, final double height) {
        instance.getData().putDouble(NBT_HEIGHT, height);
    }

    public double getHeight(final SpellComponentInstance<?> instance) {
        return instance.getData().getDoubleOr(NBT_HEIGHT, 0.0);
    }

    public void setColor(final SpellComponentInstance<?> instance, final Color color) {
        instance.getData().putString(NBT_COLOR, color.getRed() + " " + color.getGreen() + " " + color.getBlue());
    }

    public Color getColor(final SpellComponentInstance<?> instance) {
        final String[] rgb = instance.getData().getStringOr(NBT_COLOR, "255 255 255").split("\\s++");
        return new Color(Integer.parseInt(rgb[0]), Integer.parseInt(rgb[1]), Integer.parseInt(rgb[2]));
    }
}
