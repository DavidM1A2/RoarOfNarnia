package com.dslovikosky.narnia.client.gui.event;

import com.dslovikosky.narnia.client.gui.control.GuiComponentWithEvents;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.input.InputQuirks;

/**
 * 26.3 replaced GLFW with SDL, which splits a key press into two different numbers. The key is a physical
 * SDL scancode, matching InputConstants.KEY_*, and is what a key binding is compared against. The key code is
 * the layout dependent SDL key code, matching InputConstants.KEYCODE_*, and is what text editing shortcuts
 * compare against so that they follow the user's keyboard layout. The old GLFW scan code had no such role,
 * which is why this carries a key code now instead.
 */
public class KeyEvent extends GuiEvent {
    private final int key;
    private final int keyCode;
    private final int character;
    private final int modifiers;
    private final KeyEventType eventType;

    public KeyEvent(final GuiComponentWithEvents source, final int key, final int keyCode, final int character, final int modifiers, final KeyEventType eventType) {
        super(source);
        this.key = key;
        this.keyCode = keyCode;
        this.character = character;
        this.modifiers = modifiers;
        this.eventType = eventType;
    }

    public int getKey() {
        return key;
    }

    public int getKeyCode() {
        return keyCode;
    }

    public int getCharacter() {
        return character;
    }

    public KeyEventType getEventType() {
        return eventType;
    }

    public boolean hasModifier(final Modifier modifier) {
        return (modifiers & modifier.mask) > 0;
    }

    /**
     * Whether the modifier that triggers a text editing shortcut is held. This is control everywhere except
     * macOS, where vanilla uses command instead.
     */
    public boolean hasEditShortcutModifier() {
        return (modifiers & InputQuirks.EDIT_SHORTCUT_KEY_MODIFIER) > 0;
    }

    public enum KeyEventType {
        Press,
        Release,
        Type
    }

    public enum Modifier {
        SHIFT(InputConstants.MOD_SHIFT),
        CONTROL(InputConstants.MOD_CONTROL),
        ALT(InputConstants.MOD_ALT),
        SUPER(InputConstants.MOD_SUPER),
        CAPS_LOCK(InputConstants.MOD_CAPS_LOCK),
        NUM_LOCK(InputConstants.MOD_NUM_LOCK);

        private final int mask;

        Modifier(int mask) {
            this.mask = mask;
        }

        public int getMask() {
            return mask;
        }
    }
}
