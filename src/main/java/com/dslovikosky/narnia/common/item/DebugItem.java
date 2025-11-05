package com.dslovikosky.narnia.common.item;

import com.dslovikosky.narnia.common.constants.Constants;
import com.dslovikosky.narnia.common.constants.ModSpellDeliveryMethods;
import com.dslovikosky.narnia.common.constants.ModSpellEffects;
import com.dslovikosky.narnia.common.spell.Spell;
import com.dslovikosky.narnia.common.spell.SpellStage;
import com.dslovikosky.narnia.common.spell.component.deliveryMethod.SpellDeliveryMethodInstance;
import com.dslovikosky.narnia.common.spell.component.effect.base.SpellEffectInstance;
import com.mojang.logging.LogUtils;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import org.slf4j.Logger;

public class DebugItem extends Item {
    private static final Logger LOG = LogUtils.getLogger();

    public DebugItem() {
        super(new Properties().stacksTo(1)
                .setId(ResourceKey.create(Registries.ITEM, Constants.modLocation("debug"))));
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        return super.useOn(context);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        final SpellEffectInstance effect = new SpellEffectInstance(ModSpellEffects.DIG.get());
        effect.setDefaults();

        final SpellDeliveryMethodInstance deliveryMethod1 = new SpellDeliveryMethodInstance(ModSpellDeliveryMethods.PROJECTILE.get());
        deliveryMethod1.setDefaults();
        ModSpellDeliveryMethods.PROJECTILE.get().setSpeed(deliveryMethod1, 5);
        ModSpellDeliveryMethods.PROJECTILE.get().setRange(deliveryMethod1, 5);
        final SpellStage spellStage1 = new SpellStage();
        spellStage1.setDeliveryInstance(deliveryMethod1);

        final SpellDeliveryMethodInstance deliveryMethod2 = new SpellDeliveryMethodInstance(ModSpellDeliveryMethods.CONE.get());
        deliveryMethod2.setDefaults();
        ModSpellDeliveryMethods.CONE.get().setRadius(deliveryMethod2, 4);
        ModSpellDeliveryMethods.CONE.get().setLength(deliveryMethod2, 15);
        final SpellStage spellStage2 = new SpellStage();
        spellStage2.setDeliveryInstance(deliveryMethod2);
        spellStage2.getEffects()[0] = effect;

//        final SpellDeliveryMethodInstance deliveryMethod3 = new SpellDeliveryMethodInstance(ModSpellDeliveryMethods.PROJECTILE.get());
//        deliveryMethod3.setDefaults();
//        ModSpellDeliveryMethods.PROJECTILE.get().setSpeed(deliveryMethod3, 8);
//        final SpellStage spellStage3 = new SpellStage();
//        spellStage3.setDeliveryInstance(deliveryMethod3);
//
//        final SpellDeliveryMethodInstance deliveryMethod4 = new SpellDeliveryMethodInstance(ModSpellDeliveryMethods.AOE.get());
//        deliveryMethod4.setDefaults();
//        final SpellStage spellStage4 = new SpellStage();
//        spellStage4.setDeliveryInstance(deliveryMethod4);
//        spellStage4.getEffects()[0] = effect;

        final Spell spell = new Spell();
        spell.setName("Test");
        spell.getSpellStages().add(spellStage1);
        spell.getSpellStages().add(spellStage2);
//        spell.getSpellStages().add(spellStage3);
//        spell.getSpellStages().add(spellStage4);

        spell.attemptToCast(player, player.getLookAngle(), false);

        return super.use(level, player, hand);
    }
}
