package com.dslovikosky.narnia.common.spell.component.effect;

import com.dslovikosky.narnia.common.constants.Constants;
import com.dslovikosky.narnia.common.constants.ModParticleTypes;
import com.dslovikosky.narnia.common.spell.component.DeliveryTransitionState;
import com.dslovikosky.narnia.common.spell.component.SpellComponentInstance;
import com.dslovikosky.narnia.common.spell.component.effect.base.ProcResult;
import com.dslovikosky.narnia.common.spell.component.effect.base.SpellEffect;
import com.dslovikosky.narnia.common.spell.component.property.SpellComponentPropertyFactory;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class ExplosionSpellEffect extends SpellEffect {
    public static final float MAX_RADIUS = 40f;

    private static final String NBT_RADIUS = "radius";

    public ExplosionSpellEffect() {
        super(Constants.modLocation("explosion"));
        addEditableProperty(
                SpellComponentPropertyFactory.floatProperty()
                        .withBaseName(getUnlocalizedPropertyBaseName("radius"))
                        .withSetter(this::setRadius)
                        .withGetter(this::getRadius)
                        .withDefaultValue(2.0f)
                        .withMinValue(1.0f)
                        .withMaxValue(MAX_RADIUS)
                        .build()
        );
    }

    @Override
    public ProcResult proc(DeliveryTransitionState state, SpellComponentInstance<SpellEffect> instance) {
        final ServerLevel world = state.getLevel();
        final Vec3 position = state.getPosition();
        final int numParticles = Math.max((int) Math.floor(getRadius(instance) * getRadius(instance)), 10);
        world.explode(null, position.x(), position.y() - 0.01f, position.z(), getRadius(instance), Level.ExplosionInteraction.MOB);
        for (int i = 0; i < numParticles; i++) {
            // Particle's 'speed' isn't actually speed. It's the explosion's radius. We'll re-use the speed's x coordinate though since it's unused
            world.sendParticles(ModParticleTypes.EXPLOSION.get(), position.x(), position.y(), position.z(), 0, getRadius(instance), 0.0, 0.0, 1.0);
        }
        return ProcResult.success();
    }

    @Override
    public double getCost(SpellComponentInstance<SpellEffect> instance) {
        final float radius = getRadius(instance);
        // Base cost to make an explosion
        final double baseCost = 10.0;
        // Cubic cost based on explosion radius
        final double radiusCost = 1.0 * radius * radius * radius;
        return baseCost + radiusCost;
    }

    public void setRadius(final SpellComponentInstance<?> instance, final float radius) {
        instance.getData().putFloat(NBT_RADIUS, radius);
    }

    public float getRadius(final SpellComponentInstance<?> instance) {
        return instance.getData().getFloatOr(NBT_RADIUS, 1f);
    }
}
