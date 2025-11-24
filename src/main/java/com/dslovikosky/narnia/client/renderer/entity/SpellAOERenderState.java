package com.dslovikosky.narnia.client.renderer.entity;

import net.minecraft.client.renderer.entity.state.EntityRenderState;

import java.awt.Color;

public class SpellAOERenderState extends EntityRenderState {
    private float radius;
    private Color color;
    private int lifespanTicks;

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public int getLifespanTicks() {
        return lifespanTicks;
    }

    public void setLifespanTicks(int lifespanTicks) {
        this.lifespanTicks = lifespanTicks;
    }
}
