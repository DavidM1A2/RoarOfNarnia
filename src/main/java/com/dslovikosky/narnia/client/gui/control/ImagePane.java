package com.dslovikosky.narnia.client.gui.control;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix3x2fStack;

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
            final Matrix3x2fStack poseStack = guiGraphics.pose();
            poseStack.pushMatrix();

            // Check for invalid texture dimensions
            if (textureHeight > -1 && textureWidth > -1) {
                guiGraphics.blit(RenderPipelines.GUI_TEXTURED, imageTexture, getX(), getY(), u, v, getWidth(), getHeight(), getWidth(), getHeight(), this.getColor().getRGB());
            }
            poseStack.popMatrix();

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
            final AbstractTexture texture = Minecraft.getInstance().getTextureManager().getTexture(this.imageTexture);
            textureWidth = texture.getTexture().getWidth(0);
            textureHeight = texture.getTexture().getHeight(0);
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
