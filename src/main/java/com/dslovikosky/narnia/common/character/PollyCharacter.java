package com.dslovikosky.narnia.common.character;

import com.dslovikosky.narnia.common.constants.ModEntityTypes;
import com.dslovikosky.narnia.common.model.scene.Character;
import net.minecraft.resources.ResourceLocation;

public class PollyCharacter extends Character {
    public PollyCharacter(final ResourceLocation id) {
        super(id, ModEntityTypes.POLLY::get);
    }
}
