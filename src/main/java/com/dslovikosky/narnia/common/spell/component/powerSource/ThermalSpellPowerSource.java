package com.dslovikosky.narnia.common.spell.component.powerSource;

import com.dslovikosky.narnia.common.constants.Constants;
import com.dslovikosky.narnia.common.constants.ModAttachmentTypes;
import com.dslovikosky.narnia.common.event.SpellThermalHandler;
import com.dslovikosky.narnia.common.model.attachment_type.ThermalData;
import com.dslovikosky.narnia.common.spell.Spell;
import com.dslovikosky.narnia.common.spell.component.powerSource.base.CastEnvironment;
import com.dslovikosky.narnia.common.spell.component.powerSource.base.SpellCastResult;
import com.dslovikosky.narnia.common.spell.component.powerSource.base.SpellPowerSource;
import com.dslovikosky.narnia.common.utils.MathUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public class ThermalSpellPowerSource extends SpellPowerSource<Void> {
    public ThermalSpellPowerSource() {
        super(Constants.modLocation("thermal"));
    }

    @Override
    public SpellCastResult cast(Entity entity, Spell spell, CastEnvironment<Void> environment) {
        if (!(entity instanceof Player player)) {
            return SpellCastResult.failure(Component.translatable(getUnlocalizedBaseName() + ".not_enough_power"));
        }

        if (environment.getVitaeAvailable() < spell.getCost()) {
            return SpellCastResult.failure(Component.translatable(getUnlocalizedBaseName() + ".not_enough_power"));
        }

        final ThermalData thermalData = player.getData(ModAttachmentTypes.THERMAL_DATA);
        thermalData.setVitae(environment.getVitaeAvailable() - spell.getCost());
        player.setData(ModAttachmentTypes.THERMAL_DATA, thermalData);

        return SpellCastResult.success();
    }

    @Override
    public CastEnvironment<Void> computeCastEnvironment(Entity entity) {
        if (!(entity instanceof Player player)) {
            return CastEnvironment.noVitae(null);
        }

        final double thermalVitae = player.getData(ModAttachmentTypes.THERMAL_DATA).getVitae();
        return CastEnvironment.withVitae(thermalVitae, SpellThermalHandler.getMaxVitae(entity.level()), null);
    }

    @Override
    protected Number getSourceSpecificCost(double vitae) {
        return MathUtils.round(vitae, 1);
    }
}
