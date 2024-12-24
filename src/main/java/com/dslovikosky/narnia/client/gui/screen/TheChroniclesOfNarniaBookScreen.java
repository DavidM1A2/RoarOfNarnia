package com.dslovikosky.narnia.client.gui.screen;

import com.dslovikosky.narnia.client.gui.control.ButtonPane;
import com.dslovikosky.narnia.client.gui.control.ImagePane;
import com.dslovikosky.narnia.client.gui.control.LabelComponent;
import com.dslovikosky.narnia.client.gui.control.StackPane;
import com.dslovikosky.narnia.client.gui.font.TtfFontLoader;
import com.dslovikosky.narnia.client.gui.layout.Dimensions;
import com.dslovikosky.narnia.client.gui.layout.Gravity;
import com.dslovikosky.narnia.client.gui.layout.Position;
import com.dslovikosky.narnia.client.gui.layout.TextAlignment;
import com.dslovikosky.narnia.common.constants.Constants;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class TheChroniclesOfNarniaBookScreen extends BaseScreen {
    private final ButtonPane forwardButton;
    private final ButtonPane backwardButton;

    public TheChroniclesOfNarniaBookScreen() {
        super(Component.literal("The Chronicles of Narnia"));

        final StackPane backgroundPane = new StackPane();
        backgroundPane.setPrefSize(new Dimensions(256, 256, false));
        backgroundPane.setGravity(Gravity.CENTER);

        // Add a background image to the background panel
        final ImagePane backgroundImage = new ImagePane(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "textures/gui/narnia_book/background.png"), ImagePane.DisplayMode.FIT_TO_PARENT);
        backgroundImage.setGravity(Gravity.CENTER);
        backgroundPane.add(backgroundImage);

        final LabelComponent labelTest = new LabelComponent(TtfFontLoader.createFont(12, false), "This is a test");
        labelTest.setPrefSize(new Dimensions(160, 45, false));
        labelTest.setTextAlignment(TextAlignment.ALIGN_RIGHT);
        backgroundImage.add(labelTest);

        forwardButton = new ButtonPane(
                new ImagePane(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "textures/gui/narnia_book/forward_button.png"), ImagePane.DisplayMode.FIT_TO_PARENT),
                new ImagePane(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "textures/gui/narnia_book/forward_button_hovered.png"), ImagePane.DisplayMode.FIT_TO_PARENT),
                null);
        forwardButton.setPrefSize(new Dimensions(10.0, 10.0, false));
        forwardButton.setOffset(new Position(-0.05, -0.12, true));
        forwardButton.setGravity(Gravity.BOTTOM_RIGHT);
        backwardButton = new ButtonPane(
                new ImagePane(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "textures/gui/narnia_book/backward_button.png"), ImagePane.DisplayMode.FIT_TO_PARENT),
                new ImagePane(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "textures/gui/narnia_book/backward_button_hovered.png"), ImagePane.DisplayMode.FIT_TO_PARENT),
                null);
        backwardButton.setPrefSize(new Dimensions(10.0, 10.0, false));
        backwardButton.setOffset(new Position(0.05, -0.12, true));
        backwardButton.setGravity(Gravity.BOTTOM_LEFT);
        backgroundImage.add(forwardButton);
        backgroundImage.add(backwardButton);

        contentPane.add(backgroundPane);
    }
}
