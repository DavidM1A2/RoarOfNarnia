package com.dslovikosky.narnia.common.item;

import com.dslovikosky.narnia.client.gui.screen.PowerSourceSelectionScreen;
import com.dslovikosky.narnia.client.gui.screen.SpellwrightingScreen;
import com.dslovikosky.narnia.common.constants.Constants;
import com.dslovikosky.narnia.common.constants.ModSpellDeliveryMethods;
import com.dslovikosky.narnia.common.constants.ModSpellEffects;
import com.dslovikosky.narnia.common.spell.Spell;
import com.dslovikosky.narnia.common.spell.SpellStage;
import com.dslovikosky.narnia.common.spell.component.deliveryMethod.SpellDeliveryMethodInstance;
import com.dslovikosky.narnia.common.spell.component.effect.base.SpellEffectInstance;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;

public class AslansChronicleItem extends Item {
    public AslansChronicleItem() {
        super(new Properties().stacksTo(1).fireResistant().setId(ResourceKey.create(Registries.ITEM, Constants.modLocation("aslans_chronicle"))));
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        Minecraft.getInstance().setScreen(new SpellwrightingScreen());

        return super.use(level, player, hand);
    }
}
