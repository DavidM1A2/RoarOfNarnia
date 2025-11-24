package com.dslovikosky.narnia.client.renderer.entity;

import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.world.phys.Vec3;

import java.awt.Color;

public class SpellConeRenderState extends EntityRenderState {
    private float radius;
    private Color color;
    private int lifespanTicks;
    private float length;
    private Vec3 direction;

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

    public float getLength() {
        return length;
    }

    public void setLength(float length) {
        this.length = length;
    }

    public Vec3 getDirection() {
        return direction;
    }

    public void setDirection(Vec3 direction) {
        this.direction = direction;
    }
}
