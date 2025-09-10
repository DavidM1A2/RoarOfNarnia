package com.dslovikosky.narnia.client.gui.layer;

import com.dslovikosky.narnia.client.gui.control.StackPane;
import com.dslovikosky.narnia.client.gui.layout.Dimensions;
import com.dslovikosky.narnia.client.gui.layout.GuiUtility;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphics;
import net.neoforged.neoforge.client.gui.GuiLayer;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public abstract class BaseGuiLayer implements GuiLayer {
    protected final StackPane contentPane;
    private boolean isLayerValid;
    private boolean isInitialized;

    public BaseGuiLayer() {
        contentPane = new StackPane();
        contentPane.setPrefSize(GuiUtility.getWindowSizeInMCCoords());
        isLayerValid = false;
        isInitialized = false;
    }

    public void update() {
        final Dimensions windowSize = GuiUtility.getWindowSizeInMCCoords();
        // Record dimensions so we can tell when they change
        this.contentPane.setPrefSize(windowSize);
        // Fit panes to the screen
        this.contentPane.negotiateDimensions(windowSize.width(), windowSize.height());
        // Resize any children to fit the new dimensions
        this.contentPane.update();
        isLayerValid = true;
    }

    @Override
    public void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
        // Do this because the layer gets initialized too early
        if (!isInitialized) {
            isLayerValid = false;
            initialize();
            isInitialized = true;
        }

        tick();

        // Trigger an update if the screen has changed size
        if (!contentPane.getPrefSize().equals(GuiUtility.getWindowSizeInMCCoords())) {
            isLayerValid = false;
        }
        // Perform an update if necessary
        if (!isLayerValid) {
            this.update();
        }
        // Draw the content pane
        this.contentPane.draw(guiGraphics);
        // Draw the overlay on top of the content pane
        this.contentPane.drawOverlay(guiGraphics);
//        RenderSystem.disableBlend();
    }

    public abstract void initialize();

    public void tick() {
    }
}
