package com.dslovikosky.narnia.client.gui.screen;

import com.dslovikosky.narnia.client.gui.control.StackPane;
import com.dslovikosky.narnia.client.gui.event.KeyEvent;
import com.dslovikosky.narnia.client.gui.event.MouseDragEvent;
import com.dslovikosky.narnia.client.gui.event.MouseEvent;
import com.dslovikosky.narnia.client.gui.event.MouseMoveEvent;
import com.dslovikosky.narnia.client.gui.event.MouseScrollEvent;
import com.dslovikosky.narnia.client.gui.layout.Dimensions;
import com.dslovikosky.narnia.client.gui.layout.GuiUtility;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public abstract class BaseScreen extends Screen {
    protected final StackPane contentPane;
    private boolean isScreenValid;

    protected BaseScreen(final Component title) {
        super(title);
        contentPane = new StackPane();
        contentPane.setPrefSize(GuiUtility.getWindowSizeInMCCoords());
        renderables.clear();
        invalidate();
    }

    public void invalidate() {
        isScreenValid = false;
    }

    public void update() {
        final Dimensions windowSize = GuiUtility.getWindowSizeInMCCoords();
        // Record dimensions so we can tell when they change
        this.contentPane.setPrefSize(windowSize);
        // Fit panes to the screen
        this.contentPane.negotiateDimensions(windowSize.width(), windowSize.height());
        // Resize any children to fit the new dimensions
        this.contentPane.update();
        isScreenValid = true;
    }

    @Override
    public void render(final GuiGraphics pGuiGraphics, final int pMouseX, final int pMouseY, final float pPartialTick) {
        // If we want a gradient background draw that background
        if (this.drawGradientBackground()) {
            this.renderBackground(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        }
        // Trigger an update if the screen has changed size
        if (!contentPane.getPrefSize().equals(GuiUtility.getWindowSizeInMCCoords())) {
            isScreenValid = false;
        }
        // Perform an update if necessary
        if (!isScreenValid) {
            this.update();
        }
        // Draw the content pane
        this.contentPane.draw(pGuiGraphics);
        // Draw the overlay on top of the content pane
        this.contentPane.drawOverlay(pGuiGraphics);
        // Enable blend so we can draw opacity
        RenderSystem.enableBlend();
        // Disable blend now that we drew the UI
        RenderSystem.disableBlend();
    }

    public boolean drawGradientBackground() {
        return true;
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public boolean charTyped(final char pCodePoint, final int pModifiers) {
        // MIN_VALUE default since we don't know what int the char corresponds to
        this.contentPane.processKeyInput(new KeyEvent(this.contentPane, Integer.MIN_VALUE, Integer.MIN_VALUE, pCodePoint, pModifiers, KeyEvent.KeyEventType.Type));
        return super.charTyped(pCodePoint, pModifiers);
    }

    @Override
    public boolean keyPressed(final int pKeyCode, final int pScanCode, final int pModifiers) {
        this.contentPane.processKeyInput(new KeyEvent(this.contentPane, pKeyCode, pScanCode, Character.MIN_VALUE, pModifiers, KeyEvent.KeyEventType.Press));
        if (super.keyPressed(pKeyCode, pScanCode, pModifiers)) {
            return true;
        }
        // If our inventory key closes the screen, test if that key was pressed
        if (this.inventoryToCloseGuiScreen()) {
            // if the keycode is the inventory key bind close the GUI screen
            if (isInventoryKeybind(pKeyCode, pScanCode)) {
                // Close the screen
                onClose();
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean keyReleased(final int pKeyCode, final int pScanCode, final int pModifiers) {
        final boolean result = super.keyReleased(pKeyCode, pScanCode, pModifiers);
        this.contentPane.processKeyInput(new KeyEvent(this.contentPane, pKeyCode, pScanCode, Character.MIN_VALUE, pModifiers, KeyEvent.KeyEventType.Release));
        return result;
    }

    protected boolean inventoryToCloseGuiScreen() {
        return true;
    }

    @Override
    public boolean mouseClicked(final double pMouseX, final double pMouseY, final int pButton) {
        // Fire the mouse clicked event
        contentPane.processMouseInput(new MouseEvent(contentPane, (int) Math.round(pMouseX), (int) Math.round(pMouseY), pButton, MouseEvent.EventType.Click));
        return super.mouseClicked(pMouseX, pMouseY, pButton);
    }

    @Override
    public boolean mouseReleased(final double pMouseX, final double pMouseY, final int pButton) {
        // Fire the release event
        contentPane.processMouseInput(new MouseEvent(contentPane, (int) Math.round(pMouseX), (int) Math.round(pMouseY), pButton, MouseEvent.EventType.Release));
        return super.mouseReleased(pMouseX, pMouseY, pButton);
    }

    @Override
    public boolean mouseScrolled(final double pMouseX, final double pMouseY, final double pScrollX, final double pScrollY) {
        // Fire the content pane's mouse scroll listener
        contentPane.processMouseScrollInput(new MouseScrollEvent(contentPane, (int) Math.round(pScrollY)));
        return super.mouseScrolled(pMouseX, pMouseY, pScrollX, pScrollY);
    }

    @Override
    public boolean mouseDragged(final double pMouseX, final double pMouseY, final int pButton, final double pDragX, final double pDragY) {
        contentPane.processMouseDragInput(new MouseDragEvent(contentPane, (int) Math.round(pMouseX), (int) Math.round(pMouseY), pButton));
        return super.mouseDragged(pMouseX, pMouseY, pButton, pDragX, pDragY);
    }

    @Override
    public void mouseMoved(final double pMouseX, final double pMouseY) {
        contentPane.processMouseMoveInput(new MouseMoveEvent(contentPane, (int) Math.round(pMouseX), (int) Math.round(pMouseY), MouseMoveEvent.EventType.Move));
        super.mouseMoved(pMouseX, pMouseY);
    }

    protected boolean isInventoryKeybind(final int key, final int scanCode) {
        return Minecraft.getInstance().options.keyInventory.isActiveAndMatches(InputConstants.getKey(key, scanCode));
    }
}
