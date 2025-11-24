package com.dslovikosky.narnia.client.renderer.entity;

import net.minecraft.client.renderer.entity.state.EntityRenderState;

import java.awt.Color;
import java.util.Random;

public class SpellProjectileRenderState extends EntityRenderState {
    private Random random;
    private Color color;

    public Random getRandom() {
        return random;
    }

    public void setRandom(Random random) {
        this.random = random;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
