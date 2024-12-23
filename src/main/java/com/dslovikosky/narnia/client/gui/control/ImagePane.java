package com.dslovikosky.narnia.client.gui.control;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class ImagePane extends GuiPane {
    private double textureWidth = -1.0;
    private double textureHeight = -1.0;
    private ResourceLocation imageTexture;
    private DisplayMode displayMode;
    private double allottedWidth = -1.0;
    private double allottedHeight = -1.0;
    private float u = 0.0f;
    private float v = 0.0f;

    public ImagePane(final ResourceLocation imageTexture, final DisplayMode displayMode) {
        this.imageTexture = imageTexture;
        this.displayMode = displayMode;
        loadTextureDimensions();
    }

    @Override
    public void draw(final GuiGraphics guiGraphics) {
        if (this.isVisible() && this.imageTexture != null) {
            final PoseStack poseStack = guiGraphics.pose();
            poseStack.pushPose();
            // Enable alpha blending
            RenderSystem.enableBlend();

            // Set the color
            RenderSystem.setShaderColor(this.getColor().getRed() / 255f, this.getColor().getGreen() / 255f, this.getColor().getBlue() / 255f, this.getColor().getAlpha() / 255f);
            // Check for invalid texture dimensions
            if (textureHeight > -1 && textureWidth > -1) {
                guiGraphics.blit(imageTexture, getX(), getY(), u, v, getWidth(), getHeight(), getWidth(), getHeight());
            }
            poseStack.popPose();

            // Draw any children
            super.draw(guiGraphics);
        }
    }

    @Override
    public void negotiateDimensions(final double width, final Double height) {
        // Save the allotted dimensions so we can redraw ourselves later without invalidating the whole screen
        this.allottedWidth = width;
        this.allottedHeight = height;
        // Do the actual resize
        this.setActualDimensions(width, height);
        // Reset the inbounds flag
        this.setInBounds(true);
    }

    public void updateImageTexture(final ResourceLocation imageTexture) {
        final boolean textureChanged = imageTexture != this.imageTexture;
        this.imageTexture = imageTexture;
        if (textureChanged) {
            this.loadTextureDimensions();
            this.setActualDimensions(allottedWidth, allottedHeight);
            this.calcChildrenBounds();
        }
    }

    private void loadTextureDimensions() {
        if (imageTexture != null) {
            Minecraft.getInstance().getTextureManager().bindForSetup(this.imageTexture);
            textureWidth = GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_WIDTH);
            textureHeight = GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_HEIGHT);
            if (Double.isNaN(textureWidth) || Double.isNaN(textureHeight)) {
                throw new IllegalStateException(String.format("Texture %s does not exist", imageTexture));
            }
        } else {
            textureWidth = -1.0;
            textureHeight = -1.0;
        }
    }

    private void setActualDimensions(final double width, final double height) {
        final double fitWidth = Math.min(width, getPrefSize().isRelative() ? getPrefSize().width() * width : getPrefSize().width());
        final double fitHeight = Math.min(height, getPrefSize().isRelative() ? getPrefSize().height() * height : getPrefSize().height());
        switch (displayMode) {
            case DisplayMode.FIT_TO_TEXTURE -> {
                final double scaleXRatio = Math.min(1.0, fitWidth / textureWidth);
                final double scaleYRatio = Math.min(1.0, fitHeight / textureHeight);
                final double scaleMinRatio = Math.min(scaleXRatio, scaleYRatio);
                setWidth((int) Math.round(textureWidth * scaleMinRatio));
                setHeight((int) Math.round(textureHeight * scaleMinRatio));
            }
            case DisplayMode.FIT_TO_PARENT -> {
                final double scaleXRatio = fitWidth / textureWidth;
                final double scaleYRatio = fitHeight / textureHeight;
                final double scaleMinRatio = Math.min(scaleXRatio, scaleYRatio);
                setWidth((int) Math.round(textureWidth * scaleMinRatio));
                setHeight((int) Math.round(textureHeight * scaleMinRatio));
            }
            case DisplayMode.STRETCH -> {
                final double scaleXRatio = fitWidth / textureWidth;
                final double scaleYRatio = fitHeight / textureHeight;
                setWidth((int) Math.round(textureWidth * scaleXRatio));
                setHeight((int) Math.round(textureHeight * scaleYRatio));
            }
        }
    }

    public enum DisplayMode {
        FIT_TO_TEXTURE,
        FIT_TO_PARENT,
        STRETCH
    }
}
