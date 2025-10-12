package com.dslovikosky.narnia.client.event;

import com.dslovikosky.narnia.client.particle.ArrowTrailParticle;
import com.dslovikosky.narnia.client.particle.CleanseParticle;
import com.dslovikosky.narnia.client.particle.DelayParticle;
import com.dslovikosky.narnia.client.particle.DigParticle;
import com.dslovikosky.narnia.client.particle.DisintegrateParticle;
import com.dslovikosky.narnia.client.particle.DustCloudParticle;
import com.dslovikosky.narnia.client.particle.EnderParticle;
import com.dslovikosky.narnia.client.particle.ExplosionParticle;
import com.dslovikosky.narnia.client.particle.FeedParticle;
import com.dslovikosky.narnia.client.particle.FeyParticle;
import com.dslovikosky.narnia.client.particle.FireParticle;
import com.dslovikosky.narnia.client.particle.FizzleParticle;
import com.dslovikosky.narnia.client.particle.FreezeParticle;
import com.dslovikosky.narnia.client.particle.GrowParticle;
import com.dslovikosky.narnia.client.particle.HealParticle;
import com.dslovikosky.narnia.client.particle.ImbueFizzleParticle;
import com.dslovikosky.narnia.client.particle.ImbueParticle;
import com.dslovikosky.narnia.client.particle.LightningParticle;
import com.dslovikosky.narnia.client.particle.PoisonParticle;
import com.dslovikosky.narnia.client.particle.ProjectileParticle;
import com.dslovikosky.narnia.client.particle.RotateParticle;
import com.dslovikosky.narnia.client.particle.SelfFizzleParticle;
import com.dslovikosky.narnia.client.particle.SelfParticle;
import com.dslovikosky.narnia.client.particle.ShieldParticle;
import com.dslovikosky.narnia.client.particle.SmokeScreenParticle;
import com.dslovikosky.narnia.client.particle.SonicDisruptionParticle;
import com.dslovikosky.narnia.client.particle.SpellHitParticle;
import com.dslovikosky.narnia.client.particle.SpellLaserParticle;
import com.dslovikosky.narnia.client.particle.StrengthParticle;
import com.dslovikosky.narnia.client.particle.WardParticle;
import com.dslovikosky.narnia.client.particle.WeaknessParticle;
import com.dslovikosky.narnia.common.constants.ModParticleTypes;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;

public class ParticleRegister {
    @SubscribeEvent
    public void onParticleFactoryRegisterEvent(final RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(ModParticleTypes.ARROW_TRAIL.get(), ArrowTrailParticle.Factory::new);
        event.registerSpriteSet(ModParticleTypes.CLEANSE.get(), CleanseParticle.Factory::new);
        event.registerSpriteSet(ModParticleTypes.DELAY.get(), DelayParticle.Factory::new);
        event.registerSpriteSet(ModParticleTypes.DIG.get(), DigParticle.Factory::new);
        event.registerSpriteSet(ModParticleTypes.DISINTEGRATE.get(), DisintegrateParticle.Factory::new);
        event.registerSpriteSet(ModParticleTypes.DUST_CLOUD.get(), DustCloudParticle.Factory::new);
        event.registerSpriteSet(ModParticleTypes.ENDER.get(), EnderParticle.Factory::new);
        event.registerSpriteSet(ModParticleTypes.EXPLOSION.get(), ExplosionParticle.Factory::new);
        event.registerSpriteSet(ModParticleTypes.FEED.get(), FeedParticle.Factory::new);
        event.registerSpriteSet(ModParticleTypes.FEY.get(), FeyParticle.Factory::new);
        event.registerSpriteSet(ModParticleTypes.FIRE.get(), FireParticle.Factory::new);
        event.registerSpriteSet(ModParticleTypes.FIZZLE.get(), FizzleParticle.Factory::new);
        event.registerSpriteSet(ModParticleTypes.FREEZE.get(), FreezeParticle.Factory::new);
        event.registerSpriteSet(ModParticleTypes.GROW.get(), GrowParticle.Factory::new);
        event.registerSpriteSet(ModParticleTypes.HEAL.get(), HealParticle.Factory::new);
        event.registerSpriteSet(ModParticleTypes.IMBUE_FIZZLE.get(), ImbueFizzleParticle.Factory::new);
        event.registerSpriteSet(ModParticleTypes.IMBUE.get(), ImbueParticle.Factory::new);
        event.registerSpriteSet(ModParticleTypes.LIGHTNING.get(), LightningParticle.Factory::new);
        event.registerSpriteSet(ModParticleTypes.POISON.get(), PoisonParticle.Factory::new);
        event.registerSpriteSet(ModParticleTypes.PROJECTILE.get(), ProjectileParticle.Factory::new);
        event.registerSpriteSet(ModParticleTypes.ROTATE.get(), RotateParticle.Factory::new);
        event.registerSpriteSet(ModParticleTypes.SELF_FIZZLE.get(), SelfFizzleParticle.Factory::new);
        event.registerSpriteSet(ModParticleTypes.SELF.get(), SelfParticle.Factory::new);
        event.registerSpriteSet(ModParticleTypes.SHIELD.get(), ShieldParticle.Factory::new);
        event.registerSpriteSet(ModParticleTypes.SMOKE_SCREEN.get(), SmokeScreenParticle.Factory::new);
        event.registerSpriteSet(ModParticleTypes.SONIC_DISRUPTION.get(), SonicDisruptionParticle.Factory::new);
        event.registerSpriteSet(ModParticleTypes.SPELL_HIT.get(), SpellHitParticle.Factory::new);
        event.registerSpriteSet(ModParticleTypes.SPELL_LASER.get(), SpellLaserParticle.Factory::new);
        event.registerSpriteSet(ModParticleTypes.STRENGTH.get(), StrengthParticle.Factory::new);
        event.registerSpriteSet(ModParticleTypes.WARD.get(), WardParticle.Factory::new);
        event.registerSpriteSet(ModParticleTypes.WEAKNESS.get(), WeaknessParticle.Factory::new);
    }
}
