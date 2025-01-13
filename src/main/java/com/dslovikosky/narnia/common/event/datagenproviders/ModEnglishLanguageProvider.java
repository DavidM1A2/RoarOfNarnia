package com.dslovikosky.narnia.common.event.datagenproviders;

import com.dslovikosky.narnia.common.constants.Constants;
import com.dslovikosky.narnia.common.constants.ModBlocks;
import com.dslovikosky.narnia.common.constants.ModEntityTypes;
import com.dslovikosky.narnia.common.constants.ModItems;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

public class ModEnglishLanguageProvider extends LanguageProvider {
    public ModEnglishLanguageProvider(final PackOutput output) {
        super(output, Constants.MOD_ID, "en_us");
    }

    @Override
    protected void addTranslations() {
        addItem(ModItems.YELLOW_RING, "Yellow Ring");
        addItem(ModItems.GREEN_RING, "Green Ring");
        addItem(ModItems.DEBUG, "Debug");

        addItem(ModItems.THE_MAGICIANS_NEPHEW, "The Magician's Nephew");

        addItem(ModItems.WORLD_WOOD_BOAT, "World Wood Boat");
        addItem(ModItems.WORLD_WOOD_CHEST_BOAT, "World Wood Chest Boat");
        addItem(ModItems.WORLD_WOOD_SIGN, "World Wood Sign");
        addItem(ModItems.WORLD_WOOD_HANGING_SIGN, "World Wood Hanging Sign");

        addBlock(ModBlocks.WORLD_WOOD, "World Wood");
        addBlock(ModBlocks.WORLD_WOOD_LOG, "World Wood Log");
        addBlock(ModBlocks.STRIPPED_WORLD_WOOD, "Stripped World Wood");
        addBlock(ModBlocks.STRIPPED_WORLD_WOOD_LOG, "Stripped World Wood Log");
        addBlock(ModBlocks.WORLD_WOOD_PLANKS, "World Wood Planks");
        addBlock(ModBlocks.WORLD_WOOD_BUTTON, "World Wood Button");
        addBlock(ModBlocks.WORLD_WOOD_DOOR, "World Wood Door");
        addBlock(ModBlocks.WORLD_WOOD_FENCE, "World Wood Fence");
        addBlock(ModBlocks.WORLD_WOOD_FENCE_GATE, "World Wood Fence Gate");
        addBlock(ModBlocks.WORLD_WOOD_LEAVES, "World Wood Leaves");
        addBlock(ModBlocks.WORLD_WOOD_SAPLING, "World Wood Sapling");
        addBlock(ModBlocks.WORLD_WOOD_SLAB, "World Wood Slab");
        addBlock(ModBlocks.WORLD_WOOD_STAIR, "World Wood Stair");
        addBlock(ModBlocks.WORLD_WOOD_TRAP_DOOR, "World Wood Trapdoor");
        addBlock(ModBlocks.WORLD_WOOD_PRESSURE_PLATE, "World Wood Pressure Plate");

        addEntityType(ModEntityTypes.DIGORY, "Digory");
        addEntityType(ModEntityTypes.POLLY, "Polly");

        add("character.narnia.spectator.name", "Spectator");
        add("character.narnia.digory.name", "Digory");
        add("character.narnia.polly.name", "Polly");

        add("book.narnia.the_magicians_nephew.name", "The Magician's Nephew");

        add("chapter.narnia.the_magicians_nephew.the_wrong_door.title", "The Wrong Door");
        add("chapter.narnia.the_magicians_nephew.digory_and_his_uncle.title", "Digory and His Uncle");
        add("chapter.narnia.the_magicians_nephew.the_wood_between_the_worlds.title", "The Wood Between the Worlds");

        add("chat.conversation.character_speaks", "[%1$s] %2$s");

        add("gui.narnia.book.chapter_header", "Chapter %1$d\n%2$s");
        add("gui.narnia.book.join_scene", "Join Scene");
        add("gui.narnia.book.leave_scene", "Leave Scene");

        add("sound.narnia.ui.page_turn", "Page Turn");
    }
}
