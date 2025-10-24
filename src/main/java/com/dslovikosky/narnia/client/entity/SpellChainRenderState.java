package com.dslovikosky.narnia.client.entity;

import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.world.phys.Vec3;

import java.util.Random;

public class SpellChainRenderState extends EntityRenderState {
    private Vec3 startPos;
    private Vec3 endPos;
    private Random random;

    public Vec3 getStartPos() {
        return startPos;
    }

    public void setStartPos(Vec3 startPos) {
        this.startPos = startPos;
    }

    public Vec3 getEndPos() {
        return endPos;
    }

    public void setEndPos(Vec3 endPos) {
        this.endPos = endPos;
    }

    public Random getRandom() {
        return random;
    }

    public void setRandom(Random random) {
        this.random = random;
    }
}
