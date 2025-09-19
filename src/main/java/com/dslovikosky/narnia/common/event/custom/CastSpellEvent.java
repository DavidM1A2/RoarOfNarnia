package com.dslovikosky.narnia.common.event.custom;

import com.dslovikosky.narnia.common.spell.Spell;
import com.dslovikosky.narnia.common.spell.component.powerSource.base.SpellPowerSource;
import net.minecraft.world.entity.Entity;
import net.neoforged.bus.api.ICancellableEvent;
import net.neoforged.neoforge.event.entity.EntityEvent;

public class CastSpellEvent extends EntityEvent implements ICancellableEvent {
    private final Spell spell;
    private final SpellPowerSource<?> powerSourceUsed;

    public CastSpellEvent(final Entity entity, final Spell spell, final SpellPowerSource<?> powerSourceUsed) {
        super(entity);
        this.spell = spell;
        this.powerSourceUsed = powerSourceUsed;
    }

    public Spell getSpell() {
        return spell;
    }

    public SpellPowerSource<?> getPowerSourceUsed() {
        return powerSourceUsed;
    }
}
