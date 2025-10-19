package com.dslovikosky.narnia.common.event.datagenproviders;

import com.dslovikosky.narnia.common.constants.Constants;
import com.dslovikosky.narnia.common.constants.ModParticleTypes;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.data.ParticleDescriptionProvider;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ModParticleDescriptionProvider extends ParticleDescriptionProvider {
    public ModParticleDescriptionProvider(PackOutput output) {
        super(output);
    }

    @Override
    protected void addDescriptions() {
        spriteSet(ModParticleTypes.ARROW_TRAIL);
        spriteSet(ModParticleTypes.CLEANSE);
        spriteSet(ModParticleTypes.DELAY.get(),
                Constants.modLocation("delay/delay1"),
                Constants.modLocation("delay/delay2"),
                Constants.modLocation("delay/delay3"),
                Constants.modLocation("delay/delay4"));
        spriteSet(ModParticleTypes.DIG);
        spriteSet(ModParticleTypes.DISINTEGRATE);
        spriteSet(ModParticleTypes.DUST_CLOUD.get(),
                Constants.modLocation("dust_cloud/blue"),
                Constants.modLocation("dust_cloud/brown"),
                Constants.modLocation("dust_cloud/green"),
                Constants.modLocation("dust_cloud/grey"));
        spriteSet(ModParticleTypes.ENDER);
        spriteSet(ModParticleTypes.EXPLOSION);
        spriteSet(ModParticleTypes.FEED);
        spriteSet(ModParticleTypes.FEY.get(),
                Constants.modLocation("fey/fey1"),
                Constants.modLocation("fey/fey2"),
                Constants.modLocation("fey/fey3"),
                Constants.modLocation("fey/fey4"));
        spriteSet(ModParticleTypes.FIRE);
        spriteSet(ModParticleTypes.FIZZLE.get(), IntStream.range(1, 41)
                .boxed()
                .map(integer -> Constants.modLocation("fizzle/fizzle" + integer))
                .collect(Collectors.toList()));
        spriteSet(ModParticleTypes.FLY);
        spriteSet(ModParticleTypes.FREEZE);
        spriteSet(ModParticleTypes.GROW);
        spriteSet(ModParticleTypes.HEAL);
        spriteSet(ModParticleTypes.IMBUE);
        spriteSet(ModParticleTypes.IMBUE_FIZZLE.get(), IntStream.range(1, 7)
                .boxed()
                .map(integer -> Constants.modLocation("imbue_fizzle/imbue_fizzle" + integer))
                .collect(Collectors.toList()));
        spriteSet(ModParticleTypes.LIGHTNING);
        spriteSet(ModParticleTypes.POISON);
        spriteSet(ModParticleTypes.PROJECTILE);
        spriteSet(ModParticleTypes.ROTATE);
        spriteSet(ModParticleTypes.SELF);
        spriteSet(ModParticleTypes.SELF_FIZZLE.get(), IntStream.range(1, 6)
                .boxed()
                .map(integer -> Constants.modLocation("self_fizzle/self_fizzle" + integer))
                .collect(Collectors.toList()));
        spriteSet(ModParticleTypes.SHIELD);
        spriteSet(ModParticleTypes.SMOKE_SCREEN);
        spriteSet(ModParticleTypes.SONIC_DISRUPTION);
        spriteSet(ModParticleTypes.SPELL_HIT);
        spriteSet(ModParticleTypes.SPELL_LASER);
        spriteSet(ModParticleTypes.STRENGTH);
        spriteSet(ModParticleTypes.VITAE_EXTRACTOR_BURN);
        spriteSet(ModParticleTypes.VITAE_EXTRACTOR_CHARGE);
        spriteSet(ModParticleTypes.WARD.get(),
                Constants.modLocation("ward/ward1"),
                Constants.modLocation("ward/ward2"),
                Constants.modLocation("ward/ward3"),
                Constants.modLocation("ward/ward4"));
        spriteSet(ModParticleTypes.WEAKNESS);
    }

    private void spriteSet(final DeferredHolder<ParticleType<?>, ? extends ParticleType<?>> particleType) {
        spriteSet(particleType.get(), ResourceLocation.parse(particleType.getRegisteredName()));
    }
}
