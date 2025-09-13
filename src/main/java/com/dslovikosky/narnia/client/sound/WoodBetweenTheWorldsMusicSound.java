package com.dslovikosky.narnia.client.sound;

import com.dslovikosky.narnia.common.constants.ModDimensions;
import com.dslovikosky.narnia.common.constants.ModSoundEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.sounds.EntityBoundSoundInstance;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;

public class WoodBetweenTheWorldsMusicSound extends EntityBoundSoundInstance {
    public WoodBetweenTheWorldsMusicSound(final Player player) {
        super(ModSoundEvents.WOOD_BETWEEN_THE_WORLDS.get(), SoundSource.AMBIENT, 1f, 1f, player, player.getRandom().nextLong());
        looping = true;
        delay = 0;
    }

    @Override
    public boolean canPlaySound() {
        final LocalPlayer player = Minecraft.getInstance().player;
        return player != null && player.level().dimension() == ModDimensions.WOOD_BETWEEN_THE_WORLDS;
    }
}
