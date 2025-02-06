package com.dslovikosky.narnia.client.gui.screen;

import com.dslovikosky.narnia.client.gui.control.HChainPane;
import com.dslovikosky.narnia.client.gui.control.ImagePane;
import com.dslovikosky.narnia.client.gui.control.LabelComponent;
import com.dslovikosky.narnia.client.gui.control.StackPane;
import com.dslovikosky.narnia.client.gui.control.TextFieldPane;
import com.dslovikosky.narnia.client.gui.control.VChainPane;
import com.dslovikosky.narnia.client.gui.font.TtfFontLoader;
import com.dslovikosky.narnia.client.gui.layout.ChainLayout;
import com.dslovikosky.narnia.client.gui.layout.Dimensions;
import com.dslovikosky.narnia.client.gui.layout.Gravity;
import com.dslovikosky.narnia.client.gui.layout.Spacing;
import com.dslovikosky.narnia.client.gui.layout.TextAlignment;
import com.dslovikosky.narnia.common.constants.Constants;
import com.dslovikosky.narnia.common.network.packet.SetPositionalMarkerDataPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;
import org.apache.commons.lang3.math.NumberUtils;

import java.awt.Color;

public class EditPositionalMarkerScreen extends BaseScreen {
    private final BlockPos pos;
    private final TextFieldPane nameTextField;
    private final TextFieldPane xOffsetField;
    private final TextFieldPane yOffsetField;
    private final TextFieldPane zOffsetField;

    public EditPositionalMarkerScreen(final BlockPos pos, final String initialName, final Vec3 initialOffset) {
        super(Component.translatable("screen.narnia.edit_positional_marker"));
        this.pos = pos;

        final StackPane backgroundPane = new StackPane();
        backgroundPane.setPrefSize(new Dimensions(256, 256, false));
        backgroundPane.setGravity(Gravity.CENTER);

        final ImagePane backgroundImage = new ImagePane(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "textures/gui/edit_positional_marker/background.png"), ImagePane.DisplayMode.STRETCH);
        backgroundImage.setPrefSize(new Dimensions(256, 120, false));
        backgroundImage.setGravity(Gravity.CENTER);
        backgroundPane.add(backgroundImage);

        final VChainPane rowsChain = new VChainPane(ChainLayout.SPREAD);
        rowsChain.setMargins(new Spacing(0.1, 0.1, 0.08, 0.08, true));
        rowsChain.setGravity(Gravity.CENTER);

        final HChainPane nameChain = new HChainPane(ChainLayout.SPREAD);
        nameChain.setPrefSize(new Dimensions(256, 25, false));
        final LabelComponent nameLabel = new LabelComponent(TtfFontLoader.getTextFont(50f, true), "Name:");
        nameLabel.setTextColor(Color.WHITE);
        nameLabel.setPrefSize(new Dimensions(56, 25, false));
        nameLabel.setTextAlignment(TextAlignment.ALIGN_LEFT);
        nameTextField = new TextFieldPane(TtfFontLoader.getTextFont(50f, true));
        nameTextField.setText(initialName);
        nameTextField.setPrefSize(new Dimensions(200, 25, false));
        nameChain.add(nameLabel);
        nameChain.add(nameTextField);

        final HChainPane xOffsetChain = new HChainPane(ChainLayout.SPREAD);
        xOffsetChain.setPrefSize(new Dimensions(256, 25, false));
        final LabelComponent xOffsetLabel = new LabelComponent(TtfFontLoader.getTextFont(50f, true), "X Offset: ");
        xOffsetLabel.setTextColor(Color.WHITE);
        xOffsetLabel.setPrefSize(new Dimensions(56, 25, false));
        xOffsetLabel.setTextAlignment(TextAlignment.ALIGN_LEFT);
        xOffsetField = new TextFieldPane(TtfFontLoader.getTextFont(50f, true));
        xOffsetField.setText(Double.toString(initialOffset.x()));
        xOffsetField.setPrefSize(new Dimensions(200, 25, false));
        xOffsetChain.add(xOffsetLabel);
        xOffsetChain.add(xOffsetField);

        final HChainPane yOffsetChain = new HChainPane(ChainLayout.SPREAD);
        yOffsetChain.setPrefSize(new Dimensions(256, 25, false));
        final LabelComponent yOffsetLabel = new LabelComponent(TtfFontLoader.getTextFont(50f, true), "Y Offset: ");
        yOffsetLabel.setTextColor(Color.WHITE);
        yOffsetLabel.setPrefSize(new Dimensions(56, 25, false));
        yOffsetLabel.setTextAlignment(TextAlignment.ALIGN_LEFT);
        yOffsetField = new TextFieldPane(TtfFontLoader.getTextFont(50f, true));
        yOffsetField.setText(Double.toString(initialOffset.y()));
        yOffsetField.setPrefSize(new Dimensions(200, 25, false));
        yOffsetChain.add(yOffsetLabel);
        yOffsetChain.add(yOffsetField);

        final HChainPane zOffsetChain = new HChainPane(ChainLayout.SPREAD);
        zOffsetChain.setPrefSize(new Dimensions(256, 25, false));
        final LabelComponent zOffsetLabel = new LabelComponent(TtfFontLoader.getTextFont(50f, true), "Z Offset: ");
        zOffsetLabel.setTextColor(Color.WHITE);
        zOffsetLabel.setPrefSize(new Dimensions(56, 25, false));
        zOffsetLabel.setTextAlignment(TextAlignment.ALIGN_LEFT);
        zOffsetField = new TextFieldPane(TtfFontLoader.getTextFont(50f, true));
        zOffsetField.setText(Double.toString(initialOffset.z()));
        zOffsetField.setPrefSize(new Dimensions(200, 25, false));
        zOffsetChain.add(zOffsetLabel);
        zOffsetChain.add(zOffsetField);

        rowsChain.add(nameChain);
        rowsChain.add(xOffsetChain);
        rowsChain.add(yOffsetChain);
        rowsChain.add(zOffsetChain);

        backgroundImage.add(rowsChain);

        contentPane.add(backgroundImage);
    }

    @Override
    public void onClose() {
        final Vec3 offset = new Vec3(NumberUtils.toDouble(xOffsetField.getText(), 0.0), NumberUtils.toDouble(yOffsetField.getText(), 0.0), NumberUtils.toDouble(zOffsetField.getText(), 0.0));
        PacketDistributor.sendToServer(new SetPositionalMarkerDataPacket(pos, nameTextField.getText(), offset));
        super.onClose();
    }

    @Override
    protected boolean inventoryToCloseGuiScreen() {
        return !nameTextField.isFocused() && !xOffsetField.isFocused() && !yOffsetField.isFocused() && !zOffsetField.isFocused();
    }
}
