package com.dslovikosky.narnia.common.constants;

import com.dslovikosky.narnia.common.model.chapter.NarniaBook;
import com.dslovikosky.narnia.common.model.chapter.NarniaChapter;
import net.minecraft.network.chat.Component;

import java.util.List;

public class ModNarniaBooks {
    public static final NarniaBook THE_MAGICIANS_NEPHEW = new NarniaBook(Component.translatable("book.narnia.the_magicians_nephew.name"), List.of(
            new NarniaChapter(Component.translatable("chapter.narnia.the_magicians_nephew.the_wrong_door.title"),
                    List.of(),
                    List.of(ModNarniaActors.DIGORY, ModNarniaActors.POLLY)),
            new NarniaChapter(Component.translatable("chapter.narnia.the_magicians_nephew.digory_and_his_uncle.title"),
                    List.of(),
                    List.of(ModNarniaActors.DIGORY, ModNarniaActors.POLLY)),
            new NarniaChapter(Component.translatable("chapter.narnia.the_magicians_nephew.the_wood_between_the_worlds.title"),
                    List.of(),
                    List.of(ModNarniaActors.DIGORY, ModNarniaActors.POLLY))
    ));
}
