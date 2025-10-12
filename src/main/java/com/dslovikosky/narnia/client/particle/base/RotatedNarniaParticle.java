package com.dslovikosky.narnia.client.particle.base;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.multiplayer.ClientLevel;
import org.joml.Quaternionf;

public abstract class RotatedNarniaParticle extends NarniaParticle {
    protected Quaternionf rotation = new Quaternionf();

    public RotatedNarniaParticle(final ClientLevel clientLevel, final double x, final double y, final double z, final double xSpeed, final double ySpeed, final double zSpeed) {
        super(clientLevel, x, y, z, xSpeed, ySpeed, zSpeed);
    }

    @Override
    protected void renderRotatedQuad(VertexConsumer buffer, Quaternionf quaternion, float x, float y, float z, float partialTicks) {
        super.renderRotatedQuad(buffer, this.rotation, x, y, z, partialTicks);
    }
}
