package com.dslovikosky.narnia.common.spell.component.deliveryMethod.base;

import com.dslovikosky.narnia.common.spell.Spell;
import com.dslovikosky.narnia.common.spell.component.DeliveryTransitionState;
import com.dslovikosky.narnia.common.spell.component.SpellComponent;
import com.dslovikosky.narnia.common.spell.component.SpellComponentInstance;
import com.dslovikosky.narnia.common.spell.component.effect.base.ProcResult;
import com.dslovikosky.narnia.common.spell.component.effect.base.SpellEffect;
import net.minecraft.resources.ResourceLocation;

public abstract class SpellDeliveryMethod extends SpellComponent<SpellDeliveryMethod> {
    public SpellDeliveryMethod(final ResourceLocation id) {
        super(id, ResourceLocation.fromNamespaceAndPath(id.getNamespace(), "textures/gui/spell_component/delivery_methods/" + id.getPath() + ".png"));
    }

    public abstract void execute(final DeliveryTransitionState state);

    public ProcResult procEffects(final DeliveryTransitionState state) {
        return procEffects(state, true);
    }

    public ProcResult procEffects(final DeliveryTransitionState state, final boolean fizzleOnFailure) {
        boolean hasOneEffect = false;
        boolean oneEffectProcdSuccessfully = false;
        for (final SpellComponentInstance<SpellEffect> effect : state.getCurrentStage().getEffects()) {
            if (effect != null) {
                hasOneEffect = true;
                final ProcResult result = effect.getComponent().proc(state, effect);
                if (result.isSuccess()) {
                    oneEffectProcdSuccessfully = true;
                }
            }
        }

        final boolean isSuccess = !hasOneEffect || oneEffectProcdSuccessfully;
        if (fizzleOnFailure) {
            // particles
        }

        return new ProcResult(isSuccess);
    }

    public void transitionFrom(final DeliveryTransitionState state) {
        final Spell spell = state.getSpell();
        final int nextStageIndex = state.getStageIndex() + 1;
        if (spell.hasStage(nextStageIndex)) {
            spell.getStage(nextStageIndex).getDeliveryInstance().getComponent().execute(state.copy(nextStageIndex, null));
        }
    }

    public abstract double getDeliveryCost(final SpellComponentInstance<SpellDeliveryMethod> instance);

    public abstract double getMultiplicity(final SpellComponentInstance<SpellDeliveryMethod> instance);

    @Override
    protected String getUnlocalizedBaseName() {
        return "delivery_method." + getId().getNamespace() + "." + getId().getPath();
    }
}
