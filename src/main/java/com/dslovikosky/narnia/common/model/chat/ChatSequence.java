package com.dslovikosky.narnia.common.model.chat;

import java.util.ArrayList;
import java.util.List;

public class ChatSequence {
    private final List<ChatLine> lines = new ArrayList<>();
    private AudienceSelector defaultAudience = Audience.DEFAULT_NEARBY;

    public ChatSequence line(int durationTicks) {
        lines.add(new ChatLine(durationTicks));
        return this;
    }

    public ChatSequence audience(AudienceSelector selector) {
        this.defaultAudience = selector;
        return this;
    }

    public List<ChatLine> getLines() {
        return lines;
    }

    public AudienceSelector getAudienceForLine(int index) {
        ChatLine line = lines.get(index);
        return line.getAudienceSelector() != null ? line.getAudienceSelector() : defaultAudience;
    }
}