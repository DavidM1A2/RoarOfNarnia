package com.dslovikosky.narnia.common.item;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

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

        if (type == Type.GREEN) {

        } else if (type == Type.YELLOW) {

        }
    }

    public enum Type {
        YELLOW,
        GREEN
    }
}
