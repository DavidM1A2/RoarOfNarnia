package com.dslovikosky.narnia.client.particle.base;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.state.QuadParticleRenderState;
import org.joml.Quaternionf;

public abstract class RotatedNarniaParticle extends NarniaParticle {
    protected Quaternionf rotation = new Quaternionf();

    public RotatedNarniaParticle(final ClientLevel clientLevel, final double x, final double y, final double z, final double xSpeed, final double ySpeed, final double zSpeed, final SpriteSet spriteSet) {
        super(clientLevel, x, y, z, xSpeed, ySpeed, zSpeed, spriteSet);
    }

    @Override
    protected void extractRotatedQuad(QuadParticleRenderState reusedState, Quaternionf orientation, float x, float y, float z, float partialTick) {
        super.extractRotatedQuad(reusedState, this.rotation, x, y, z, partialTick);
    }
}
