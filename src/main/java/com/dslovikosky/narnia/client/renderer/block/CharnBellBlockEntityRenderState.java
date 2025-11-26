package com.dslovikosky.narnia.client.renderer.block;

import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;

public class CharnBellBlockEntityRenderState extends BlockEntityRenderState {
    public boolean hitNorth = true;
    public long lastHitTime = 0;
    public long gameTime = 0;
    public float partialTick = 0f;
}