package com.dslovikosky.narnia.common.chapter;

import com.dslovikosky.narnia.common.chapter.goal.InstantiateAtUncleAndrewsHouseGoal;
import com.dslovikosky.narnia.common.chapter.goal.LocateUncleAndrewsHouseGoal;
import com.dslovikosky.narnia.common.constants.ModBooks;
import com.dslovikosky.narnia.common.constants.ModCharacters;
import com.dslovikosky.narnia.common.model.scene.Chapter;
import com.dslovikosky.narnia.common.model.scene.goal.ChatLine;
import com.dslovikosky.narnia.common.model.scene.goal.ConversationChapterGoal;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class TheWrongDoorChapter extends Chapter {
    public TheWrongDoorChapter(final ResourceLocation id) {
        super(id, ModBooks.THE_MAGICIANS_NEPHEW, List.of(ModCharacters.DIGORY, ModCharacters.POLLY));
        addGoal(new LocateUncleAndrewsHouseGoal());
        addGoal(new InstantiateAtUncleAndrewsHouseGoal(ModCharacters.DIGORY, new Vec3(10, 1, 20), Vec3.directionFromRotation(0f, 270f)));
        addGoal(new InstantiateAtUncleAndrewsHouseGoal(ModCharacters.POLLY, new Vec3(16, 1, 20), Vec3.directionFromRotation(0f, 90f)));
        addGoal(new ConversationChapterGoal(Component.literal("Digory meets Polly"),
                new ChatLine(ModCharacters.POLLY::get, ModCharacters.DIGORY::get, Component.literal("Hi")),
                new ChatLine(ModCharacters.DIGORY::get, ModCharacters.POLLY::get, Component.literal("Hey")),
                new ChatLine(ModCharacters.POLLY::get, ModCharacters.DIGORY::get, Component.literal("asdf")),
                new ChatLine(ModCharacters.DIGORY::get, ModCharacters.POLLY::get, Component.literal("ghjk"))));
    }
}
