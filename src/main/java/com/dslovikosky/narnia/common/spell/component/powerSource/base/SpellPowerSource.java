package com.dslovikosky.narnia.common.spell.component.powerSource.base;

import com.dslovikosky.narnia.common.spell.Spell;
import com.dslovikosky.narnia.common.spell.component.SpellComponentBase;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public abstract class SpellPowerSource<T> extends SpellComponentBase {
    public SpellPowerSource(final ResourceLocation id) {
        super(id, ResourceLocation.fromNamespaceAndPath(id.getNamespace(), "textures/gui/spell_component/power_sources/" + id.getPath() + ".png"));
    }

    public SpellCastResult cast(final Entity entity, final Spell spell) {
        return cast(entity, spell, computeCastEnvironment(entity));
    }

    public abstract SpellCastResult cast(final Entity entity, final Spell spell, final CastEnvironment<T> environment);

    public abstract CastEnvironment<T> computeCastEnvironment(final Entity entity);

    protected abstract Number getSourceSpecificCost(final double vitae);

    public Component getCostOverview() {
        return Component.translatable(getUnlocalizedBaseName() + ".cost_overview");
    }

    public Component getFormattedCost(final double rawCost) {
        return Component.translatable(getUnlocalizedBaseName() + ".formatted_cost", getSourceSpecificCost(rawCost));
    }

    public String getUnlocalizedBaseName() {
        return "power_source." + getId().getNamespace() + "." + getId().getPath();
    }
}
