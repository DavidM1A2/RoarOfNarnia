package com.dslovikosky.narnia.common.constants;

import com.dslovikosky.narnia.common.spell.component.deliveryMethod.AOESpellDeliveryMethod;
import com.dslovikosky.narnia.common.spell.component.deliveryMethod.ChainSpellDeliveryMethod;
import com.dslovikosky.narnia.common.spell.component.deliveryMethod.SelfSpellDeliveryMethod;
import com.dslovikosky.narnia.common.spell.component.deliveryMethod.base.SpellDeliveryMethod;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModSpellDeliveryMethods {
    public static final DeferredRegister<SpellDeliveryMethod> SPELL_DELIVERY_METHODS = DeferredRegister.create(ModRegistries.SPELL_DELIVERY_METHODS, Constants.MOD_ID);

    public static final DeferredHolder<SpellDeliveryMethod, SelfSpellDeliveryMethod> SELF = SPELL_DELIVERY_METHODS.register("self", SelfSpellDeliveryMethod::new);
    public static final DeferredHolder<SpellDeliveryMethod, AOESpellDeliveryMethod> AOE = SPELL_DELIVERY_METHODS.register("aoe", AOESpellDeliveryMethod::new);
    public static final DeferredHolder<SpellDeliveryMethod, ChainSpellDeliveryMethod> CHAIN = SPELL_DELIVERY_METHODS.register("chain", ChainSpellDeliveryMethod::new);
}
