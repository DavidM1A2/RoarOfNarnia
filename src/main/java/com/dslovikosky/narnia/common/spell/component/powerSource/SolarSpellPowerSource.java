package com.dslovikosky.narnia.common.spell.component.powerSource;

import com.dslovikosky.narnia.common.constants.Constants;
import com.dslovikosky.narnia.common.constants.ModAttachmentTypes;
import com.dslovikosky.narnia.common.event.SpellSolarHandler;
import com.dslovikosky.narnia.common.spell.Spell;
import com.dslovikosky.narnia.common.spell.component.powerSource.base.CastEnvironment;
import com.dslovikosky.narnia.common.spell.component.powerSource.base.SpellCastResult;
import com.dslovikosky.narnia.common.spell.component.powerSource.base.SpellPowerSource;
import com.dslovikosky.narnia.common.utils.MathUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public class SolarSpellPowerSource extends SpellPowerSource<Void> {
    public SolarSpellPowerSource() {
        super(Constants.modLocation("solar"));
    }

    @Override
    public SpellCastResult cast(Entity entity, Spell spell, CastEnvironment<Void> environment) {
        if (!(entity instanceof Player player)) {
            return SpellCastResult.failure(Component.translatable(getUnlocalizedBaseName() + ".not_enough_power"));
        }

        if (environment.getVitaeAvailable() < spell.getCost()) {
            return SpellCastResult.failure(Component.translatable(getUnlocalizedBaseName() + ".not_enough_power"));
        }

        player.setData(ModAttachmentTypes.SOLAR_VITAE, environment.getVitaeAvailable() - spell.getCost());

        return SpellCastResult.success();
    }

    @Override
    public CastEnvironment<Void> computeCastEnvironment(Entity entity) {
        if (!(entity instanceof Player player)) {
            return CastEnvironment.noVitae(null);
        }

        final double solarVitae = player.getData(ModAttachmentTypes.SOLAR_VITAE);
        final boolean canSeeSky = entity.level().canSeeSky(entity.blockPosition());
        if (!canSeeSky) {
            return CastEnvironment.withVitae(0.0, SpellSolarHandler.getMaxVitae(entity.level()), null);
        }

        return CastEnvironment.withVitae(solarVitae, SpellSolarHandler.getMaxVitae(entity.level()), null);
    }

    @Override
    protected Number getSourceSpecificCost(double vitae) {
        return MathUtils.round(vitae, 1);
    }
}
