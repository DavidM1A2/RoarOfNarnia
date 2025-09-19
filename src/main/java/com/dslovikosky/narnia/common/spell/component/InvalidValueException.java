package com.dslovikosky.narnia.common.spell.component;

import net.minecraft.network.chat.Component;

public class InvalidValueException extends RuntimeException {
    private final Component reason;

    public InvalidValueException(final Component reason) {
        this.reason = reason;
    }

    public Component getReason() {
        return reason;
    }
}
