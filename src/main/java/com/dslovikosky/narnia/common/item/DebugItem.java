package com.dslovikosky.narnia.common.item;

import com.dslovikosky.narnia.common.constants.ModBooks;
import com.dslovikosky.narnia.common.model.NarniaGlobalData;
import com.dslovikosky.narnia.common.model.chapter.Scene;
import com.mojang.logging.LogUtils;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.slf4j.Logger;

import java.util.Objects;

public class DebugItem extends Item {
    private static final Logger LOG = LogUtils.getLogger();

    public DebugItem() {
        super(new Properties().stacksTo(1));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(final Level pLevel, final Player pPlayer, final InteractionHand pUsedHand) {
        final NarniaGlobalData data = NarniaGlobalData.getInstance(pLevel);
        final Scene runningChapter = data.getActiveChapter();
        if (!pLevel.isClientSide()) {
            if (runningChapter == null) {
                data.setActiveChapter(new Scene(ModBooks.THE_MAGICIANS_NEPHEW, ModBooks.THE_MAGICIANS_NEPHEW.chapters().get(0)));
            } else {
                data.setActiveChapter(null);
            }
            data.syncAll();
            data.markDirty();
        }
        LOG.info(Objects.toString(runningChapter));
        return super.use(pLevel, pPlayer, pUsedHand);
    }
}
