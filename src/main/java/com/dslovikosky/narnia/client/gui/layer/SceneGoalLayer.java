package com.dslovikosky.narnia.client.gui.layer;

import com.dslovikosky.narnia.client.gui.control.ImagePane;
import com.dslovikosky.narnia.client.gui.control.TextBoxComponent;
import com.dslovikosky.narnia.client.gui.font.TtfFontLoader;
import com.dslovikosky.narnia.client.gui.layout.Dimensions;
import com.dslovikosky.narnia.client.gui.layout.Gravity;
import com.dslovikosky.narnia.client.gui.layout.Spacing;
import com.dslovikosky.narnia.client.gui.layout.TextAlignment;
import com.dslovikosky.narnia.common.constants.Constants;
import com.dslovikosky.narnia.common.model.NarniaGlobalData;
import com.dslovikosky.narnia.common.model.scene.Chapter;
import com.dslovikosky.narnia.common.model.scene.Scene;
import com.dslovikosky.narnia.common.model.scene.SceneState;
import com.dslovikosky.narnia.common.model.scene.goal.ChapterGoal;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Optional;

@ParametersAreNonnullByDefault
public class SceneGoalLayer extends BaseLayer {
    private TextBoxComponent questBox;

    @Override
    public void initialize() {
        final ImagePane backgroundImage = new ImagePane(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "textures/gui/goal_layer/background.png"), ImagePane.DisplayMode.STRETCH);
        backgroundImage.setGravity(Gravity.TOP_CENTER);
        backgroundImage.setMargins(new Spacing(5.0, false));
        backgroundImage.setPrefSize(new Dimensions(100, 35, false));

        questBox = new TextBoxComponent(TtfFontLoader.getTextFont(30f, true), "");
        questBox.setTextAlignment(TextAlignment.ALIGN_CENTER);
        questBox.setMargins(new Spacing(5.0, false));
        backgroundImage.add(questBox);

        contentPane.add(backgroundImage);
    }

    @Override
    public void tick() {
        final Level level = Minecraft.getInstance().level;
        if (level == null) {
            contentPane.setVisible(false);
            return;
        }

        final NarniaGlobalData data = NarniaGlobalData.getInstance(level);
        final Scene activeScene = data.getActiveScene();

        if (activeScene == null) {
            contentPane.setVisible(false);
            return;
        }

        if (activeScene.getState() == SceneState.NEW) {
            contentPane.setVisible(false);
            return;
        }

        final Chapter chapter = activeScene.getChapter();
        if (!chapter.isParticipating(activeScene, Minecraft.getInstance().player)) {
            contentPane.setVisible(false);
            return;
        }

        final Optional<ChapterGoal> currentGoalOpt = chapter.getCurrentGoal(activeScene);
        if (currentGoalOpt.isEmpty()) {
            contentPane.setVisible(false);
            return;
        }
        final ChapterGoal chapterGoal = currentGoalOpt.get();

        final String description = chapterGoal.getDescription(activeScene, level).getString();
        if (!questBox.getText().equals(description)) {
            questBox.setText(description);
        }
        contentPane.setVisible(true);
    }
}
