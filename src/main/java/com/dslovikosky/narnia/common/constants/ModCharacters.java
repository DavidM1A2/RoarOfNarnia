package com.dslovikosky.narnia.common.constants;

import com.dslovikosky.narnia.common.character.DigoryCharacter;
import com.dslovikosky.narnia.common.character.PollyCharacter;
import com.dslovikosky.narnia.common.character.UncleAndrewCharacter;
import com.dslovikosky.narnia.common.model.scene.Character;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModCharacters {
    public static final DeferredRegister<Character> CHARACTERS = DeferredRegister.create(ModRegistries.CHARACTER, Constants.MOD_ID);

    public static final DeferredHolder<Character, DigoryCharacter> DIGORY = CHARACTERS.register("digory", DigoryCharacter::new);
    public static final DeferredHolder<Character, PollyCharacter> POLLY = CHARACTERS.register("polly", PollyCharacter::new);
    public static final DeferredHolder<Character, UncleAndrewCharacter> UNCLE_ANDREW = CHARACTERS.register("uncle_andrew", UncleAndrewCharacter::new);
}
