package com.dslovikosky.narnia.common.spell.component.effect;

import com.dslovikosky.narnia.common.constants.Constants;
import com.dslovikosky.narnia.common.particle.ArrowTrailParticleData;
import com.dslovikosky.narnia.common.spell.component.DeliveryTransitionState;
import com.dslovikosky.narnia.common.spell.component.SpellComponentInstance;
import com.dslovikosky.narnia.common.spell.component.effect.base.ProcResult;
import com.dslovikosky.narnia.common.spell.component.effect.base.SpellEffect;
import com.dslovikosky.narnia.common.spell.component.property.SpellComponentPropertyFactory;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class SummonArrowEffect extends SpellEffect {
    private static final String NBT_SPEED = "speed";

    public SummonArrowEffect() {
        super(Constants.modLocation("summon_arrow"));
        addEditableProperty(
                SpellComponentPropertyFactory.floatProperty()
                        .withBaseName(getUnlocalizedPropertyBaseName("speed"))
                        .withSetter(this::setSpeed)
                        .withGetter(this::getSpeed)
                        .withDefaultValue(40f)
                        .withMinValue(0f)
                        // Turns out MC doesn't let things move faster than about 3.9 blocks/tick. This speed is hardcoded as part of the SEntityVelocityPacket :(
                        .withMaxValue(75f)
                        .build()
        );
    }

    @Override
    public ProcResult proc(DeliveryTransitionState state, SpellComponentInstance<SpellEffect> instance) {
        // Divide by 20 to convert speed per second to speed per tick
        final float speed = getSpeed(instance) / 20f;
        final ServerLevel world = state.getLevel();
        final Vec3 position = state.getPosition();
        final Vec3 direction = state.getDirection();

        final Arrow arrowEntity = new Arrow(world, position.x, position.y, position.z, ItemStack.EMPTY, null);

        arrowEntity.setOwner(state.getEntity());
        arrowEntity.shoot(direction.x, direction.y, direction.z, speed, 0f);
        arrowEntity.pickup = AbstractArrow.Pickup.DISALLOWED;
        world.addFreshEntity(arrowEntity);
        for (int i = 0; i < Math.ceil(speed * 5); i++) {
            world.sendParticles(new ArrowTrailParticleData(arrowEntity.getId(), i), position.x(), position.y(), position.z(),
                    0, 0.0, 0.0, 0.0, 0.0);
        }
        return ProcResult.success();
    }

    @Override
    public double getCost(SpellComponentInstance<SpellEffect> instance) {
        return 5.0;
    }

    public void setSpeed(final SpellComponentInstance<?> instance, final float speed) {
        instance.getData().putFloat(NBT_SPEED, speed);
    }

    public float getSpeed(final SpellComponentInstance<?> instance) {
        return instance.getData().getFloatOr(NBT_SPEED, 0);
    }
}
