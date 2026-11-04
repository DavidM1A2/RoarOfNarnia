package com.dslovikosky.narnia.common.spell.component.effect.base;

import com.dslovikosky.narnia.common.spell.component.DeliveryTransitionState;
import com.dslovikosky.narnia.common.spell.component.SpellComponent;
import com.dslovikosky.narnia.common.spell.component.SpellComponentInstance;
import net.minecraft.resources.Identifier;

public abstract class SpellEffect extends SpellComponent<SpellEffect> {
    public SpellEffect(final Identifier id) {
        super(id, Identifier.fromNamespaceAndPath(id.getNamespace(), "textures/gui/spell_component/effects/" + id.getPath() + ".png"));
    }

    public abstract ProcResult proc(final DeliveryTransitionState state, final SpellComponentInstance<SpellEffect> instance);

    public abstract double getCost(final SpellComponentInstance<SpellEffect> instance);

    @Override
    protected String getUnlocalizedBaseName() {
        return "effect." + getId().getNamespace() + "." + getId().getPath();
    }
}
