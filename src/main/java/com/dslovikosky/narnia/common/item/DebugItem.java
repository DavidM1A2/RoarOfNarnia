package com.dslovikosky.narnia.common.item;

import com.dslovikosky.narnia.common.constants.ModDimensions;
import com.mojang.logging.LogUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.portal.DimensionTransition;
import org.slf4j.Logger;

public class DebugItem extends Item {
    private static final Logger LOG = LogUtils.getLogger();

    public DebugItem() {
        super(new Properties().stacksTo(1));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(final Level pLevel, final Player pPlayer, final InteractionHand pUsedHand) {
        if (!pLevel.isClientSide()) {
            if (pPlayer.level().dimension() == ModDimensions.LONDON) {
                final ServerLevel overworld = pLevel.getServer().getLevel(Level.OVERWORLD);
                pPlayer.changeDimension(new DimensionTransition(overworld, pPlayer, e -> {
                }));
            } else {
                final ServerLevel london = pLevel.getServer().getLevel(ModDimensions.LONDON);
                pPlayer.changeDimension(new DimensionTransition(london, pPlayer, e -> {
                }));
            }
        }
        return super.use(pLevel, pPlayer, pUsedHand);
    }
}
