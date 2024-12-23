package com.dslovikosky.narnia.client.gui.event;

import com.dslovikosky.narnia.client.gui.control.GuiComponentWithEvents;
import org.lwjgl.glfw.GLFW;

public class KeyEvent extends GuiEvent {
    private final int key;
    private final int scanCode;
    private final char character;
    private final int modifiers;
    private final KeyEventType eventType;

    public KeyEvent(final GuiComponentWithEvents source, final int key, final int scanCode, final char character, final int modifiers, final KeyEventType eventType) {
        super(source);
        this.key = key;
        this.scanCode = scanCode;
        this.character = character;
        this.modifiers = modifiers;
        this.eventType = eventType;
    }

    public int getKey() {
        return key;
    }

    public int getScanCode() {
        return scanCode;
    }

    public char getCharacter() {
        return character;
    }

    public KeyEventType getEventType() {
        return eventType;
    }

    public boolean hasModifier(final Modifier modifier) {
        return (modifiers & modifier.mask) > 0;
    }

    public enum KeyEventType {
        Press,
        Release,
        Type
    }

    public enum Modifier {
        SHIFT(GLFW.GLFW_MOD_SHIFT),
        CONTROL(GLFW.GLFW_MOD_CONTROL),
        ALT(GLFW.GLFW_MOD_ALT),
        SUPER(GLFW.GLFW_MOD_SUPER),
        CAPS_LOCK(GLFW.GLFW_MOD_CAPS_LOCK),
        NUM_LOCK(GLFW.GLFW_MOD_NUM_LOCK);

        private final int mask;

        Modifier(int mask) {
            this.mask = mask;
        }

        public int getMask() {
            return mask;
        }
    }
}
