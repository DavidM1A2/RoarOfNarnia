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
    /**
     * The vignette is a full screen post effect, so it opens its own render pass over the main target. In 26.3
     * AfterWeather fires from inside LevelRenderer's transparency pass with a render pass already open, and
     * opening a second one there throws. AfterLevel is the last stage to fire and the only one handed a null
     * render pass, which makes it the one place a post effect can still do this - and it is where a pass over
     * the finished level belongs anyway. It is still inside renderLevel, so the HUD draws on top as before.
     */
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
