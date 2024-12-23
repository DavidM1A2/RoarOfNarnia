package com.dslovikosky.narnia.common.item;

import net.minecraft.world.item.Item;

public class TheChroniclesOfNarniaBookItem extends Item {
    private final Book book;

    public TheChroniclesOfNarniaBookItem(final Book book) {
        super(new Properties());
        this.book = book;
    }

    public enum Book {
        TheMagiciansNephew
    }
}
