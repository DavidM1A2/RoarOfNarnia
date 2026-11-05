package com.dslovikosky.narnia.client.renderer.block;

import com.dslovikosky.narnia.common.block.RuneBlock;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import org.jetbrains.annotations.Nullable;

public class RuneBlockEntityRenderState extends BlockEntityRenderState {
    public float tickCount = 0;
    public @Nullable RuneBlock.Color color = null;
}
