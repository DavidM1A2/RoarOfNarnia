package com.dslovikosky.narnia.common.spell.component.powerSource;

import com.dslovikosky.narnia.common.constants.Constants;
import com.dslovikosky.narnia.common.constants.ModItems;
import com.dslovikosky.narnia.common.spell.Spell;
import com.dslovikosky.narnia.common.spell.component.powerSource.base.CastEnvironment;
import com.dslovikosky.narnia.common.spell.component.powerSource.base.SpellCastResult;
import com.dslovikosky.narnia.common.spell.component.powerSource.base.SpellPowerSource;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class AlchemySpellPowerSource extends SpellPowerSource<Void> {
    private static final double VITAE_PER_DUST = 30.0;

    public AlchemySpellPowerSource() {
        super(Constants.modLocation("alchemy"));
    }

    @Override
    public SpellCastResult cast(Entity entity, Spell spell, CastEnvironment<Void> environment) {
        if (entity instanceof Player player) {
            if (environment.getVitaeAvailable() < spell.getCost()) {
                return SpellCastResult.failure(Component.translatable(getUnlocalizedBaseName() + ".not_enough_power"));
            }

            double costRemaining = spell.getCost();
            for (final ItemStack itemStack : player.getInventory()) {
                if (itemStack.is(ModItems.SPARKLING_DUST.get())) {
                    while (costRemaining > 0.0 && itemStack.getCount() > 0) {
                        costRemaining = costRemaining - VITAE_PER_DUST;
                        itemStack.setCount(itemStack.getCount() - 1);
                    }
                }
                if (costRemaining <= 0.0) {
                    break;
                }
            }
            return SpellCastResult.success();
        } else {
            return SpellCastResult.failure(Component.translatable(getUnlocalizedBaseName() + ".not_enough_power"));
        }
    }

    @Override
    public CastEnvironment<Void> computeCastEnvironment(Entity entity) {
        if (entity instanceof Player player) {
            double vitaeAvailable = 0.0;
            for (final ItemStack itemStack : player.getInventory()) {
                if (itemStack.is(ModItems.SPARKLING_DUST.get())) {
                    vitaeAvailable = vitaeAvailable + itemStack.getCount() * VITAE_PER_DUST;
                }
            }

            return CastEnvironment.withVitae(vitaeAvailable, player.getInventory().getContainerSize() * 64 * VITAE_PER_DUST, null);
        }

        return CastEnvironment.noVitae(null);
    }

    @Override
    protected Number getSourceSpecificCost(double vitae) {
        return Math.ceil(vitae / VITAE_PER_DUST);
    }
}
