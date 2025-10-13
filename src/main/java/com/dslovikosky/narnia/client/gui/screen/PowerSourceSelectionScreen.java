package com.dslovikosky.narnia.client.gui.screen;

import com.dslovikosky.narnia.client.gui.control.ImagePane;
import com.dslovikosky.narnia.client.gui.control.LabelComponent;
import com.dslovikosky.narnia.client.gui.control.RadialPane;
import com.dslovikosky.narnia.client.gui.control.SpritePane;
import com.dslovikosky.narnia.client.gui.control.StackPane;
import com.dslovikosky.narnia.client.gui.event.KeyEvent;
import com.dslovikosky.narnia.client.gui.event.MouseEvent;
import com.dslovikosky.narnia.client.gui.font.TtfFontLoader;
import com.dslovikosky.narnia.client.gui.layout.Dimensions;
import com.dslovikosky.narnia.client.gui.layout.Gravity;
import com.dslovikosky.narnia.client.gui.layout.GuiUtility;
import com.dslovikosky.narnia.client.gui.layout.Position;
import com.dslovikosky.narnia.client.gui.layout.Spacing;
import com.dslovikosky.narnia.client.gui.layout.TextAlignment;
import com.dslovikosky.narnia.common.constants.Constants;
import com.dslovikosky.narnia.common.constants.ModKeyMappings;
import com.dslovikosky.narnia.common.constants.ModRegistries;
import com.dslovikosky.narnia.common.constants.ModSpellPowerSources;
import com.dslovikosky.narnia.common.spell.component.powerSource.base.CastEnvironment;
import com.dslovikosky.narnia.common.spell.component.powerSource.base.SpellPowerSource;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

public class PowerSourceSelectionScreen extends BaseScreen {
    private static final int POWER_SOURCES_PER_PAGE = 8;
    private static final double MOUSE_BOUNDS_SIZE = 0.5;
    private static final double MOUSE_DEADZONE_SIZE = 0.1;
    private static int currentPageIndex = 0;

    private final List<StackPane> powerSourcePanes = new ArrayList<>();
    private final List<ImagePane> selectionIcons = new ArrayList<>();
    private final SpellPowerSource<?> previousSpellPowerSource = ModSpellPowerSources.CREATIVE.get();
    private final List<SpellPowerSource<?>> availableSpellPowerSources = ModRegistries.SPELL_POWER_SOURCES.stream().toList();
    private final int pageCount = (availableSpellPowerSources.size() - 1) / POWER_SOURCES_PER_PAGE + 1;
    private SpellPowerSource<?> selectedSpellPowerSource = null;
    private RadialPane radialMenuPane = null;

    public PowerSourceSelectionScreen() {
        super(Component.translatable("screen.narnia.power_source_selection"));

        // Close the screen when TOGGLE_POWER_SOURCE_SELECTOR is released. It must be pressed to open this screen
        this.contentPane.addKeyListener(keyEvent -> {
            if (keyEvent.getEventType() == KeyEvent.KeyEventType.Release && keyEvent.getKey() == ModKeyMappings.POWER_SOURCE_SELECTOR.getKey().getValue()) {
                onClose();
            }
        });

        // Left click also closes the gui
        this.contentPane.addMouseListener(mouseEvent -> {
            if (mouseEvent.getClickedButton() == MouseEvent.LEFT_MOUSE_BUTTON) {
                onClose();
            }
        });

        // Highlight selection based on mouse movement
        this.contentPane.addMouseMoveListener(mouseEvent -> {
            if (radialMenuPane != null) {
                final int x = mouseEvent.getMouseX();
                final int y = mouseEvent.getMouseY();
                final int offset = currentPageIndex * POWER_SOURCES_PER_PAGE;

                // Math stuff
                final int radiusSquared = x * x + y * y;
                final double radiusAbsoluteMin = MOUSE_DEADZONE_SIZE * radialMenuPane.getWidth() / 2;
                final double radiusMinSquared = radiusAbsoluteMin * radiusAbsoluteMin;
                if (radiusSquared > radiusMinSquared) {
                    final double radiusAbsoluteMax = MOUSE_BOUNDS_SIZE * radialMenuPane.getWidth() / 2;
                    final double radiusMaxSquared = radiusAbsoluteMax * radiusAbsoluteMax;
                    final double theta = Mth.positiveModulo(Math.atan2(y, x), 2 * Math.PI);  // From -PI to PI
                    final int sectionIndex = (int) Mth.positiveModulo((theta + (Math.PI / POWER_SOURCES_PER_PAGE)) / (2 * Math.PI) * POWER_SOURCES_PER_PAGE, POWER_SOURCES_PER_PAGE);
                    // Make the hovered option larger
                    powerSourcePanes.forEach(pane -> pane.setPrefSize(new Dimensions(0.13, 0.13, true)));
                    powerSourcePanes.get(sectionIndex).setPrefSize(new Dimensions(0.15, 0.15, true));
                    // Set the selected power source client-side
                    if (sectionIndex < POWER_SOURCES_PER_PAGE) {
                        selectedSpellPowerSource = sectionIndex + offset < availableSpellPowerSources.size() ? availableSpellPowerSources.get(sectionIndex + offset) : null;
                    }
                    // Redraw the pane, since elements have changed
                    this.contentPane.invalidate();
                    // Bound Cursor
                    if (radiusSquared > radiusMaxSquared) {
                        GLFW.glfwSetCursorPos(minecraft.getWindow().getWindow(), Math.cos(theta) * radiusAbsoluteMax, Math.sin(theta) * radiusAbsoluteMax);
                    }
                }
            }
        });

        // Right click changes page
        this.contentPane.addMouseListener(mouseEvent -> {
            if (mouseEvent.getClickedButton() == MouseEvent.RIGHT_MOUSE_BUTTON && mouseEvent.getEventType() == MouseEvent.EventType.Click) {
                currentPageIndex = (currentPageIndex + 1) % pageCount;
                populateMenuWithOffset(POWER_SOURCES_PER_PAGE * currentPageIndex);
            }
        });

        currentPageIndex = Math.max(0, Math.min(currentPageIndex, pageCount - 1));
        populateMenuWithOffset(POWER_SOURCES_PER_PAGE * currentPageIndex);
    }

    private void populateMenuWithOffset(final int offset) {
        // Clear the pane
        this.contentPane.getChildren().forEach(contentPane::remove);

        // Set up the main GUI elements
        this.radialMenuPane = new RadialPane();
        radialMenuPane.setGravity(Gravity.CENTER);
        final ImagePane bkgPane = new ImagePane(Constants.modLocation("textures/gui/power_source_selector/background.png"), ImagePane.DisplayMode.FIT_TO_PARENT);
        bkgPane.setGravity(Gravity.CENTER);
        this.contentPane.add(bkgPane);
        this.contentPane.add(radialMenuPane);

        // Add the page count
        final LabelComponent pageLabel = new LabelComponent(TtfFontLoader.getTextFont(42f, true), "Page " + (currentPageIndex + 1) + "/" + pageCount + " (RMB)");
        pageLabel.setPrefSize(new Dimensions(0.5, 0.5, true));
        pageLabel.setGravity(Gravity.CENTER);
        pageLabel.setTextAlignment(TextAlignment.ALIGN_CENTER);
        this.contentPane.add(pageLabel);

        // Fill the radial menu with power sources
        powerSourcePanes.clear();
        selectionIcons.clear();
        for (int i = 0; i < POWER_SOURCES_PER_PAGE; i++) {
            final SpritePane liquidSprite = new SpritePane(Constants.modLocation("textures/gui/power_source_selector/liquid_spritesheet.png"), ImagePane.DisplayMode.STRETCH, 4, 4);
            liquidSprite.setFrame(12);

            final ImagePane orbImage = new ImagePane(Constants.modLocation("textures/gui/power_source_selector/orb_front_colored.png"), ImagePane.DisplayMode.STRETCH);
            final StackPane buttonPane = new StackPane();
            buttonPane.setGravity(Gravity.CENTER);
            buttonPane.setPrefSize(new Dimensions(0.13, 0.13, true));
            buttonPane.setOffset(new Position(0.5, (double) i / POWER_SOURCES_PER_PAGE, true));
            final ImagePane selectorImage = new ImagePane(Constants.modLocation("textures/gui/power_source_selector/orb_selector.png"), ImagePane.DisplayMode.STRETCH);
            selectorImage.setVisible(false);
            buttonPane.add(liquidSprite);
            selectionIcons.add(selectorImage);
            radialMenuPane.add(buttonPane);
            powerSourcePanes.add(buttonPane);

            // Add number descriptor
            final LabelComponent numberPane = new LabelComponent(TtfFontLoader.getTextFont(32f, true), "N/A");
            numberPane.setPrefSize(new Dimensions(1.0, 0.3, true));
            numberPane.setGravity(Gravity.TOP_CENTER);
            numberPane.setOffset(new Position(0.0, 0.95, true));
            numberPane.setTextAlignment(TextAlignment.ALIGN_CENTER);

            // Only fill out the gui while there are still available power sources
            if (i + offset < availableSpellPowerSources.size()) {
                final ImagePane ssIcon = new ImagePane(availableSpellPowerSources.get(i + offset).getIcon(), ImagePane.DisplayMode.STRETCH);
                ssIcon.setMargins(new Spacing(0.4, true));
                buttonPane.add(ssIcon);
                final CastEnvironment<?> castEnvironment = availableSpellPowerSources.get(i + offset).computeCastEnvironment(Minecraft.getInstance().player);
                numberPane.setText(castEnvironment.getVitaeMaximum() == Double.POSITIVE_INFINITY
                        ? String.format("%.1f", castEnvironment.getVitaeAvailable())
                        : String.format("%.1f/%.1f", castEnvironment.getVitaeAvailable(), castEnvironment.getVitaeMaximum()));
                if (castEnvironment.getVitaeMaximum() == 0.0 || castEnvironment.getVitaeAvailable() == 0.0) { // Zero Case
                    liquidSprite.setFrame(12);
                } else if (castEnvironment.getVitaeAvailable() == Double.POSITIVE_INFINITY || castEnvironment.getVitaeMaximum() == Double.POSITIVE_INFINITY || castEnvironment.getVitaeAvailable() / castEnvironment.getVitaeMaximum() > 0.75) {  // Full case
                    liquidSprite.setAnimation(List.of(0, 1, 2, 3), SpritePane.AnimationMode.LOOP, 4.0);
                } else if (castEnvironment.getVitaeAvailable() / castEnvironment.getVitaeMaximum() > 0.5) {
                    liquidSprite.setAnimation(List.of(4, 5, 6, 7), SpritePane.AnimationMode.LOOP, 4.0);
                } else if (castEnvironment.getVitaeAvailable() / castEnvironment.getVitaeMaximum() > 0.0) {
                    liquidSprite.setAnimation(List.of(8, 9, 10, 11), SpritePane.AnimationMode.LOOP, 4.0);
                }
            }
            buttonPane.add(orbImage);
            buttonPane.add(selectorImage);
            buttonPane.add(numberPane);
        }

        // Start with the current power source selected
        final int selectedIndex = availableSpellPowerSources.indexOf(previousSpellPowerSource);
        if (selectedIndex >= offset && selectedIndex <= POWER_SOURCES_PER_PAGE + offset) {
            selectionIcons.get(selectedIndex - offset).setVisible(true);
        }

        // Draw new menu
        this.contentPane.invalidate();
    }

    @Override
    protected void init() {
        InputConstants.grabOrReleaseMouse(minecraft.getWindow().getWindow(), GLFW.GLFW_CURSOR_DISABLED, 0.0, 0.0);
        super.init();
    }

    @Override
    public boolean drawGradientBackground() {
        return false;
    }

    @Override
    protected boolean inventoryToCloseGuiScreen() {
        return true;
    }

    @Override
    public void onClose() {
        InputConstants.grabOrReleaseMouse(minecraft.getWindow().getWindow(), GLFW.GLFW_CURSOR_NORMAL, GuiUtility.getWindowWidthInMCCoords() / 2.0, GuiUtility.getWindowHeightInMCCoords() / 2.0);
        super.onClose();
    }
}
