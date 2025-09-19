package com.dslovikosky.narnia.common.spell.component.powerSource;

import com.dslovikosky.narnia.common.constants.Constants;
import com.dslovikosky.narnia.common.spell.Spell;
import com.dslovikosky.narnia.common.spell.component.powerSource.base.CastEnvironment;
import com.dslovikosky.narnia.common.spell.component.powerSource.base.SpellCastResult;
import com.dslovikosky.narnia.common.spell.component.powerSource.base.SpellPowerSource;
import com.dslovikosky.narnia.common.utils.MathUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public class CreativeSpellPowerSource extends SpellPowerSource<Void> {
    public CreativeSpellPowerSource() {
        super(Constants.modLocation("creative"));
    }

    @Override
    public SpellCastResult cast(final Entity entity, final Spell spell, final CastEnvironment<Void> environment) {
        if (environment.getVitaeAvailable() >= spell.getCost()) {
            return SpellCastResult.success();
        } else {
            return SpellCastResult.failure(Component.translatable(getUnlocalizedBaseName() + ".not_enough_power"));
        }
    }

    @Override
    public CastEnvironment<Void> computeCastEnvironment(final Entity entity) {
        if (entity instanceof Player player && player.isCreative()) {
            return CastEnvironment.infiniteVitae(null);
        } else {
            return CastEnvironment.noVitae(null);
        }
    }

    @Override
    protected Double getSourceSpecificCost(final double vitae) {
        return MathUtils.round(vitae, 1);
    }
}
