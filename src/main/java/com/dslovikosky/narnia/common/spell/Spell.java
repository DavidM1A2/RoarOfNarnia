package com.dslovikosky.narnia.common.spell;

import com.dslovikosky.narnia.common.constants.ModDimensions;
import com.dslovikosky.narnia.common.constants.ModSoundEvents;
import com.dslovikosky.narnia.common.constants.ModSpellPowerSources;
import com.dslovikosky.narnia.common.event.custom.CastSpellEvent;
import com.dslovikosky.narnia.common.spell.component.DeliveryTransitionState;
import com.dslovikosky.narnia.common.spell.component.SpellComponentInstance;
import com.dslovikosky.narnia.common.spell.component.deliveryMethod.base.SpellDeliveryMethod;
import com.dslovikosky.narnia.common.spell.component.effect.base.SpellEffect;
import com.dslovikosky.narnia.common.spell.component.powerSource.base.SpellCastResult;
import com.dslovikosky.narnia.common.spell.component.powerSource.base.SpellPowerSource;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.NeoForge;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Spell {
    private static final String NBT_NAME = "name";
    private static final String NBT_SPELL_STAGES = "spell_stages";

    private final List<SpellStage> spellStages = new ArrayList<>();
    private String name = null;

    public Spell() {
    }

    public Spell(final CompoundTag nbt) {
        // Read each field from NBT
        name = nbt.getString(NBT_NAME).orElse(null);

        // Read each spell stage from NBT
        final ListTag spellStagesNBT = nbt.getList(NBT_SPELL_STAGES).get();
        for (int i = 0; i < spellStagesNBT.size(); i++) {
            // Grab the spell stage NBT, read it into the spell stage, and add it
            final CompoundTag spellStageNBT = spellStagesNBT.getCompound(i).get();
            final SpellStage spellStage = new SpellStage(spellStageNBT);
            spellStages.add(spellStage);
        }
    }

    public CompoundTag serializeNbt() {
        final CompoundTag nbt = new CompoundTag();

        // Write each field to NBT
        nbt.putString(NBT_NAME, name);

        // Write each spell stage to NBT
        final ListTag spellStagesNBT = new ListTag();
        spellStages.forEach(spellStage -> spellStagesNBT.add(spellStage.serializeNbt()));
        nbt.put(NBT_SPELL_STAGES, spellStagesNBT);

        return nbt;
    }

    public void attemptToCast(final Entity entity, final Vec3 direction, final boolean isSpellScroll) {
        if (!entity.level().isClientSide) {
            if (entity.level().dimension() != ModDimensions.WOOD_BETWEEN_THE_WORLDS) {
                if (isValid()) {
                    final SpellPowerSource<?> selectedPowerSource;
                    if (entity instanceof Player && !isSpellScroll) {
                        selectedPowerSource = ModSpellPowerSources.CREATIVE.get();
                    } else {
                        selectedPowerSource = ModSpellPowerSources.SPELL_SCROLL.get();
                    }

                    final SpellCastResult castResult = selectedPowerSource.cast(entity, this);
                    if (!castResult.wasSuccessful()) {
                        if (entity instanceof Player player) {
                            player.displayClientMessage(castResult.getFailureMessage(), false);
                        }
                        return;
                    }

                    final CastSpellEvent castSpellEvent = new CastSpellEvent(entity, this, selectedPowerSource);
                    NeoForge.EVENT_BUS.post(castSpellEvent);
                    if (castSpellEvent.isCanceled()) {
                        return;
                    }

                    // Play a cast sound
                    entity.level().playSound(null, entity.blockPosition(), ModSoundEvents.SPELL_CAST.get(), SoundSource.PLAYERS, 1.0f, 0.8f + (float) Math.random() * 0.4f);

                    final Vec3 position = entity.getEyePosition(1.0f);
                    // Tell the first delivery method to fire
                    getStage(0)
                            .getDeliveryInstance()
                            .getComponent()
                            .execute(new DeliveryTransitionState(this, 0, entity.level(), position,
                                    new BlockPos((int) position.x, (int) position.y, (int) position.z), direction, entity.getUpVector(1f), entity, entity, null));
                } else {
                    if (entity instanceof Player player) {
                        player.displayClientMessage(Component.translatable("message.narnia.spell.invalid"), false);
                    }
                }
            } else {
                if (entity instanceof Player player) {
                    player.displayClientMessage(Component.translatable("message.narnia.spell.wrong_dimension"), false);
                }
            }
        }
    }

    public boolean isValid() {
        return !spellStages.isEmpty() && spellStages.stream().allMatch(SpellStage::isValid);
    }

    public double getCost() {
        double cost = 0.0;

        double currentDeliveryMultiplicity = 1.0;
        // Go over each spell stage and add up costs. The last stage has no
        for (final SpellStage spellStage : spellStages) {
            // Add the cost of the stage
            cost = cost + spellStage.getCost() * currentDeliveryMultiplicity;
            final SpellComponentInstance<SpellDeliveryMethod> deliveryInstance = spellStage.getDeliveryInstance();
            // Each stage after will cost "Multiplicity" more since it gets proc'd "Multiplicity" times
            currentDeliveryMultiplicity = currentDeliveryMultiplicity * deliveryInstance.getComponent().getMultiplicity(deliveryInstance);
        }

        // If cost overflowed then set it to max double
        if (cost < 0) {
            cost = Double.MAX_VALUE;
        }

        return cost;
    }

    public boolean hasStage(final int index) {
        return index >= 0 && index < spellStages.size();
    }

    public SpellStage getStage(final int stageIndex) {
        if (hasStage(stageIndex)) {
            return spellStages.get(stageIndex);
        } else {
            return null;
        }
    }

    public boolean hasDeliveryMethod(final SpellDeliveryMethod deliveryMethod) {
        return spellStages.stream().anyMatch(it -> it.getDeliveryInstance() != null && it.getDeliveryInstance().getComponent() == deliveryMethod);
    }

    public boolean hasEffect(final SpellEffect spellEffect) {
        return spellStages.stream().anyMatch(it -> Arrays.stream(it.getEffects()).anyMatch(effect -> effect.getComponent() == spellEffect));
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<SpellStage> getSpellStages() {
        return spellStages;
    }
}
