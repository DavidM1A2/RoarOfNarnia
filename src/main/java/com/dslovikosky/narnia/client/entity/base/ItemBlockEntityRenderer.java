package com.dslovikosky.narnia.client.entity.base;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;

public class ItemBlockEntityRenderer implements IClientItemExtensions {
    private final BlockEntityWithoutLevelRenderer renderer;

    public ItemBlockEntityRenderer(final BlockEntityWithoutLevelRenderer renderer) {
        this.renderer = renderer;
    }

    @Override
    public @NotNull BlockEntityWithoutLevelRenderer getCustomRenderer() {
        return renderer;
    }
}
