package com.dslovikosky.narnia.common.constants;

import com.dslovikosky.narnia.common.mob_effect.DrowsyMobEffect;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModMobEffects {
    public static final DeferredRegister<MobEffect> MOB_EFFECTS = DeferredRegister.create(Registries.MOB_EFFECT, Constants.MOD_ID);

    public static final DeferredHolder<MobEffect, DrowsyMobEffect> DROWSY = MOB_EFFECTS.register("drowsy", DrowsyMobEffect::new);
}
