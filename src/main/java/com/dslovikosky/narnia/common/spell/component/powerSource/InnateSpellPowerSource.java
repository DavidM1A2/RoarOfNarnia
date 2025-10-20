package com.dslovikosky.narnia.common.spell.component.powerSource;

import com.dslovikosky.narnia.common.constants.Constants;
import com.dslovikosky.narnia.common.constants.ModAttachmentTypes;
import com.dslovikosky.narnia.common.event.SpellInnateHandler;
import com.dslovikosky.narnia.common.spell.Spell;
import com.dslovikosky.narnia.common.spell.component.powerSource.base.CastEnvironment;
import com.dslovikosky.narnia.common.spell.component.powerSource.base.SpellCastResult;
import com.dslovikosky.narnia.common.spell.component.powerSource.base.SpellPowerSource;
import com.dslovikosky.narnia.common.utils.MathUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public class InnateSpellPowerSource extends SpellPowerSource<Void> {
    public InnateSpellPowerSource() {
        super(Constants.modLocation("innate"));
    }

    @Override
    public SpellCastResult cast(Entity entity, Spell spell, CastEnvironment<Void> environment) {
        if (!(entity instanceof Player player)) {
            return SpellCastResult.failure(Component.translatable(getUnlocalizedBaseName() + ".not_enough_power"));
        }

        if (environment.getVitaeAvailable() < spell.getCost()) {
            return SpellCastResult.failure(Component.translatable(getUnlocalizedBaseName() + ".not_enough_power"));
        }

        player.setData(ModAttachmentTypes.INNATE_VITAE, environment.getVitaeAvailable() - spell.getCost());

        return SpellCastResult.success();
    }

    @Override
    public CastEnvironment<Void> computeCastEnvironment(Entity entity) {
        if (entity instanceof Player player) {
            final double innateVitae = player.getData(ModAttachmentTypes.INNATE_VITAE);
            return CastEnvironment.withVitae(innateVitae, SpellInnateHandler.getMaxVitae(entity.level()), null);
        }

        return CastEnvironment.noVitae(null);
    }

    @Override
    protected Number getSourceSpecificCost(double vitae) {
        return MathUtils.round(vitae, 1);
    }
}
