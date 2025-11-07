package com.dslovikosky.narnia.common.spell.component.effect;

import com.dslovikosky.narnia.common.constants.Constants;
import com.dslovikosky.narnia.common.particle.HealParticleData;
import com.dslovikosky.narnia.common.spell.component.DeliveryTransitionState;
import com.dslovikosky.narnia.common.spell.component.SpellComponentInstance;
import com.dslovikosky.narnia.common.spell.component.effect.base.ProcResult;
import com.dslovikosky.narnia.common.spell.component.effect.base.SpellEffect;
import com.dslovikosky.narnia.common.spell.component.property.SpellComponentPropertyFactory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ArmorStand;

public class HealSpellEffect extends SpellEffect {
    private static final String NBT_AMOUNT = "amount";

    public HealSpellEffect() {
        super(Constants.modLocation("heal"));
        addEditableProperty(
                SpellComponentPropertyFactory.intProperty()
                        .withBaseName(getUnlocalizedPropertyBaseName("amount"))
                        .withSetter(this::setAmount)
                        .withGetter(this::getAmount)
                        .withDefaultValue(2)
                        .withMinValue(1)
                        .build()
        );
    }

    @Override
    public ProcResult proc(DeliveryTransitionState state, SpellComponentInstance<SpellEffect> instance) {
        final Entity entity = state.getEntity();
        if (entity instanceof LivingEntity livingEntity && !(entity instanceof ArmorStand)) {
            final int healAmount = getAmount(instance);
            livingEntity.heal(healAmount);
            for (int i = 0; i < healAmount; i++) {
                state.getLevel().sendParticles(new HealParticleData(entity.getId(), i * 360f / healAmount),
                        entity.getX(), entity.getY(), entity.getZ(), 0, 0.0, 0.0, 0.0, 0.0);
            }
        } else {
            return ProcResult.failure();
        }
        return ProcResult.success();
    }

    @Override
    public double getCost(SpellComponentInstance<SpellEffect> instance) {
        return 0.5 + getAmount(instance) * 5.0;
    }

    public void setAmount(final SpellComponentInstance<?> instance, final int amount) {
        instance.getData().putInt(NBT_AMOUNT, amount);
    }

    public int getAmount(final SpellComponentInstance<?> instance) {
        return instance.getData().getIntOr(NBT_AMOUNT, 0);
    }
}
