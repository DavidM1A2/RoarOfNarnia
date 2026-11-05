package com.dslovikosky.narnia.client.event;

import com.dslovikosky.narnia.client.constants.ModRenderers;
import com.dslovikosky.narnia.client.sound.WoodBetweenTheWorldsMusicSound;
import com.dslovikosky.narnia.common.constants.ModDimensions;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.ExtractLevelRenderStateEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;

public class WoodBetweenTheWorldsClientHandler {
    // The vignette opens its own render pass over the main target, so it needs a stage with no pass already
    // open. AfterLevel is the only one, and it still runs inside renderLevel so the HUD draws on top.
    @SubscribeEvent
    public void onRenderLevelStageEvent(final RenderLevelStageEvent.AfterLevel event) {
        ModRenderers.Client.DROWSY_VIGNETTE.render(Minecraft.getInstance().gameRenderer.mainRenderTarget(), event.getLevelRenderState());
    }

    @SubscribeEvent
    public void onExtractLevelRenderStateEvent(final ExtractLevelRenderStateEvent event) {
        ModRenderers.Client.DROWSY_VIGNETTE.extract(event.getDeltaTracker(), event.getRenderState());
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
