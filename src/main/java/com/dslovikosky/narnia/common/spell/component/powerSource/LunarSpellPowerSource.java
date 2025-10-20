package com.dslovikosky.narnia.common.spell.component.powerSource;

import com.dslovikosky.narnia.common.constants.Constants;
import com.dslovikosky.narnia.common.constants.ModAttachmentTypes;
import com.dslovikosky.narnia.common.event.SpellLunarHandler;
import com.dslovikosky.narnia.common.spell.Spell;
import com.dslovikosky.narnia.common.spell.component.powerSource.base.CastEnvironment;
import com.dslovikosky.narnia.common.spell.component.powerSource.base.SpellCastResult;
import com.dslovikosky.narnia.common.spell.component.powerSource.base.SpellPowerSource;
import com.dslovikosky.narnia.common.utils.MathUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public class LunarSpellPowerSource extends SpellPowerSource<Void> {
    public LunarSpellPowerSource() {
        super(Constants.modLocation("lunar"));
    }

    @Override
    public SpellCastResult cast(Entity entity, Spell spell, CastEnvironment<Void> environment) {
        if (!(entity instanceof Player player)) {
            return SpellCastResult.failure(Component.translatable(getUnlocalizedBaseName() + ".not_enough_power"));
        }

        if (environment.getVitaeAvailable() < spell.getCost()) {
            return SpellCastResult.failure(Component.translatable(getUnlocalizedBaseName() + ".not_enough_power"));
        }

        player.setData(ModAttachmentTypes.LUNAR_VITAE, environment.getVitaeAvailable() - spell.getCost());

        return SpellCastResult.success();
    }

    @Override
    public CastEnvironment<Void> computeCastEnvironment(Entity entity) {
        if (!(entity instanceof Player player)) {
            return CastEnvironment.noVitae(null);
        }

        final double lunarVitae = player.getData(ModAttachmentTypes.LUNAR_VITAE);
        final boolean canSeeSky = entity.level().canSeeSky(entity.blockPosition());
        if (!canSeeSky) {
            return CastEnvironment.withVitae(0.0, SpellLunarHandler.getMaxVitae(entity.level()), null);
        }

        return CastEnvironment.withVitae(lunarVitae, SpellLunarHandler.getMaxVitae(entity.level()), null);
    }

    @Override
    protected Number getSourceSpecificCost(double vitae) {
        return MathUtils.round(vitae, 1);
    }
}
