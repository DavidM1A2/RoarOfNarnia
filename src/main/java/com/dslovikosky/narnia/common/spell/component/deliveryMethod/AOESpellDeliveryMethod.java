package com.dslovikosky.narnia.common.spell.component.deliveryMethod;

import com.dslovikosky.narnia.common.constants.Constants;
import com.dslovikosky.narnia.common.constants.ModParticleTypes;
import com.dslovikosky.narnia.common.entity.spell.SpellAOEEntity;
import com.dslovikosky.narnia.common.spell.component.DeliveryTransitionState;
import com.dslovikosky.narnia.common.spell.component.SpellComponentInstance;
import com.dslovikosky.narnia.common.spell.component.deliveryMethod.base.SpellDeliveryMethod;
import com.dslovikosky.narnia.common.spell.component.effect.base.ProcResult;
import com.dslovikosky.narnia.common.spell.component.property.SpellComponentPropertyFactory;
import com.dslovikosky.narnia.common.utils.MathUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.Vec3;

import java.awt.Color;
import java.util.Random;
import java.util.stream.IntStream;

public class AOESpellDeliveryMethod extends SpellDeliveryMethod {
    private static final Random RANDOM = new Random();
    private static final String NBT_RADIUS = "radius";
    private static final String NBT_SHELL_ONLY = "shell_only";
    private static final String NBT_COLOR = "color";

    public AOESpellDeliveryMethod() {
        super(Constants.modLocation("aoe"));
        addEditableProperty(SpellComponentPropertyFactory.doubleProperty()
                .withBaseName(getUnlocalizedPropertyBaseName("radius"))
                .withSetter(this::setRadius)
                .withGetter(this::getRadius)
                .withDefaultValue(3.0)
                .withMinValue(1.0)
                .withMaxValue(20.0)
                .build());
        addEditableProperty(SpellComponentPropertyFactory.booleanProperty()
                .withBaseName(getUnlocalizedPropertyBaseName("shell_only"))
                .withSetter(this::setShellOnly)
                .withGetter(this::getShellOnly)
                .withDefaultValue(false)
                .build());
        addEditableProperty(SpellComponentPropertyFactory.colorProperty()
                .withBaseName(getUnlocalizedPropertyBaseName("color"))
                .withSetter(this::setColor)
                .withGetter(this::getColor)
                .withDefaultValue(Color.LIGHT_GRAY)
                .build());
    }

    @Override
    public void execute(final DeliveryTransitionState state) {
        final SpellComponentInstance<SpellDeliveryMethod> instance = state.getCurrentStage().getDeliveryInstance();
        final double radius = getRadius(instance);
        final boolean shellOnly = getShellOnly(instance);

        final Vec3 centerPos = state.getPosition();
        final ServerLevel level = state.getLevel();
        final int blockRadius = (int) Math.ceil(radius);

        level.addFreshEntity(new SpellAOEEntity(level, centerPos, (float) radius, getColor(instance)));
        // Go over every block in the radius
        boolean oneEffectProcd = false;
        for (int x = -blockRadius; x < blockRadius; x++) {
            for (int y = -blockRadius; y < blockRadius; y++) {
                for (int z = -blockRadius; z < blockRadius; z++) {
                    final Vec3 aoePos = centerPos.add(x, y, z);
                    final double distance = aoePos.distanceTo(centerPos);

                    // Test to see if the block is within the radius. If we're in "shellOnly" mode, only take the outer ~1.5 blocks of shell
                    if (distance <= radius && (!shellOnly || (radius - distance) < 1.5)) {
                        Vec3 direction = aoePos.subtract(centerPos).normalize();
                        // Direction may be 0 if aoePos = centerPos. In this case, move it up
                        if (direction == Vec3.ZERO) {
                            direction = new Vec3(0.0, 1.0, 0.0);
                        }
                        Vec3 normal = MathUtils.getNormal(direction);
                        // Straight up means we can't know our normal. Just use 1, 0, 0
                        if (normal == Vec3.ZERO) {
                            normal = new Vec3(1.0, 0.0, 0.0);
                        }

                        final DeliveryTransitionState newState = new DeliveryTransitionState(
                                state.getSpell(),
                                state.getStageIndex(),
                                level,
                                aoePos,
                                BlockPos.containing(aoePos),
                                direction,
                                normal,
                                state.getCasterEntity(),
                                null,
                                null
                        );
                        final ProcResult procResult = procEffects(newState, false);
                        oneEffectProcd = oneEffectProcd || procResult.isSuccess();
                        transitionFrom(newState);
                    }
                }
            }
        }

        // Create fizzle particles if no effect procd successfully
        if (!oneEffectProcd) {
            // Generate random fizzle particles within the sphere
            final int numParticles = (int) (radius * 4);
            IntStream.of(numParticles).forEach(index -> {
                double x = (RANDOM.nextDouble() - 0.5) * radius * 2;
                double y = (RANDOM.nextDouble() - 0.5) * radius * 2;
                double z = (RANDOM.nextDouble() - 0.5) * radius * 2;
                final double length = Math.sqrt(x * x + y * y + z * z);
                x = x / length;
                y = y / length;
                z = z / length;
                final double c = Math.pow(RANDOM.nextDouble(), 1.0 / 3.0) * radius;
                final Vec3 position = centerPos.add(x * c, y * c, z * c);
                level.sendParticles(ModParticleTypes.FIZZLE.get(),
                        position.x(), position.y(), position.z(), 1, 0.0, 1.0, 0.0, 0.1);
            });
        }
    }

    @Override
    public double getDeliveryCost(SpellComponentInstance<SpellDeliveryMethod> instance) {
        return 1.0;
    }

    @Override
    public double getMultiplicity(SpellComponentInstance<SpellDeliveryMethod> instance) {
        return Math.max(estimateBlocksHit(instance), 1);
    }

    private double estimateBlocksHit(final SpellComponentInstance<SpellDeliveryMethod> instance) {
        final boolean shellOnly = getShellOnly(instance);
        final double radius = getRadius(instance);
        final double volume = 4.0 / 3.0 * Math.PI * radius * radius * radius;
        if (shellOnly) {
            final double innerVolume = 4.0 / 3.0 * Math.PI * (radius - 1) * (radius - 1) * (radius - 1);
            // Subtract out the inner volume to just leave the "shell"
            return volume - innerVolume;
        } else {
            return volume;
        }
    }

    public void setRadius(final SpellComponentInstance<?> instance, final double radius) {
        instance.getData().putDouble(NBT_RADIUS, radius);
    }

    public double getRadius(final SpellComponentInstance<?> instance) {
        return instance.getData().getDoubleOr(NBT_RADIUS, 0.0);
    }

    public void setShellOnly(final SpellComponentInstance<?> instance, final boolean shellOnly) {
        instance.getData().putBoolean(NBT_SHELL_ONLY, shellOnly);
    }

    public boolean getShellOnly(final SpellComponentInstance<?> instance) {
        return instance.getData().getBooleanOr(NBT_SHELL_ONLY, false);
    }

    public void setColor(final SpellComponentInstance<?> instance, final Color color) {
        instance.getData().putString(NBT_COLOR, color.getRed() + " " + color.getGreen() + " " + color.getBlue());
    }

    public Color getColor(final SpellComponentInstance<?> instance) {
        final String[] rgb = instance.getData().getStringOr(NBT_COLOR, "255 255 255").split("\\s++");
        return new Color(Integer.parseInt(rgb[0]), Integer.parseInt(rgb[1]), Integer.parseInt(rgb[2]));
    }
}
