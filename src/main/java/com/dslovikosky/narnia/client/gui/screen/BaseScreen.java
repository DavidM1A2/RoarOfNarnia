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
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.CharacterEvent;
import net.minecraft.client.input.MouseButtonEvent;
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
    }

    public boolean drawGradientBackground() {
        return true;
    }

    @Override
    public void renderBackground(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        // If we want a gradient background draw that background
        if (drawGradientBackground()) {
            super.renderBackground(guiGraphics, mouseX, mouseY, partialTick);
        }
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public boolean charTyped(final CharacterEvent characterEvent) {
        // MIN_VALUE default since we don't know what int the char corresponds to
        this.contentPane.processKeyInput(new KeyEvent(this.contentPane, Integer.MIN_VALUE, Integer.MIN_VALUE, characterEvent.codepoint(), characterEvent.modifiers(), KeyEvent.KeyEventType.Type));
        return super.charTyped(characterEvent);
    }

    @Override
    public boolean keyPressed(final net.minecraft.client.input.KeyEvent keyEvent) {
        this.contentPane.processKeyInput(new KeyEvent(this.contentPane, keyEvent.key(), keyEvent.scancode(), Character.MIN_VALUE, keyEvent.modifiers(), KeyEvent.KeyEventType.Press));
        if (super.keyPressed(keyEvent)) {
            return true;
        }
        // If our inventory key closes the screen, test if that key was pressed
        if (this.inventoryToCloseGuiScreen()) {
            // if the keycode is the inventory key bind close the GUI screen
            if (isInventoryKeybind(keyEvent)) {
                // Close the screen
                onClose();
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean keyReleased(final net.minecraft.client.input.KeyEvent keyEvent) {
        final boolean result = super.keyReleased(keyEvent);
        this.contentPane.processKeyInput(new KeyEvent(this.contentPane, keyEvent.key(), keyEvent.scancode(), Character.MIN_VALUE, keyEvent.modifiers(), KeyEvent.KeyEventType.Release));
        return result;
    }

    protected boolean inventoryToCloseGuiScreen() {
        return true;
    }

    @Override
    public boolean mouseClicked(final MouseButtonEvent event, final boolean isDoubleClick) {
        // Fire the mouse clicked event
        contentPane.processMouseInput(new MouseEvent(contentPane, (int) Math.round(event.x()), (int) Math.round(event.y()), event.button(), MouseEvent.EventType.Click));
        return super.mouseClicked(event, isDoubleClick);
    }

    @Override
    public boolean mouseReleased(final MouseButtonEvent event) {
        // Fire the release event
        contentPane.processMouseInput(new MouseEvent(contentPane, (int) Math.round(event.x()), (int) Math.round(event.y()), event.button(), MouseEvent.EventType.Release));
        return super.mouseReleased(event);
    }

    @Override
    public boolean mouseScrolled(final double pMouseX, final double pMouseY, final double pScrollX, final double pScrollY) {
        // Fire the content pane's mouse scroll listener
        contentPane.processMouseScrollInput(new MouseScrollEvent(contentPane, (int) Math.round(pScrollY)));
        return super.mouseScrolled(pMouseX, pMouseY, pScrollX, pScrollY);
    }

    @Override
    public boolean mouseDragged(final MouseButtonEvent event, final double mouseX, final double mouseY) {
        contentPane.processMouseDragInput(new MouseDragEvent(contentPane, (int) Math.round(mouseX), (int) Math.round(mouseY), event.button()));
        return super.mouseDragged(event, mouseX, mouseY);
    }

    @Override
    public void mouseMoved(final double pMouseX, final double pMouseY) {
        contentPane.processMouseMoveInput(new MouseMoveEvent(contentPane, (int) Math.round(pMouseX), (int) Math.round(pMouseY), MouseMoveEvent.EventType.Move));
        super.mouseMoved(pMouseX, pMouseY);
    }

    protected boolean isInventoryKeybind(final net.minecraft.client.input.KeyEvent keyEvent) {
        return Minecraft.getInstance().options.keyInventory.isActiveAndMatches(InputConstants.getKey(keyEvent));
    }
}
