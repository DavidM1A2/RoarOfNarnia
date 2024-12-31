package com.dslovikosky.narnia.common.constants;

import com.dslovikosky.narnia.common.model.chapter.Character;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModCharacters {
    public static final DeferredRegister<Character> CHARACTERS = DeferredRegister.create(ModRegistries.CHARACTER, Constants.MOD_ID);

    public static final DeferredHolder<Character, ? extends Character> DIGORY = CHARACTERS.register("digory", id -> new Character(id, true));
    public static final DeferredHolder<Character, ? extends Character> POLLY = CHARACTERS.register("polly", id -> new Character(id, true));
    public static final DeferredHolder<Character, ? extends Character> UNCLE_ANDREW = CHARACTERS.register("uncle_andrew", id -> new Character(id, false));
}
