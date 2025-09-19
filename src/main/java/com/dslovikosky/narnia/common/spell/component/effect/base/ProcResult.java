package com.dslovikosky.narnia.common.spell.component.effect.base;

public class ProcResult {
    private final boolean isSuccess;

    public ProcResult(final boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    public static ProcResult success() {
        return new ProcResult(true);
    }

    public static ProcResult failure() {
        return new ProcResult(false);
    }

    public boolean isSuccess() {
        return isSuccess;
    }
}
