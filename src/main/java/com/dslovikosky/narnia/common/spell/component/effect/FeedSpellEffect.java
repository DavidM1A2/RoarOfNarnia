package com.dslovikosky.narnia.common.spell.component.effect;

import com.dslovikosky.narnia.common.constants.Constants;
import com.dslovikosky.narnia.common.particle.FeedParticleData;
import com.dslovikosky.narnia.common.spell.component.DeliveryTransitionState;
import com.dslovikosky.narnia.common.spell.component.SpellComponentInstance;
import com.dslovikosky.narnia.common.spell.component.effect.base.ProcResult;
import com.dslovikosky.narnia.common.spell.component.effect.base.SpellEffect;
import com.dslovikosky.narnia.common.spell.component.property.SpellComponentPropertyFactory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.phys.Vec3;

public class FeedSpellEffect extends SpellEffect {
    private static final String NBT_HUNGER_AMOUNT = "hunger_amount";
    private static final String NBT_SATURATION_AMOUNT = "saturation_amount";

    public FeedSpellEffect() {
        super(Constants.modLocation("feed"));
        addEditableProperty(
                SpellComponentPropertyFactory.intProperty()
                        .withBaseName(getUnlocalizedPropertyBaseName("hunger_amount"))
                        .withSetter(this::setHungerAmount)
                        .withGetter(this::getHungerAmount)
                        .withDefaultValue(2)
                        .withMinValue(1)
                        .withMaxValue(20)
                        .build()
        );
        addEditableProperty(
                SpellComponentPropertyFactory.intProperty()
                        .withBaseName(getUnlocalizedPropertyBaseName("saturation_amount"))
                        .withSetter(this::setSaturationAmount)
                        .withGetter(this::getSaturationAmount)
                        .withDefaultValue(1)
                        .withMinValue(0)
                        .withMaxValue(20)
                        .build()
        );
    }

    @Override
    public ProcResult proc(DeliveryTransitionState state, SpellComponentInstance<SpellEffect> instance) {
        final Entity entity = state.getEntity();
        if (entity instanceof Player player) {
            final FoodData foodStats = player.getFoodData();
            foodStats.eat(getHungerAmount(instance), getSaturationAmount(instance));
            final float particleOffset = (float) (RANDOM.nextFloat() * Math.PI * 2);
            final Vec3 position = entity.position();
            for (int i = 0; i < 3; i++) {
                state.getLevel().sendParticles(new FeedParticleData(entity.getId(), (float) Math.PI * 2 * i / 3 + particleOffset, entity.getBbWidth() / 2),
                        position.x(), position.y() + entity.getBbHeight() / 2.0, position.z(), 0, 0.0, 0.0, 0.0, 1.0);
            }
        } else {
            return ProcResult.failure();
        }
        return ProcResult.success();
    }

    @Override
    public double getCost(SpellComponentInstance<SpellEffect> instance) {
        // Each half-drumstick is 0.5 vitae
        final double hungerCost = getHungerAmount(instance) * 0.5;
        // Each saturation half-drumstick is 2 vitae
        final double saturationCost = getSaturationAmount(instance) * 2.0;
        return hungerCost + saturationCost;
    }

    public void setHungerAmount(final SpellComponentInstance<?> instance, final int hungerAmount) {
        instance.getData().putInt(NBT_HUNGER_AMOUNT, hungerAmount);
    }

    public int getHungerAmount(final SpellComponentInstance<?> instance) {
        return instance.getData().getIntOr(NBT_HUNGER_AMOUNT, 0);
    }

    public void setSaturationAmount(final SpellComponentInstance<?> instance, final int saturationAmount) {
        instance.getData().putInt(NBT_SATURATION_AMOUNT, saturationAmount);
    }

    public int getSaturationAmount(final SpellComponentInstance<?> instance) {
        return instance.getData().getIntOr(NBT_SATURATION_AMOUNT, 0);
    }
}
