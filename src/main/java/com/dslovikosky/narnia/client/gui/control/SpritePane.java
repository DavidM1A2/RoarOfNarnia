package com.dslovikosky.narnia.client.gui.control;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ARGB;
import org.joml.Matrix3x2fStack;

import java.util.ArrayList;
import java.util.List;

public class SpritePane extends GuiPane {
    private final ResourceLocation spriteSheetTexture;
    private final ImagePane.DisplayMode displayMode;
    private final int columns;
    private final int rows;
    private final List<Integer> curAnimation = new ArrayList<>();
    private AnimationMode curAnimationMode = null;
    private int textureWidth = -1;
    private int textureHeight = -1;
    private int frameWidth = -1;
    private int frameHeight = -1;
    private int curFrame = 0;
    private double curFPS = 24.0;
    private long lastTime = 0;

    public SpritePane(final ResourceLocation spriteSheetTexture, final ImagePane.DisplayMode displayMode, final int columns, final int rows) {
        this.spriteSheetTexture = spriteSheetTexture;
        this.displayMode = displayMode;
        this.columns = columns;
        this.rows = rows;
        loadTextureDimensions();
    }

    @Override
    public void draw(GuiGraphics guiGraphics) {
        if (this.isVisible()) {
            handleAnimation();

            final Matrix3x2fStack poseStack = guiGraphics.pose();
            poseStack.pushMatrix();

            // Check for invalid texture dimensions
            if (textureHeight > -1 && textureWidth > -1) {
                final int col = curFrame % columns;
                final int row = curFrame / columns;
                guiGraphics.blit(RenderPipelines.GUI_TEXTURED, spriteSheetTexture,
                        getX(), getY(),
                        (float) col * frameWidth, (float) row * frameHeight,
                        getWidth(), getHeight(),
                        frameWidth, frameHeight,
                        textureWidth, textureHeight,
                        ARGB.color(getColor().getAlpha(), getColor().getRed(), getColor().getGreen(), getColor().getBlue()));
            }
            poseStack.popMatrix();

            // Draw the any children
            super.draw(guiGraphics);
        }
    }

    private void handleAnimation() {
        if (!curAnimation.isEmpty()) {
            final double msBetweenFrames = 1000 / curFPS;
            final long time = System.currentTimeMillis();
            final long timeDelta = time - lastTime;
            if (timeDelta > msBetweenFrames) {
                curFrame = curAnimation.removeFirst();
                if (curAnimationMode == AnimationMode.LOOP) curAnimation.add(curFrame);
                final long overshoot = (long) (timeDelta - msBetweenFrames);
                // Try to "catch up" if we overshot the frame, but not by more than one frame
                lastTime = time - Math.min(overshoot, (long) msBetweenFrames);
            }
        }
    }

    public void setFrame(final int frame) {
        this.curFrame = frame;
        this.invalidate();
    }

    public void setAnimation(final List<Integer> frames, final AnimationMode mode, final double fps) {
        this.curAnimation.clear();
        this.curAnimation.addAll(frames);
        this.curAnimationMode = mode;
        this.curFPS = fps;
    }

    public void stopAnimation() {
        this.curAnimation.clear();
    }

    private void loadTextureDimensions() {
        final AbstractTexture texture = Minecraft.getInstance().getTextureManager().getTexture(this.spriteSheetTexture);
        textureWidth = texture.getTexture().getWidth(0);
        textureHeight = texture.getTexture().getHeight(0);
        frameWidth = textureWidth / columns;
        frameHeight = textureHeight / rows;
    }

    @Override
    public void negotiateDimensions(double width, Double height) {
        final double fitWidth = Math.min(getPrefSize().isRelative() ? getPrefSize().width() * width : getPrefSize().width(), width);
        final double fitHeight = Math.min(getPrefSize().isRelative() ? getPrefSize().height() * height : getPrefSize().height(), height);

        switch (displayMode) {
            case ImagePane.DisplayMode.FIT_TO_TEXTURE -> {
                final double scaleXRatio = Math.min(fitWidth / frameWidth, 1.0);
                final double scaleYRatio = Math.min(fitHeight / frameHeight, 1.0);
                final double scaleMinRatio = Math.min(scaleXRatio, scaleYRatio);
                this.setWidth((int) Math.round(frameWidth * scaleMinRatio));
                this.setHeight((int) Math.round(frameHeight * scaleMinRatio));
            }
            case ImagePane.DisplayMode.FIT_TO_PARENT -> {
                final double scaleXRatio = fitWidth / frameWidth;
                final double scaleYRatio = fitHeight / frameHeight;
                final double scaleMinRatio = Math.min(scaleXRatio, scaleYRatio);
                this.setWidth((int) Math.round(frameWidth * scaleMinRatio));
                this.setHeight((int) Math.round(frameHeight * scaleMinRatio));
            }
            case ImagePane.DisplayMode.STRETCH -> {
                this.setWidth((int) Math.round(fitWidth));
                this.setHeight((int) Math.round(fitHeight));
            }
        }
        // Reset the inbounds flag
        setInBounds(true);
    }

    public enum AnimationMode {
        ONE_SHOT,
        LOOP
    }
}
