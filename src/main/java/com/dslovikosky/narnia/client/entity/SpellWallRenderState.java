package com.dslovikosky.narnia.client.entity;

import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.world.phys.Vec3;

import java.awt.Color;

public class SpellWallRenderState extends EntityRenderState {
    private Color color;
    private Vec3 width;
    private Vec3 height;
    private int lifespanTicks;

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Vec3 getWidth() {
        return width;
    }

    public void setWidth(Vec3 width) {
        this.width = width;
    }

    public Vec3 getHeight() {
        return height;
    }

    public void setHeight(Vec3 height) {
        this.height = height;
    }

    public int getLifespanTicks() {
        return lifespanTicks;
    }

    public void setLifespanTicks(int lifespanTicks) {
        this.lifespanTicks = lifespanTicks;
    }
}
