package com.dslovikosky.narnia.common.model.scene.goal;

import com.dslovikosky.narnia.common.model.scene.Character;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.function.Supplier;

public class ChatLine {
    private final Supplier<Character> speaker;
    private final Supplier<Character> listener;
    private final Component component;
    private final int durationTicks;

    private ChatLine(final Supplier<Character> speaker, final Supplier<Character> listener, final Component component, final int durationTicks) {
        this.speaker = speaker;
        this.listener = listener;
        this.component = component;
        this.durationTicks = durationTicks;
    }

    public static ChatLine between(final DeferredHolder<Character, ? extends Character> speaker, final DeferredHolder<Character, ? extends Character> listener, final Component component, final int durationTicks) {
        return new ChatLine(speaker::get, listener::get, component, durationTicks);
    }

    public static ChatLine between(final DeferredHolder<Character, ? extends Character> speaker, final DeferredHolder<Character, ? extends Character> listener, final Component component) {
        return new ChatLine(speaker::get, listener::get, component, getLineDuration(component));
    }

    public static ChatLine of(final DeferredHolder<Character, ? extends Character> speaker, final Component component, final int durationTicks) {
        return new ChatLine(speaker::get, () -> null, component, durationTicks);
    }

    public static ChatLine of(DeferredHolder<Character, ? extends Character> speaker, final Component component) {
        return new ChatLine(speaker::get, () -> null, component, getLineDuration(component));
    }

    private static int getLineDuration(final Component line) {
        return (int) Math.max(50, Math.round(line.getString().length() * 1.1));
    }

    public Character getSpeaker() {
        return speaker.get();
    }

    public Character getListener() {
        return listener.get();
    }

    public Component getComponent() {
        return component;
    }

    public int getDurationTicks() {
        return durationTicks;
    }
}
