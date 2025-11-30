package com.dslovikosky.narnia.common.model.chat;

public class ChatLine {
    private final int durationTicks;
    private AudienceSelector audienceSelector;

    public ChatLine(int durationTicks) {
        this.durationTicks = durationTicks;
    }

    public ChatLine audience(AudienceSelector selector) {
        this.audienceSelector = selector;
        return this;
    }

    public int getDurationTicks() {
        return durationTicks;
    }

    public AudienceSelector getAudienceSelector() {
        return audienceSelector;
    }
}
