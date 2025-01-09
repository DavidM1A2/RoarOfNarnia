package com.dslovikosky.narnia.common.constants;

import com.dslovikosky.narnia.common.model.chapter.Chapter;
import com.dslovikosky.narnia.common.model.chapter.goal.DigoryPollyIntroductionGoal;
import com.dslovikosky.narnia.common.model.chapter.goal.FindUncleAndrewsHouseGoal;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.List;

public class ModChapters {
    public static final DeferredRegister<Chapter> CHAPTERS = DeferredRegister.create(ModRegistries.CHAPTER, Constants.MOD_ID);
    public static final DeferredHolder<Chapter, ? extends Chapter> THE_WRONG_DOOR = CHAPTERS.register("the_wrong_door", id ->
            new Chapter(id, ModBooks.THE_MAGICIANS_NEPHEW, List.of(ModCharacters.DIGORY, ModCharacters.POLLY), List.of(new FindUncleAndrewsHouseGoal(), new DigoryPollyIntroductionGoal())));
    public static final DeferredHolder<Chapter, ? extends Chapter> DIGORY_AND_HIS_UNCLE = CHAPTERS.register("digory_and_his_uncle", id ->
            new Chapter(id, ModBooks.THE_MAGICIANS_NEPHEW, List.of(ModCharacters.DIGORY, ModCharacters.UNCLE_ANDREW), List.of()));
    public static final DeferredHolder<Chapter, ? extends Chapter> THE_WOOD_BETWEEN_THE_WORLDS = CHAPTERS.register("the_wood_between_the_worlds", id ->
            new Chapter(id, ModBooks.THE_MAGICIANS_NEPHEW, List.of(ModCharacters.DIGORY, ModCharacters.POLLY, ModCharacters.UNCLE_ANDREW), List.of()));
}
