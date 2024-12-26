package com.dslovikosky.narnia.common.model.chapter;

import net.minecraft.network.chat.Component;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
public record NarniaChapter(Component title, List<NarniaChapterGoal> goals, List<NarniaActor> actors) {
}
