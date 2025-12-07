package com.dslovikosky.narnia.client.gui.screen;

import com.dslovikosky.narnia.client.gui.control.ImagePane;
import com.dslovikosky.narnia.client.gui.control.StackPane;
import com.dslovikosky.narnia.client.gui.control.TextFieldPane;
import com.dslovikosky.narnia.client.gui.font.TtfFontLoader;
import com.dslovikosky.narnia.client.gui.layout.Gravity;
import com.dslovikosky.narnia.common.constants.Constants;
import net.minecraft.network.chat.Component;

import java.awt.*;

public class SpellwrightingScreen extends BaseScreen {
    private final ImagePane backgroundPane;
    private final TextFieldPane textFieldPane;

    public SpellwrightingScreen() {
        super(Component.translatable("screen.narnia.spellwrighting"));

        this.backgroundPane = new ImagePane(Constants.modLocation("textures/gui/narnia_book/background.png"), ImagePane.DisplayMode.FIT_TO_PARENT);
        this.backgroundPane.setGravity(Gravity.CENTER);

        this.textFieldPane = new TextFieldPane(TtfFontLoader.getTextFont(64.0f, true), new StackPane(), true);
        this.textFieldPane.setTextColor(Color.BLACK);

        this.backgroundPane.add(this.textFieldPane);
        this.contentPane.add(this.backgroundPane);
    }

    @Override
    protected boolean inventoryToCloseGuiScreen() {
        return false;
    }
}
