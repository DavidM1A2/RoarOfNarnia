package com.dslovikosky.narnia.common.spell.component;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

import java.util.Random;

public abstract class SpellComponentBase {
    protected static final Random RANDOM = new Random();

    private final Identifier id;
    private final Identifier icon;

    public SpellComponentBase(final Identifier id, final Identifier icon) {
        this.id = id;
        this.icon = icon;
    }

    protected abstract String getUnlocalizedBaseName();

    public Component getName() {
        return Component.translatable(getUnlocalizedBaseName() + ".name");
    }

    public Component getDescription() {
        return Component.translatable(getUnlocalizedBaseName() + ".description");
    }

    public Identifier getId() {
        return id;
    }

    public Identifier getIcon() {
        return icon;
    }
}
