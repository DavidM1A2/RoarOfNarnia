package com.dslovikosky.narnia.client.event;

import com.dslovikosky.narnia.client.constants.ModRenderers;
import com.dslovikosky.narnia.client.sound.WoodBetweenTheWorldsMusicSound;
import com.dslovikosky.narnia.common.constants.ModDimensions;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;

public class WoodBetweenTheWorldsClientHandler {
    @SubscribeEvent
    public void onRenderLevelStageEvent(final RenderLevelStageEvent.AfterWeather event) {
        ModRenderers.DROWSY_VIGNETTE.render(event.getPartialTick());
    }

    @SubscribeEvent
    public void onEntityJoinLevelEvent(final EntityJoinLevelEvent event) {
        final Level level = event.getLevel();
        if (!level.isClientSide()) {
            return;
        }

        if (level.dimension() != ModDimensions.WOOD_BETWEEN_THE_WORLDS) {
            return;
        }

        final LocalPlayer player = Minecraft.getInstance().player;
        if (event.getEntity() != player) {
            return;
        }

        if (player.level().dimension() != ModDimensions.WOOD_BETWEEN_THE_WORLDS) {
            return;
        }

        final SoundManager soundManager = Minecraft.getInstance().getSoundManager();
        soundManager.play(new WoodBetweenTheWorldsMusicSound(player));
    }
}
