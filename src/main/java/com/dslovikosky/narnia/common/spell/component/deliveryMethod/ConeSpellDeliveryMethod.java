package com.dslovikosky.narnia.common.spell.component.deliveryMethod;

import com.dslovikosky.narnia.common.constants.Constants;
import com.dslovikosky.narnia.common.constants.ModParticleTypes;
import com.dslovikosky.narnia.common.entity.spell.SpellConeEntity;
import com.dslovikosky.narnia.common.spell.component.DeliveryTransitionState;
import com.dslovikosky.narnia.common.spell.component.SpellComponentInstance;
import com.dslovikosky.narnia.common.spell.component.deliveryMethod.base.SpellDeliveryMethod;
import com.dslovikosky.narnia.common.spell.component.effect.base.ProcResult;
import com.dslovikosky.narnia.common.spell.component.property.SpellComponentPropertyFactory;
import com.dslovikosky.narnia.common.utils.ConeUtils;
import com.dslovikosky.narnia.common.utils.MathUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.awt.Color;
import java.util.Random;

public class ConeSpellDeliveryMethod extends SpellDeliveryMethod {
    private static final Random RANDOM = new Random();

    private static final double SHELL_ONLY_MARGIN = 1.5;

    private static final String NBT_RADIUS = "radius";
    private static final String NBT_LENGTH = "length";
    private static final String NBT_SHELL_ONLY = "shell_only";
    private static final String NBT_COLOR = "color";

    public ConeSpellDeliveryMethod() {
        super(Constants.modLocation("cone"));
        addEditableProperty(
                SpellComponentPropertyFactory.doubleProperty()
                        .withBaseName(getUnlocalizedPropertyBaseName("radius"))
                        .withSetter(this::setRadius)
                        .withGetter(this::getRadius)
                        .withDefaultValue(2.0)
                        .withMinValue(1.0)
                        .withMaxValue(10.0)
                        .build()
        );
        addEditableProperty(
                SpellComponentPropertyFactory.doubleProperty()
                        .withBaseName(getUnlocalizedPropertyBaseName("length"))
                        .withSetter(this::setLength)
                        .withGetter(this::getLength)
                        .withDefaultValue(4.0)
                        .withMinValue(1.0)
                        .withMaxValue(20.0)
                        .build()
        );
        addEditableProperty(
                SpellComponentPropertyFactory.booleanProperty()
                        .withBaseName(getUnlocalizedPropertyBaseName("shell_only"))
                        .withSetter(this::setShellOnly)
                        .withGetter(this::getShellOnly)
                        .withDefaultValue(false)
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
        final double radius = getRadius(deliveryMethod);
        final double length = getLength(deliveryMethod);
        final boolean shellOnly = getShellOnly(deliveryMethod);

        final Vec3 tipPos = state.getPosition();
        final Vec3 forwardDir = state.getDirection();
        final Vec3 upDir = state.getNormal();
        final Vec3 leftDir = forwardDir.cross(upDir).normalize();

        level.addFreshEntity(new SpellConeEntity(level, tipPos, (float) radius, (float) length, getColor(deliveryMethod), forwardDir));

        // Find the smallest (x, y, z) and biggest (x, y, z) coordinates to loop through. Ceil/floor the values, so we don't cut off any blocks partially within the cone
        final AABB boundingBox = ConeUtils.getBoundingBox(tipPos, forwardDir, radius, length, upDir);
        final int minX = (int) Math.floor(boundingBox.minX);
        final int minY = (int) Math.floor(boundingBox.minY);
        final int minZ = (int) Math.floor(boundingBox.minZ);
        final int maxX = (int) Math.ceil(boundingBox.maxX);
        final int maxY = (int) Math.ceil(boundingBox.maxY);
        final int maxZ = (int) Math.ceil(boundingBox.maxZ);

        // We'll need a triple for loop to go over every block in the rectangle surrounding the cone. We'll filter out any points that lie outside the cone.
        boolean oneEffectProcd = false;
        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    final Vec3 conePos = new Vec3(x, y, z);
                    if (isWithinCone(tipPos, forwardDir, length, radius, conePos, shellOnly)) {
                        Vec3 newDirection = conePos.subtract(tipPos).normalize();
                        // Direction may be 0 if conePos = tipPos. In this case, move it up
                        if (newDirection == Vec3.ZERO) {
                            newDirection = new Vec3(0.0, 1.0, 0.0);
                        }
                        Vec3 newNormal = MathUtils.getNormal(newDirection);
                        // Straight up means we can't know our normal. Just use 1, 0, 0
                        if (newNormal == Vec3.ZERO) {
                            newNormal = new Vec3(1.0, 0.0, 0.0);
                        }

                        final DeliveryTransitionState newState = new DeliveryTransitionState(
                                state.getSpell(),
                                state.getStageIndex(),
                                level,
                                conePos,
                                BlockPos.containing(conePos),
                                newDirection,
                                newNormal,
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

        // Create fizzle particles in the cone if the effect failed to proc
        if (!oneEffectProcd) {
            // Pick random positions within the cone
            final int numParticles = (int) (5 + length + radius) * 2;
            for (int i = 0; i < numParticles; i++) {
                final double distanceDownCone = RANDOM.nextDouble() * length;
                final double radiusAtDistance = distanceDownCone * radius / length;
                final Vec3 position = tipPos.add(forwardDir.scale(distanceDownCone))
                        .add(leftDir.scale((RANDOM.nextDouble() - 0.5) * radiusAtDistance * 2))
                        .add(upDir.scale((RANDOM.nextDouble() - 0.5) * radiusAtDistance * 2));

                level.sendParticles(ModParticleTypes.FIZZLE.get(),
                        position.x(), position.y(), position.z(), 0, 0.0, 1.0, 0.0, 0.1);
            }
        }
    }

    private boolean isWithinCone(final Vec3 tip, final Vec3 direction, final double height, final double radius, final Vec3 point, final boolean shellOnly) {
        // To detect if a point is within the cone or not, we can use: https://stackoverflow.com/questions/12826117/how-can-i-detect-if-a-point-is-inside-a-cone-or-not-in-3d-space
        final double coneDistance = point.subtract(tip).dot(direction);
        if (coneDistance < 0 || coneDistance > height) {
            return false;
        }

        final double coneRadius = (coneDistance / height) * radius;
        final double orthographicDistance = point.subtract(tip).subtract(direction.scale(coneDistance)).length();
        return orthographicDistance < coneRadius && (!shellOnly || (coneRadius - orthographicDistance) <= SHELL_ONLY_MARGIN);
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
        final double radius = getRadius(instance);
        final double length = getLength(instance);
        final boolean shellOnly = getShellOnly(instance);

        final double volume = Math.PI * radius * radius * length / 3;
        if (shellOnly) {
            final double innerVolume = Math.PI * (radius - 1) * (radius - 1) * length / 3;
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

    public void setLength(final SpellComponentInstance<?> instance, final double length) {
        instance.getData().putDouble(NBT_LENGTH, length);
    }

    public double getLength(final SpellComponentInstance<?> instance) {
        return instance.getData().getDoubleOr(NBT_LENGTH, 0.0);
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
