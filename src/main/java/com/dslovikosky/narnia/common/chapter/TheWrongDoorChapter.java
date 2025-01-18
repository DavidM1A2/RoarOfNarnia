package com.dslovikosky.narnia.common.chapter;

import com.dslovikosky.narnia.common.constants.ModBooks;
import com.dslovikosky.narnia.common.constants.ModCharacters;
import com.dslovikosky.narnia.common.model.scene.Chapter;
import com.dslovikosky.narnia.common.model.scene.goal.DigoryPollyIntroductionGoal;
import com.dslovikosky.narnia.common.model.scene.goal.FindUncleAndrewsHouseGoal;
import com.dslovikosky.narnia.common.model.scene.goal.base.ChatLine;
import com.dslovikosky.narnia.common.model.scene.goal.base.ConversationChapterGoal;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public class TheWrongDoorChapter extends Chapter {
    public TheWrongDoorChapter(final ResourceLocation id) {
        super(id, ModBooks.THE_MAGICIANS_NEPHEW, List.of(ModCharacters.DIGORY, ModCharacters.POLLY));
        addGoal(new FindUncleAndrewsHouseGoal());
        addGoal(new DigoryPollyIntroductionGoal());
        addGoal(new ConversationChapterGoal(Component.literal("Digory meets Polly"),
                new ChatLine(ModCharacters.POLLY::get, Component.literal("Hi"), 30),
                new ChatLine(ModCharacters.DIGORY::get, Component.literal("Hey"), 30),
                new ChatLine(ModCharacters.POLLY::get, Component.literal("asdf"), 30),
                new ChatLine(ModCharacters.DIGORY::get, Component.literal("ghjk"), 30)));
    }
}
