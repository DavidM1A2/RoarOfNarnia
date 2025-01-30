package com.dslovikosky.narnia.client.gui.screen;

import com.dslovikosky.narnia.client.gui.control.HChainPane;
import com.dslovikosky.narnia.client.gui.control.ImagePane;
import com.dslovikosky.narnia.client.gui.control.LabelComponent;
import com.dslovikosky.narnia.client.gui.control.StackPane;
import com.dslovikosky.narnia.client.gui.control.VChainPane;
import com.dslovikosky.narnia.client.gui.font.TtfFontLoader;
import com.dslovikosky.narnia.client.gui.layout.ChainLayout;
import com.dslovikosky.narnia.client.gui.layout.Dimensions;
import com.dslovikosky.narnia.client.gui.layout.Gravity;
import com.dslovikosky.narnia.client.gui.layout.Spacing;
import com.dslovikosky.narnia.common.constants.Constants;
import com.dslovikosky.narnia.common.network.packet.SetPositionalMarkerDataPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;

import java.awt.Color;

public class EditPositionalMarkerScreen extends BaseScreen {
    private final BlockPos pos;
    private final LabelComponent nameTextField;
    private final LabelComponent xOffsetField;
    private final LabelComponent yOffsetField;
    private final LabelComponent zOffsetField;

    public EditPositionalMarkerScreen(final BlockPos pos, final String initialName, final Vec3 initialOffset) {
        super(Component.translatable("screen.narnia.edit_positional_marker"));
        this.pos = pos;

        final StackPane backgroundPane = new StackPane();
        backgroundPane.setPrefSize(new Dimensions(256, 256, false));
        backgroundPane.setGravity(Gravity.CENTER);

        final ImagePane backgroundImage = new ImagePane(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "textures/gui/edit_positional_marker/background.png"), ImagePane.DisplayMode.FIT_TO_TEXTURE);
        backgroundImage.setPrefSize(new Dimensions(256, 100, false));
        backgroundImage.setGravity(Gravity.CENTER);
        backgroundPane.add(backgroundImage);

        final VChainPane rowsChain = new VChainPane(ChainLayout.SPREAD);
        rowsChain.setMargins(new Spacing(0.15, 0.15, 0.05, 0.05, true));
        rowsChain.setGravity(Gravity.CENTER);

        final HChainPane nameChain = new HChainPane(ChainLayout.SPREAD);
        nameChain.setPrefSize(new Dimensions(256, 25, false));
        final LabelComponent nameLabel = new LabelComponent(TtfFontLoader.getTextFont(50f, true), "Name: ");
        nameLabel.setTextColor(Color.WHITE);
        nameLabel.setPrefSize(new Dimensions(0.2, 1, true));
        nameTextField = new LabelComponent(TtfFontLoader.getTextFont(50f, true), initialName);
        nameTextField.setPrefSize(new Dimensions(0.8, 1, true));
        nameChain.add(nameLabel);
        nameChain.add(nameTextField);

        final HChainPane xOffsetChain = new HChainPane(ChainLayout.SPREAD);
        xOffsetChain.setPrefSize(new Dimensions(256, 25, false));
        final LabelComponent xOffsetLabel = new LabelComponent(TtfFontLoader.getTextFont(50f, true), "X Offset: ");
        xOffsetLabel.setTextColor(Color.WHITE);
        xOffsetLabel.setPrefSize(new Dimensions(0.2, 1, true));
        xOffsetField = new LabelComponent(TtfFontLoader.getTextFont(50f, true), Double.toString(initialOffset.x()));
        xOffsetField.setPrefSize(new Dimensions(0.8, 1, true));
        xOffsetChain.add(xOffsetLabel);
        xOffsetChain.add(xOffsetField);

        final HChainPane yOffsetChain = new HChainPane(ChainLayout.SPREAD);
        yOffsetChain.setPrefSize(new Dimensions(256, 25, false));
        final LabelComponent yOffsetLabel = new LabelComponent(TtfFontLoader.getTextFont(50f, true), "Y Offset: ");
        yOffsetLabel.setTextColor(Color.WHITE);
        yOffsetLabel.setPrefSize(new Dimensions(0.2, 1, true));
        yOffsetField = new LabelComponent(TtfFontLoader.getTextFont(50f, true), Double.toString(initialOffset.y()));
        yOffsetField.setPrefSize(new Dimensions(0.8, 1, true));
        yOffsetChain.add(yOffsetLabel);
        yOffsetChain.add(yOffsetField);

        final HChainPane zOffsetChain = new HChainPane(ChainLayout.SPREAD);
        zOffsetChain.setPrefSize(new Dimensions(256, 25, false));
        final LabelComponent zOffsetLabel = new LabelComponent(TtfFontLoader.getTextFont(50f, true), "Z Offset: ");
        zOffsetLabel.setTextColor(Color.WHITE);
        zOffsetLabel.setPrefSize(new Dimensions(0.2, 1, true));
        zOffsetField = new LabelComponent(TtfFontLoader.getTextFont(50f, true), Double.toString(initialOffset.z()));
        zOffsetField.setPrefSize(new Dimensions(0.8, 1, true));
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
        final Vec3 offset = new Vec3(Double.parseDouble(xOffsetField.getText()), Double.parseDouble(yOffsetField.getText()), Double.parseDouble(zOffsetField.getText()));
        PacketDistributor.sendToServer(new SetPositionalMarkerDataPacket(pos, nameTextField.getText(), offset));
        super.onClose();
    }
}
