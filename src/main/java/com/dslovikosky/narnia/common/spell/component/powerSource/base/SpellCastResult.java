package com.dslovikosky.narnia.common.spell.component.powerSource.base;

import net.minecraft.network.chat.Component;

public class SpellCastResult {
    private final Component failureMessage;

    private SpellCastResult(final Component failureMessage) {
        this.failureMessage = failureMessage;
    }

    public static SpellCastResult success() {
        return new SpellCastResult(null);
    }

    public static SpellCastResult failure(final Component failureMessage) {
        return new SpellCastResult(failureMessage);
    }

    public boolean wasSuccessful() {
        return failureMessage == null;
    }

    public Component getFailureMessage() {
        return failureMessage;
    }
}
