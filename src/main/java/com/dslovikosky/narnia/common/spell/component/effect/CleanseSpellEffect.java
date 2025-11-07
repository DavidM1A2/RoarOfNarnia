package com.dslovikosky.narnia.common.spell.component.effect;

import com.dslovikosky.narnia.common.constants.Constants;
import com.dslovikosky.narnia.common.constants.ModAttachmentTypes;
import com.dslovikosky.narnia.common.model.attachment_type.SpellCharmData;
import com.dslovikosky.narnia.common.particle.CleanseParticleData;
import com.dslovikosky.narnia.common.spell.component.DeliveryTransitionState;
import com.dslovikosky.narnia.common.spell.component.SpellComponentInstance;
import com.dslovikosky.narnia.common.spell.component.effect.base.ProcResult;
import com.dslovikosky.narnia.common.spell.component.effect.base.SpellEffect;
import com.dslovikosky.narnia.common.spell.component.property.SpellComponentPropertyFactory;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class CleanseSpellEffect extends SpellEffect {
    private static final String NBT_BENEFICIAL_POTION_EFFECTS = "beneficial_potion_effects";
    private static final String NBT_NEUTRAL_POTION_EFFECTS = "neutral_potion_effects";
    private static final String NBT_HARMFUL_POTION_EFFECTS = "harmful_potion_effects";
    private static final String NBT_SPELL_EFFECTS = "spell_effects";

    public CleanseSpellEffect() {
        super(Constants.modLocation("cleanse"));
        addEditableProperty(
                SpellComponentPropertyFactory.booleanProperty()
                        .withBaseName(getUnlocalizedPropertyBaseName("beneficial_potion_effects"))
                        .withDefaultValue(true)
                        .withSetter(this::setBeneficialPotionEffects)
                        .withGetter(this::getBeneficialPotionEffects)
                        .build()
        );
        addEditableProperty(
                SpellComponentPropertyFactory.booleanProperty()
                        .withBaseName(getUnlocalizedPropertyBaseName("neutral_potion_effects"))
                        .withDefaultValue(true)
                        .withSetter(this::setNeutralPotionEffects)
                        .withGetter(this::getNeutralPotionEffects)
                        .build()
        );
        addEditableProperty(
                SpellComponentPropertyFactory.booleanProperty()
                        .withBaseName(getUnlocalizedPropertyBaseName("harmful_potion_effects"))
                        .withDefaultValue(true)
                        .withSetter(this::setHarmfulPotionEffects)
                        .withGetter(this::getHarmfulPotionEffects)
                        .build()
        );
        addEditableProperty(
                SpellComponentPropertyFactory.booleanProperty()
                        .withBaseName(getUnlocalizedPropertyBaseName("spell_effects"))
                        .withDefaultValue(true)
                        .withSetter(this::setSpellEffects)
                        .withGetter(this::getSpellEffects)
                        .build()
        );
    }

    @Override
    public ProcResult proc(DeliveryTransitionState state, SpellComponentInstance<SpellEffect> instance) {
        final Entity entity = state.getEntity();
        if (entity != null) {
            // Clear potion effects
            if (entity instanceof LivingEntity livingEntity) {
                final Set<MobEffectCategory> effectTypesToClear = new HashSet<>();
                if (getBeneficialPotionEffects(instance)) {
                    effectTypesToClear.add(MobEffectCategory.BENEFICIAL);
                }
                if (getNeutralPotionEffects(instance)) {
                    effectTypesToClear.add(MobEffectCategory.NEUTRAL);
                }
                if (getHarmfulPotionEffects(instance)) {
                    effectTypesToClear.add(MobEffectCategory.HARMFUL);
                }

                final Set<Holder<MobEffect>> effectsToRemove = livingEntity.getActiveEffects()
                        .stream()
                        .map(MobEffectInstance::getEffect)
                        .filter(effect -> effectTypesToClear.contains(effect.value().getCategory()))
                        .collect(Collectors.toSet());

                effectsToRemove.forEach(livingEntity::removeEffect);
            }

            // Unfreeze and uncharm the player
            if (entity instanceof Player player && getSpellEffects(instance)) {
                player.setData(ModAttachmentTypes.SPELL_CHARM_DATA, new SpellCharmData(0, UUID.randomUUID()));
            }

            // Show cleanse particles spinning around the hit entity
            final float startOffset = RANDOM.nextFloat() * 360;
            final float radius = Math.max(0.1f, entity.getBbWidth() / 2 * 1.5f);

            final ServerLevel level = state.getLevel();
            level.sendParticles(new CleanseParticleData(entity.getId(), startOffset + 0f, radius), entity.getX(), entity.getY(), entity.getZ(), 0, 0f, 0f, 0f, 0f);
            level.sendParticles(new CleanseParticleData(entity.getId(), startOffset + 90f, radius), entity.getX(), entity.getY(), entity.getZ(), 0, 0f, 0f, 0f, 0f);
            level.sendParticles(new CleanseParticleData(entity.getId(), startOffset + 180f, radius), entity.getX(), entity.getY(), entity.getZ(), 0, 0f, 0f, 0f, 0f);
            level.sendParticles(new CleanseParticleData(entity.getId(), startOffset + 270f, radius), entity.getX(), entity.getY(), entity.getZ(), 0, 0f, 0f, 0f, 0f);
        } else {
            return ProcResult.failure();
        }
        return ProcResult.success();
    }

    @Override
    public double getCost(SpellComponentInstance<SpellEffect> instance) {
        final double beneficialEffectCost = getBeneficialPotionEffects(instance) ? 5.0 : 0.0;
        final double neutralEffectCost = getNeutralPotionEffects(instance) ? 5.0 : 0.0;
        final double harmfulEffectCost = getHarmfulPotionEffects(instance) ? 5.0 : 0.0;
        final double spellEffectCost = getSpellEffects(instance) ? 10.0 : 0.0;
        return spellEffectCost + beneficialEffectCost + neutralEffectCost + harmfulEffectCost;
    }

    public void setBeneficialPotionEffects(final SpellComponentInstance<?> instance, final boolean value) {
        instance.getData().putBoolean(NBT_BENEFICIAL_POTION_EFFECTS, value);
    }

    public boolean getBeneficialPotionEffects(final SpellComponentInstance<?> instance) {
        return instance.getData().getBooleanOr(NBT_BENEFICIAL_POTION_EFFECTS, false);
    }

    public void setNeutralPotionEffects(final SpellComponentInstance<?> instance, final boolean value) {
        instance.getData().putBoolean(NBT_NEUTRAL_POTION_EFFECTS, value);
    }

    public boolean getNeutralPotionEffects(final SpellComponentInstance<?> instance) {
        return instance.getData().getBooleanOr(NBT_NEUTRAL_POTION_EFFECTS, false);
    }

    public void setHarmfulPotionEffects(final SpellComponentInstance<?> instance, final boolean value) {
        instance.getData().putBoolean(NBT_HARMFUL_POTION_EFFECTS, value);
    }

    public boolean getHarmfulPotionEffects(final SpellComponentInstance<?> instance) {
        return instance.getData().getBooleanOr(NBT_HARMFUL_POTION_EFFECTS, false);
    }

    public void setSpellEffects(final SpellComponentInstance<?> instance, final boolean value) {
        instance.getData().putBoolean(NBT_SPELL_EFFECTS, value);
    }

    public boolean getSpellEffects(final SpellComponentInstance<?> instance) {
        return instance.getData().getBooleanOr(NBT_SPELL_EFFECTS, false);
    }
}
