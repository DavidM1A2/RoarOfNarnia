package com.dslovikosky.narnia.common.item;

import com.dslovikosky.narnia.client.proxy.ClientProxy;
import com.dslovikosky.narnia.common.constants.Constants;
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
        // use runs on both sides, so without this guard the server thread opens a second screen on top of the
        // client's. Replacing a screen clears the old one's focus and stops text input, which left the GUI
        // drawn but deaf to the keyboard.
        if (level.isClientSide()) {
            ClientProxy.openSpellwrightingScreen();
        }

        return super.use(level, player, hand);
    }
}
