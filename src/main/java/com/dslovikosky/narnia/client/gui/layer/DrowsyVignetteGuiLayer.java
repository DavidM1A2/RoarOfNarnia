package com.dslovikosky.narnia.client.gui.layer;

import com.dslovikosky.narnia.client.gui.control.ImagePane;
import com.dslovikosky.narnia.client.gui.layout.Dimensions;
import com.dslovikosky.narnia.common.constants.Constants;
import com.dslovikosky.narnia.common.constants.ModMobEffects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.effect.MobEffectInstance;

import java.awt.Color;

public class DrowsyVignetteGuiLayer extends BaseGuiLayer {
    private ImagePane vignette;

    @Override
    public void initialize() {
        vignette = new ImagePane(Constants.modLocation("textures/gui/drowsy_vignette_layer/vignette.png"), ImagePane.DisplayMode.STRETCH);
        vignette.setPrefSize(new Dimensions(1.0, 1.0, true));
        vignette.setColor(new Color(0, 0, 0, 255));
        contentPane.add(vignette);
    }

    @Override
    public void tick() {
        final LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) {
            contentPane.setVisible(false);
            return;
        }

        final MobEffectInstance drowsyEffect = player.getEffect(ModMobEffects.DROWSY);
        if (drowsyEffect == null) {
            contentPane.setVisible(false);
            return;
        }

        vignette.setColor(new Color(0, 0, 0, Math.clamp(drowsyEffect.getAmplifier() * 25L, 0, 255)));
        contentPane.setVisible(true);
    }
}
