package com.dslovikosky.narnia.common.spell.component.powerSource;

import com.dslovikosky.narnia.common.constants.Constants;
import com.dslovikosky.narnia.common.spell.Spell;
import com.dslovikosky.narnia.common.spell.component.powerSource.base.CastEnvironment;
import com.dslovikosky.narnia.common.spell.component.powerSource.base.SpellCastResult;
import com.dslovikosky.narnia.common.spell.component.powerSource.base.SpellPowerSource;
import com.dslovikosky.narnia.common.utils.MathUtils;
import net.minecraft.world.entity.Entity;

public class SpellScrollSpellPowerSource extends SpellPowerSource<Void> {
    public SpellScrollSpellPowerSource() {
        super(Constants.modLocation("spell_scroll"));
    }

    @Override
    public SpellCastResult cast(final Entity entity, final Spell spell, final CastEnvironment<Void> environment) {
        return SpellCastResult.success();
    }

    @Override
    public CastEnvironment<Void> computeCastEnvironment(final Entity entity) {
        return CastEnvironment.infiniteVitae(null);
    }

    @Override
    protected Double getSourceSpecificCost(final double vitae) {
        return MathUtils.round(vitae, 1);
    }
}
