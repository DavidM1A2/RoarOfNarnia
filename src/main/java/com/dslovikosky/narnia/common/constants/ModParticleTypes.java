package com.dslovikosky.narnia.common.constants;

import com.dslovikosky.narnia.common.particle.ArrowTrailParticleType;
import com.dslovikosky.narnia.common.particle.CleanseParticleType;
import com.dslovikosky.narnia.common.particle.FeedParticleType;
import com.dslovikosky.narnia.common.particle.FeyParticleType;
import com.dslovikosky.narnia.common.particle.FlyParticleType;
import com.dslovikosky.narnia.common.particle.HealParticleType;
import com.dslovikosky.narnia.common.particle.ProjectileParticleType;
import com.dslovikosky.narnia.common.particle.SelfParticleType;
import com.dslovikosky.narnia.common.particle.ShieldParticleType;
import com.dslovikosky.narnia.common.particle.WardParticleType;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModParticleTypes {
    public static final DeferredRegister<ParticleType<?>> PARTICLES = DeferredRegister.create(Registries.PARTICLE_TYPE, Constants.MOD_ID);

    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> SMOKE_SCREEN = PARTICLES.register("smoke_screen", () -> new SimpleParticleType(true));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> SPELL_HIT = PARTICLES.register("spell_hit", () -> new SimpleParticleType(false));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> SPELL_LASER = PARTICLES.register("spell_laser", () -> new SimpleParticleType(false));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> FREEZE = PARTICLES.register("freeze", () -> new SimpleParticleType(false));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> DIG = PARTICLES.register("dig", () -> new SimpleParticleType(false));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> ENDER = PARTICLES.register("ender", () -> new SimpleParticleType(false));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> EXPLOSION = PARTICLES.register("explosion", () -> new SimpleParticleType(false));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> FIRE = PARTICLES.register("fire", () -> new SimpleParticleType(false));
    public static final DeferredHolder<ParticleType<?>, FlyParticleType> FLY = PARTICLES.register("fly", FlyParticleType::new);
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> GROW = PARTICLES.register("grow", () -> new SimpleParticleType(false));
    public static final DeferredHolder<ParticleType<?>, HealParticleType> HEAL = PARTICLES.register("heal", HealParticleType::new);
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> POISON = PARTICLES.register("poison", () -> new SimpleParticleType(false));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> STRENGTH = PARTICLES.register("strength", () -> new SimpleParticleType(false));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> WEAKNESS = PARTICLES.register("weakness", () -> new SimpleParticleType(false));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> VITAE_EXTRACTOR_BURN = PARTICLES.register("vitae_extractor_burn", () -> new SimpleParticleType(false));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> VITAE_EXTRACTOR_CHARGE = PARTICLES.register("vitae_extractor_charge", () -> new SimpleParticleType(false));
    public static final DeferredHolder<ParticleType<?>, WardParticleType> WARD = PARTICLES.register("ward", WardParticleType::new);
    public static final DeferredHolder<ParticleType<?>, CleanseParticleType> CLEANSE = PARTICLES.register("cleanse", CleanseParticleType::new);
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> DISINTEGRATE = PARTICLES.register("disintegrate", () -> new SimpleParticleType(false));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> FIZZLE = PARTICLES.register("fizzle", () -> new SimpleParticleType(false));
    public static final DeferredHolder<ParticleType<?>, FeedParticleType> FEED = PARTICLES.register("feed", FeedParticleType::new);
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> LIGHTNING = PARTICLES.register("lightning", () -> new SimpleParticleType(false));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> SONIC_DISRUPTION = PARTICLES.register("sonic_disruption", () -> new SimpleParticleType(false));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> DUST_CLOUD = PARTICLES.register("dust_cloud", () -> new SimpleParticleType(false));
    public static final DeferredHolder<ParticleType<?>, ArrowTrailParticleType> ARROW_TRAIL = PARTICLES.register("arrow_trail", ArrowTrailParticleType::new);
    public static final DeferredHolder<ParticleType<?>, ShieldParticleType> SHIELD = PARTICLES.register("shield", ShieldParticleType::new);
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> DELAY = PARTICLES.register("delay", () -> new SimpleParticleType(false));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> IMBUE_FIZZLE = PARTICLES.register("imbue_fizzle", () -> new SimpleParticleType(false));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> IMBUE = PARTICLES.register("imbue", () -> new SimpleParticleType(false));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> ROTATE = PARTICLES.register("rotate", () -> new SimpleParticleType(false));
    public static final DeferredHolder<ParticleType<?>, SelfParticleType> SELF = PARTICLES.register("self", SelfParticleType::new);
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> SELF_FIZZLE = PARTICLES.register("self_fizzle", () -> new SimpleParticleType(false));
    public static final DeferredHolder<ParticleType<?>, ProjectileParticleType> PROJECTILE = PARTICLES.register("projectile", ProjectileParticleType::new);
    public static final DeferredHolder<ParticleType<?>, FeyParticleType> FEY = PARTICLES.register("fey", FeyParticleType::new);
}
