package com.dslovikosky.narnia.common.item;

import com.dslovikosky.narnia.common.constants.ModLevels;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.portal.DimensionTransition;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class RingItem extends Item {
    private final Type type;

    public RingItem(final Type type) {
        super(new Properties().stacksTo(1).fireResistant());
        this.type = type;
    }

    @Override
    public void inventoryTick(final ItemStack itemStack, final Level level, final Entity entity, final int slotId, final boolean isSelected) {
        if (!isSelected) {
            return;
        }

        if (level.isClientSide()) {
            return;
        }

        if (type == Type.GREEN && ModLevels.WOOD_BETWEEN_THE_WORLDS == level.dimension()) {
            final ServerLevel overworld = level.getServer().getLevel(Level.OVERWORLD);
            entity.changeDimension(new DimensionTransition(overworld, entity, transitionedEntity -> {
            }));
        } else if (type == Type.YELLOW && ModLevels.WOOD_BETWEEN_THE_WORLDS != level.dimension()) {
            final ServerLevel woodBetweenTheWorlds = level.getServer().getLevel(ModLevels.WOOD_BETWEEN_THE_WORLDS);
            entity.changeDimension(new DimensionTransition(woodBetweenTheWorlds, entity, transitionedEntity -> {
            }));
        }
    }

    public enum Type {
        YELLOW, GREEN
    }
}
