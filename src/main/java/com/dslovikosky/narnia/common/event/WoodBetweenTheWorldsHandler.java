package com.dslovikosky.narnia.common.event;

import com.dslovikosky.narnia.common.constants.ModAttachmentTypes;
import com.dslovikosky.narnia.common.constants.ModDimensions;
import com.dslovikosky.narnia.common.constants.ModMobEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.EntityTravelToDimensionEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

public class WoodBetweenTheWorldsHandler {
    public static final int TICKS_PER_DROWSY_LEVEL = 60 * 20; // 1 min per level
    public static final int MAX_DROWSY_LEVEL = 10;
    private static final double SWAY_SPEED = 0.05;
    private static final double SWAY_PUSH_POWER = 0.003;

    @SubscribeEvent
    public void onPlayerTickEvent(final PlayerTickEvent.Pre event) {
        final Player player = event.getEntity();
        final Level level = player.level();

        if (!level.dimension().equals(ModDimensions.WOOD_BETWEEN_THE_WORLDS)) {
            return;
        }

        // ticksInWoodBetweenTheWorlds++
        final int ticksInWoodBetweenTheWorlds = player.getData(ModAttachmentTypes.TICKS_IN_WOOD_BETWEEN_THE_WORLDS);
        player.setData(ModAttachmentTypes.TICKS_IN_WOOD_BETWEEN_THE_WORLDS, ticksInWoodBetweenTheWorlds + 1);

        final int drowsinessAmplitude = Math.min(ticksInWoodBetweenTheWorlds / TICKS_PER_DROWSY_LEVEL, MAX_DROWSY_LEVEL - 1);

        if (level.isClientSide()) {
            if (drowsinessAmplitude >= MAX_DROWSY_LEVEL / 2) {
                // Offset sin/cos so we don't go in circles, but instead around "randomly"
                final double pushForceX = Math.sin(player.tickCount * SWAY_SPEED * 0.6) * drowsinessAmplitude * SWAY_PUSH_POWER;
                final double pushForceZ = Math.cos(player.tickCount * SWAY_SPEED) * drowsinessAmplitude * SWAY_PUSH_POWER;
                player.push(pushForceX, 0, pushForceZ);
            }

            return;
        }

        // Re-apply drowsy based on ticksInWoodBetweenTheWorlds
        if (player.tickCount % 20 == 0) {
            player.addEffect(new MobEffectInstance(ModMobEffects.DROWSY, 100, drowsinessAmplitude, true, true, true));
        }

        // Feed the player so they never need to eat
        final FoodData foodData = player.getFoodData();
        final boolean needsSaturation = foodData.getSaturationLevel() < 4f;
        if (player.tickCount % 20 == 0 && (needsSaturation || foodData.needsFood())) {
            foodData.eat(1, needsSaturation ? 1f : 0f);
        }
    }

    @SubscribeEvent
    public void onEntityTravelToDimensionEvent(final EntityTravelToDimensionEvent event) {
        // Reset ticks in wood between the worlds
        if (event.getDimension() == ModDimensions.WOOD_BETWEEN_THE_WORLDS) {
            event.getEntity().setData(ModAttachmentTypes.TICKS_IN_WOOD_BETWEEN_THE_WORLDS, 0);
        }
    }
}
