package com.dslovikosky.narnia.client.renderer.block;

import com.dslovikosky.narnia.common.model.CharnStatueType;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.core.Direction;

public class CharnImageHallStatueBlockEntityRenderState extends BlockEntityRenderState {
    public CharnStatueType statueType = CharnStatueType.JADIS;
    public Direction facing = Direction.NORTH;
}
