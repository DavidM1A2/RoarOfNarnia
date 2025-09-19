package com.dslovikosky.narnia.common.spell.component.powerSource.base;

public class CastEnvironment<T> {
    private final double vitaeAvailable;
    private final double vitaeMaximum;
    private final T context;

    public CastEnvironment(final double vitaeAvailable, final double vitaeMaximum, final T context) {
        this.vitaeAvailable = vitaeAvailable;
        this.vitaeMaximum = vitaeMaximum;
        this.context = context;
    }

    public static <T> CastEnvironment<T> noVitae(final T context) {
        return new CastEnvironment<>(0, 0, context);
    }

    public static <T> CastEnvironment<T> infiniteVitae(final T context) {
        return new CastEnvironment<>(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, context);
    }

    public static <T> CastEnvironment<T> withVitae(final double vitaeAvailable, final T context) {
        return new CastEnvironment<>(vitaeAvailable, Double.POSITIVE_INFINITY, context);
    }

    public static <T> CastEnvironment<T> withVitae(final double vitaeAvailable, final double vitaeMaximum, final T context) {
        return new CastEnvironment<>(vitaeAvailable, vitaeAvailable, context);
    }

    public double getVitaeAvailable() {
        return vitaeAvailable;
    }

    public double getVitaeMaximum() {
        return vitaeMaximum;
    }

    protected T getContext() {
        return context;
    }
}
