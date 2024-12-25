package com.dslovikosky.narnia.client.gui.screen;

import com.dslovikosky.narnia.client.gui.control.ButtonPane;
import com.dslovikosky.narnia.client.gui.control.HChainPane;
import com.dslovikosky.narnia.client.gui.control.ImagePane;
import com.dslovikosky.narnia.client.gui.control.LabelComponent;
import com.dslovikosky.narnia.client.gui.control.StackPane;
import com.dslovikosky.narnia.client.gui.control.TextBoxComponent;
import com.dslovikosky.narnia.client.gui.control.VChainPane;
import com.dslovikosky.narnia.client.gui.font.TtfFontLoader;
import com.dslovikosky.narnia.client.gui.layout.ChainLayout;
import com.dslovikosky.narnia.client.gui.layout.Dimensions;
import com.dslovikosky.narnia.client.gui.layout.Gravity;
import com.dslovikosky.narnia.client.gui.layout.Position;
import com.dslovikosky.narnia.client.gui.layout.TextAlignment;
import com.dslovikosky.narnia.common.constants.Constants;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.awt.Color;

public class TheChroniclesOfNarniaBookScreen extends BaseScreen {
    private final ButtonPane forwardButton;
    private final ButtonPane backwardButton;

    public TheChroniclesOfNarniaBookScreen() {
        super(Component.literal("The Chronicles of Narnia"));

        final StackPane backgroundPane = new StackPane();
        backgroundPane.setPrefSize(new Dimensions(256, 256, false));
        backgroundPane.setGravity(Gravity.CENTER);

        final ImagePane backgroundImage = new ImagePane(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "textures/gui/narnia_book/background.png"), ImagePane.DisplayMode.FIT_TO_PARENT);
        backgroundImage.setGravity(Gravity.CENTER);
        backgroundPane.add(backgroundImage);

        final LabelComponent titleLabel = new LabelComponent(TtfFontLoader.getNarniaFont(40f), "The Wrong Door");
        titleLabel.setTextAlignment(TextAlignment.ALIGN_CENTER);
        titleLabel.setOffset(new Position(0.04, 0.1, true));
        titleLabel.setPrefSize(new Dimensions(0.5 - 2 * 0.04, 0.1, true));
        titleLabel.setTextColor(new Color(16, 140, 0));
        backgroundPane.add(titleLabel);

        final TextBoxComponent textBoxComponent = new TextBoxComponent(TtfFontLoader.getTextFont(26f, true), "    This is a test. It is a very long string that should be wrapped to multiple lines.\n".repeat(50));
        textBoxComponent.setTextAlignment(TextAlignment.ALIGN_CENTER);
        textBoxComponent.setOffset(new Position(0.04, 0.2, true));
        textBoxComponent.setPrefSize(new Dimensions(0.5 - 2 * 0.04, 0.56, true));
        textBoxComponent.setTextColor(new Color(6, 50, 0));
        backgroundPane.add(textBoxComponent);

        final HChainPane hChainPane = new HChainPane(ChainLayout.SPREAD);
        hChainPane.setPrefSize(new Dimensions(1.0, 0.1, true));
        final ImagePane imagePaneH1 = new ImagePane(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "textures/gui/narnia_book/forward_button.png"), ImagePane.DisplayMode.FIT_TO_PARENT);
        final ImagePane imagePaneH2 = new ImagePane(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "textures/gui/narnia_book/forward_button.png"), ImagePane.DisplayMode.FIT_TO_PARENT);
        final ImagePane imagePaneH3 = new ImagePane(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "textures/gui/narnia_book/forward_button.png"), ImagePane.DisplayMode.FIT_TO_PARENT);
        imagePaneH1.setPrefSize(new Dimensions(0.2, 1.0, true));
        imagePaneH2.setPrefSize(new Dimensions(0.2, 1.0, true));
        imagePaneH3.setPrefSize(new Dimensions(0.2, 1.0, true));
        hChainPane.add(imagePaneH1);
        hChainPane.add(imagePaneH2);
        hChainPane.add(imagePaneH3);
        backgroundPane.add(hChainPane);

        final VChainPane vChainPane = new VChainPane(ChainLayout.SPREAD);
        vChainPane.setPrefSize(new Dimensions(0.1, 1.0, true));
        final ImagePane imagePaneV1 = new ImagePane(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "textures/gui/narnia_book/forward_button.png"), ImagePane.DisplayMode.FIT_TO_PARENT);
        final ImagePane imagePaneV2 = new ImagePane(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "textures/gui/narnia_book/forward_button.png"), ImagePane.DisplayMode.FIT_TO_PARENT);
        final ImagePane imagePaneV3 = new ImagePane(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "textures/gui/narnia_book/forward_button.png"), ImagePane.DisplayMode.FIT_TO_PARENT);
        imagePaneV1.setPrefSize(new Dimensions(1.0, 0.2, true));
        imagePaneV2.setPrefSize(new Dimensions(1.0, 0.2, true));
        imagePaneV3.setPrefSize(new Dimensions(1.0, 0.2, true));
        vChainPane.add(imagePaneV1);
        vChainPane.add(imagePaneV2);
        vChainPane.add(imagePaneV3);
        backgroundPane.add(vChainPane);

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
