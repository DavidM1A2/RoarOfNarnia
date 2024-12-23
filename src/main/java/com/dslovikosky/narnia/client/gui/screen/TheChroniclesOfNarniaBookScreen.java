package com.dslovikosky.narnia.client.gui.screen;

import com.dslovikosky.narnia.client.gui.control.ImagePane;
import com.dslovikosky.narnia.client.gui.control.StackPane;
import com.dslovikosky.narnia.client.gui.layout.Dimensions;
import com.dslovikosky.narnia.client.gui.layout.Gravity;
import com.dslovikosky.narnia.common.constants.Constants;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class TheChroniclesOfNarniaBookScreen extends BaseScreen {
    public TheChroniclesOfNarniaBookScreen() {
        super(Component.literal("The Chronicles of Narnia"));

        final StackPane backgroundPane = new StackPane();
        backgroundPane.setPrefSize(new Dimensions(512.0, 256.0, false));
        backgroundPane.setGravity(Gravity.CENTER);

        // Add a background image to the background panel
        final ImagePane backgroundImage = new ImagePane(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "textures/gui/narnia_book/background.png"), ImagePane.DisplayMode.FIT_TO_PARENT);
        backgroundImage.setGravity(Gravity.CENTER);
        backgroundPane.add(backgroundImage);

        contentPane.add(backgroundPane);
    }
}
