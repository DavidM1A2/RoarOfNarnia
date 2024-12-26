package com.dslovikosky.narnia.common.model.chapter;

import net.minecraft.network.chat.Component;

import java.util.List;

public record NarniaBook(Component title, List<NarniaChapter> narniaChapters) {
}
