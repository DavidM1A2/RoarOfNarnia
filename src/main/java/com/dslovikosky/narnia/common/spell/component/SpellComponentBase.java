package com.dslovikosky.narnia.common.spell.component;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public abstract class SpellComponentBase {
    private final ResourceLocation id;
    private final ResourceLocation icon;

    public SpellComponentBase(final ResourceLocation id, final ResourceLocation icon) {
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

    public ResourceLocation getId() {
        return id;
    }

    public ResourceLocation getIcon() {
        return icon;
    }
}
