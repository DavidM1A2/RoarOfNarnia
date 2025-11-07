package com.dslovikosky.narnia.common.spell.component.effect;

import com.dslovikosky.narnia.common.constants.Constants;
import com.dslovikosky.narnia.common.particle.FlyParticleData;
import com.dslovikosky.narnia.common.spell.component.DeliveryTransitionState;
import com.dslovikosky.narnia.common.spell.component.SpellComponentInstance;
import com.dslovikosky.narnia.common.spell.component.effect.base.ProcResult;
import com.dslovikosky.narnia.common.spell.component.effect.base.SpellEffect;
import com.dslovikosky.narnia.common.spell.component.property.SpellComponentPropertyFactory;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public class PushSpellEffect extends SpellEffect {
    private static final String NBT_STRENGTH = "strength";

    public PushSpellEffect() {
        super(Constants.modLocation("push"));
        addEditableProperty(
                SpellComponentPropertyFactory.floatProperty()
                        .withBaseName(getUnlocalizedPropertyBaseName("strength"))
                        .withSetter(this::setStrength)
                        .withGetter(this::getStrength)
                        .withDefaultValue(10f)
                        .withMinValue(0f)
                        .withMaxValue(50f)
                        .build()
        );
    }

    @Override
    public ProcResult proc(DeliveryTransitionState state, SpellComponentInstance<SpellEffect> instance) {
        // Divide by 10 to make it roughly the number of blocks to move
        final float strength = getStrength(instance);
        final double pushStrength = strength / 10.0;
        final Entity entityHit = state.getEntity();
        if (entityHit != null) {
            final Vec3 pushDirection = state.getDirection().scale(pushStrength);
            entityHit.push(pushDirection.x(), pushDirection.y(), pushDirection.z());
            if (entityHit.getDeltaMovement().y() >= 0) {
                entityHit.fallDistance = 0f;
            }
            if (entityHit instanceof ServerPlayer serverPlayer) {
                serverPlayer.connection.send(new ClientboundSetEntityMotionPacket(entityHit));
            }

            final Vec3 position = state.getPosition();
            for (int i = 0; i < strength; i++) {
                state.getLevel().sendParticles(new FlyParticleData(entityHit.getId(), i), position.x(), position.y(), position.z(),
                        0, 0.0, 0.0, 0.0, 0.0);
            }
        } else {
            return ProcResult.failure();
        }
        return ProcResult.success();
    }

    @Override
    public double getCost(SpellComponentInstance<SpellEffect> instance) {
        return getStrength(instance) * 0.2;
    }

    public void setStrength(final SpellComponentInstance<?> instance, final float amount) {
        instance.getData().putFloat(NBT_STRENGTH, amount);
    }

    public float getStrength(final SpellComponentInstance<?> instance) {
        return instance.getData().getFloatOr(NBT_STRENGTH, 0);
    }
}
