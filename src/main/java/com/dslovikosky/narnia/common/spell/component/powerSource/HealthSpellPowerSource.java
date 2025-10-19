package com.dslovikosky.narnia.common.spell.component.powerSource;

import com.dslovikosky.narnia.common.constants.Constants;
import com.dslovikosky.narnia.common.spell.Spell;
import com.dslovikosky.narnia.common.spell.component.powerSource.base.CastEnvironment;
import com.dslovikosky.narnia.common.spell.component.powerSource.base.SpellCastResult;
import com.dslovikosky.narnia.common.spell.component.powerSource.base.SpellPowerSource;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class HealthSpellPowerSource extends SpellPowerSource<Void> {
    private static final double VITAE_PER_HP = 5.0;

    public HealthSpellPowerSource() {
        super(Constants.modLocation("health"));
    }

    @Override
    public SpellCastResult cast(Entity entity, Spell spell, CastEnvironment<Void> environment) {
        if (entity instanceof LivingEntity livingEntity) {
            if (livingEntity.hurtTime > 0) {
                return SpellCastResult.failure(Component.translatable(getUnlocalizedBaseName() + ".was_just_hurt"));
            }

            if (environment.getVitaeAvailable() < spell.getCost()) {
                return SpellCastResult.failure(Component.translatable(getUnlocalizedBaseName() + ".not_enough_power"));
            }

            // Creative/Spectator players can't take dmg
            if (entity instanceof Player player && !(player.isCreative() && player.isSpectator())) {
                final double hpCost = spell.getCost() / VITAE_PER_HP;
                entity.hurt(player.damageSources().fellOutOfWorld(), (float) hpCost);
                return SpellCastResult.success();
            }
        }
        return SpellCastResult.failure(Component.translatable(getUnlocalizedBaseName() + ".not_enough_power"));
    }

    @Override
    public CastEnvironment<Void> computeCastEnvironment(Entity entity) {
        if (entity instanceof LivingEntity livingEntity) {
            // They can use 10 "deficit" hearts (i.e. they can kill themselves :P)
            final float absorptionAmount = livingEntity.getAbsorptionAmount();
            return CastEnvironment.withVitae(
                    (livingEntity.getHealth() + 20 + absorptionAmount) * VITAE_PER_HP,
                    (livingEntity.getMaxHealth() + 20 + absorptionAmount) * VITAE_PER_HP,
                    null
            );
        }

        return CastEnvironment.noVitae(null);
    }

    @Override
    protected Number getSourceSpecificCost(double vitae) {
        return Math.ceil(vitae / VITAE_PER_HP) / 2.0;
    }
}
