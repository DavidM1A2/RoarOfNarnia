package com.dslovikosky.narnia.common.constants;

import com.dslovikosky.narnia.common.chapter.DigoryAndHisUncleChapter;
import com.dslovikosky.narnia.common.chapter.TheWoodBetweenTheWorldsChapter;
import com.dslovikosky.narnia.common.chapter.TheWrongDoorChapter;
import com.dslovikosky.narnia.common.model.scene.Chapter;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModChapters {
    public static final DeferredRegister<Chapter> CHAPTERS = DeferredRegister.create(ModRegistries.CHAPTER, Constants.MOD_ID);
    public static final DeferredHolder<Chapter, TheWrongDoorChapter> THE_WRONG_DOOR = CHAPTERS.register("the_wrong_door", TheWrongDoorChapter::new);
    public static final DeferredHolder<Chapter, DigoryAndHisUncleChapter> DIGORY_AND_HIS_UNCLE = CHAPTERS.register("digory_and_his_uncle", DigoryAndHisUncleChapter::new);
    public static final DeferredHolder<Chapter, TheWoodBetweenTheWorldsChapter> THE_WOOD_BETWEEN_THE_WORLDS = CHAPTERS.register("the_wood_between_the_worlds", TheWoodBetweenTheWorldsChapter::new);
}
