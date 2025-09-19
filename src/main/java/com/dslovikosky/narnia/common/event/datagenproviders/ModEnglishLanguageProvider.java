package com.dslovikosky.narnia.common.event.datagenproviders;

import com.dslovikosky.narnia.common.constants.Constants;
import com.dslovikosky.narnia.common.constants.ModBlocks;
import com.dslovikosky.narnia.common.constants.ModEntityTypes;
import com.dslovikosky.narnia.common.constants.ModItems;
import com.dslovikosky.narnia.common.constants.ModMobEffects;
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
        addItem(ModItems.ASLANS_CHRONICLE, "Aslan's Chronicle");
        addItem(ModItems.SPARKLING_DUST, "Sparkling Dust");

        addItem(ModItems.WORLD_WOOD_BOAT, "World Wood Boat");
        addItem(ModItems.WORLD_WOOD_CHEST_BOAT, "World Wood Chest Boat");
        addItem(ModItems.WORLD_WOOD_STANDING_SIGN, "World Wood Sign");
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

        addEntityType(ModEntityTypes.WORLD_WOOD_BOAT, "World Wood Boat");
        addEntityType(ModEntityTypes.WORLD_WOOD_CHEST_BOAT, "World Wood Chest Boat");

        addEffect(ModMobEffects.DROWSY, "Drowsy");

        add("sound.narnia.ui.page_turn", "Page Turn");
        add("sound.narnia.music.wood_between_the_worlds", "Wood Between the Worlds Music");
        add("sound.narnia.entity.spell_cast", "Spell Cast");

        add("power_source.narnia.creative.name", "Creative");
        add("power_source.narnia.creative.description", "Cast spells of any cost while in creative mode.");
        add("power_source.narnia.creative.cost_overview", "Unlimited power!");
        add("power_source.narnia.creative.not_enough_power", "You must be in creative mode to cast this spell.");
        add("power_source.narnia.creative.formatted_cost", "%1$s (free)");

        add("power_source.narnia.experience.name", "Experience");
        add("power_source.narnia.experience.description", "Cast spells using your XP level.");
        add("power_source.narnia.experience.cost_overview", "One spell cost per XP point (not level)");
        add("power_source.narnia.experience.not_enough_power", "Not enough experience to cast this spell.");
        add("power_source.narnia.experience.formatted_cost", "%1$s XP points");

        add("power_source.narnia.health.name", "Blood Magic");
        add("power_source.narnia.health.description", "Cast spells using your health.");
        add("power_source.narnia.health.cost_overview", "Five spell cost per half heart of damage");
        add("power_source.narnia.health.not_enough_power", "Not enough health to cast this spell.");
        add("power_source.narnia.health.was_just_hurt", "You can't cast after just being hurt.");
        add("power_source.narnia.health.formatted_cost", "%1$s heart(s)");

        add("power_source.narnia.alchemy.name", "Alchemy");
        add("power_source.narnia.alchemy.description", "Cast spells by consuming gold or astral silver ingots from your inventory.");
        add("power_source.narnia.alchemy.cost_overview", "Ten (gold) or twenty (astral silver) spell cost per ingot consumed");
        add("power_source.narnia.alchemy.not_enough_power", "You do not have enough gold or astral silver in your inventory to cast this spell.");
        add("power_source.narnia.alchemy.formatted_cost", "%1$s gold ingots");

        add("power_source.narnia.vitae_lantern.name", "Vitae Lantern");
        add("power_source.narnia.vitae_lantern.description", "Cast spells by consuming vitae from vitae lanterns in your inventory.");
        add("power_source.narnia.vitae_lantern.cost_overview", "One spell cost per lantern vitae");
        add("power_source.narnia.vitae_lantern.not_enough_power", "You do not have enough total vitae in your lanterns to cast this spell.");
        add("power_source.narnia.vitae_lantern.formatted_cost", "%1$s vitae");

        add("power_source.narnia.crystal.name", "Crystal");
        add("power_source.narnia.crystal.description", "Cast spells by consuming vitae from nearby magic crystals.");
        add("power_source.narnia.crystal.cost_overview", "One spell cost per magic crystal vitae");
        add("power_source.narnia.crystal.not_enough_power", "There is not enough vitae in nearby magic crystals to cast this spell.");
        add("power_source.narnia.crystal.formatted_cost", "%1$s vitae");

        add("power_source.narnia.lunar.name", "Lunar");
        add("power_source.narnia.lunar.description", "Cast spells by consuming vitae obtained at night. The fuller the moon phase, the faster vitae is obtained and the higher the cap.");
        add("power_source.narnia.lunar.cost_overview", "One spell cost per lunar vitae");
        add("power_source.narnia.lunar.not_enough_power", "You do not have enough vitae to cast this spell.");
        add("power_source.narnia.lunar.formatted_cost", "%1$s vitae");

        add("power_source.narnia.solar.name", "Solar");
        add("power_source.narnia.solar.description", "Cast spells by consuming vitae obtained during the day.");
        add("power_source.narnia.solar.cost_overview", "One spell cost per solar vitae");
        add("power_source.narnia.solar.not_enough_power", "You do not have enough vitae to cast this spell.");
        add("power_source.narnia.solar.formatted_cost", "%1$s vitae");

        add("power_source.narnia.thermal.name", "Thermal");
        add("power_source.narnia.thermal.description", "Cast spells by consuming vitae obtained underground, in warm biomes, and around warm blocks.");
        add("power_source.narnia.thermal.cost_overview", "One spell cost per thermal vitae");
        add("power_source.narnia.thermal.not_enough_power", "You do not have enough vitae to cast this spell.");
        add("power_source.narnia.thermal.formatted_cost", "%1$s vitae");

        add("power_source.narnia.innate.name", "Innate");
        add("power_source.narnia.innate.description", "Cast spells by consuming your innate vitae. Gain one vitae per second");
        add("power_source.narnia.innate.cost_overview", "One spell cost per innate vitae");
        add("power_source.narnia.innate.not_enough_power", "You do not have enough vitae to cast this spell.");
        add("power_source.narnia.innate.formatted_cost", "%1$s vitae");

        add("power_source.narnia.leech.name", "Leech");
        add("power_source.narnia.leech.description", "Cast spells by leeching health from nearby monsters and animals. You can also leech nearby nature blocks which will destroy them. Rarer blocks like crops and flowers give more vitae than basic ones like grass.");
        add("power_source.narnia.leech.cost_overview", "Five spell cost per half heart of damage. One to fourteen spell cost per nature block depending on rarity.");
        add("power_source.narnia.leech.not_enough_power", "There are not enough nearby healthy monsters, animals, or nature blocks to cast the spell.");
        add("power_source.narnia.leech.formatted_cost", "%1$s vitae");

        add("power_source.narnia.spell_scroll.name", "Spell Scroll");
        add("power_source.narnia.spell_scroll.description", "Special power source used by spell scrolls that were pre-paid for.");
        add("power_source.narnia.spell_scroll.cost_overview", "Cast pre-paid spells.");
        add("power_source.narnia.spell_scroll.formatted_cost", "%1$s (free)");

        add("effect.narnia.dig.name", "Dig");
        add("effect.narnia.dig.description", "Breaks the block hit. If an entity is hit, they will get haste or mining fatigue.");
        add("effect.narnia.dig.duration.name", "Duration");
        add("effect.narnia.dig.duration.description", "The number of seconds to apply mining speed when hitting entities. The first three seconds are free.");
        add("effect.narnia.dig.speed.name", "Speed");
        add("effect.narnia.dig.speed.description", "The amount to modify mining speed by when hitting entities. Negative values slow the mining speed, while positive values speed it up.");

        add("effect.narnia.disintegrate.name", "Disintegrate");
        add("effect.narnia.disintegrate.description", "Destroys the block hit. If an entity is hit, they will take damage instead.");
        add("effect.narnia.disintegrate.strength.name", "Strength");
        add("effect.narnia.disintegrate.strength.description", "The damage when an entity is hit.");

        add("effect.narnia.push.name", "Push");
        add("effect.narnia.push.description", "Pushes entities hit. Has no effects on blocks.");
        add("effect.narnia.push.strength.name", "Strength");
        add("effect.narnia.push.strength.description", "The number of blocks to push the entity hit.");

        add("effect.narnia.explosion.name", "Explosion");
        add("effect.narnia.explosion.description", "Creates an explosion centered at the block or entity hit.");
        add("effect.narnia.explosion.radius.name", "Radius");
        add("effect.narnia.explosion.radius.description", "The explosion's radius in blocks.");

        add("effect.narnia.lightning.name", "Lightning");
        add("effect.narnia.lightning.description", "Strikes the block or entity hit with lightning.");

        add("effect.narnia.teleport.name", "Teleport");
        add("effect.narnia.teleport.description", "Teleports the caster of the spell to the block or entity hit.");

        add("effect.narnia.grow.name", "Grow");
        add("effect.narnia.grow.description", "Triggers the 'bonemeal' effect on the block hit. Has no effect on entities.");
        add("effect.narnia.grow.strength.name", "Strength");
        add("effect.narnia.grow.strength.description", "The number of times to 'bonemeal' the block hit.");

        add("effect.narnia.heal.name", "Heal");
        add("effect.narnia.heal.description", "Restores health to the hit entity. Has no effect on blocks.");
        add("effect.narnia.heal.amount.name", "Amount");
        add("effect.narnia.heal.amount.description", "The amount of half hearts to restore.");

        add("effect.narnia.feed.name", "Feed");
        add("effect.narnia.feed.description", "Restores hunger to the player hit. Has no effect on blocks or non-player entities.");
        add("effect.narnia.feed.hunger_amount.name", "Hunger Amount");
        add("effect.narnia.feed.hunger_amount.description", "The amount of food half 'drumsticks' to restore.");
        add("effect.narnia.feed.saturation_amount.name", "Saturation Amount");
        add("effect.narnia.feed.saturation_amount.description", "The amount of saturation restore.");

        add("effect.narnia.smoke_screen.name", "Smoke Screen");
        add("effect.narnia.smoke_screen.description", "Creates a smoke screen which blocks vision around the entity or block hit.");

        add("effect.narnia.burn.name", "Burn");
        add("effect.narnia.burn.description", "Lights the block or entity hit on fire.");
        add("effect.narnia.burn.duration.name", "Duration");
        add("effect.narnia.burn.duration.description", "The number of seconds to set fire to when hitting entities.");

        add("effect.narnia.cleanse.name", "Cleanse");
        add("effect.narnia.cleanse.description", "Clears effects that affect the entity hit.");
        add("effect.narnia.cleanse.beneficial_potion_effects.name", "Beneficial Potion Effects");
        add("effect.narnia.cleanse.beneficial_potion_effects.description", "Removes \"good\" potion effects like regeneration.");
        add("effect.narnia.cleanse.neutral_potion_effects.name", "Neutral Potion Effects");
        add("effect.narnia.cleanse.neutral_potion_effects.description", "Removes \"neutral\" potion effects like glowing.");
        add("effect.narnia.cleanse.harmful_potion_effects.name", "Harmful Potion Effects");
        add("effect.narnia.cleanse.harmful_potion_effects.description", "Removes \"bad\" potion effects like wither.");
        add("effect.narnia.cleanse.spell_effects.name", "Spell Effects");
        add("effect.narnia.cleanse.spell_effects.description", "Removes spell effects like charm and freeze.");

        add("effect.narnia.extinguish.name", "Extinguish");
        add("effect.narnia.extinguish.description", "Extinguishes fire on the block or entity hit.");

        add("effect.narnia.ender_pocket.name", "Ender Pocket");
        add("effect.narnia.ender_pocket.description", "Causes the player hit to open their enderchest. Has no effect on blocks or non-player entities.");

        add("effect.narnia.freeze.name", "Freeze");
        add("effect.narnia.freeze.description", "Freezes the entity or water block hit. Frozen entities won't be able to move or look around. Has no effect on non-water blocks");
        add("effect.narnia.freeze.duration.name", "Duration");
        add("effect.narnia.freeze.duration.description", "The number of seconds the freeze will last against entities. The first second is free.");

        add("effect.narnia.charm.name", "Charm");
        add("effect.narnia.charm.description", "Forces the player hit to look at the spell caster. Animals hit will breed. Has no effect on blocks or mobs.");
        add("effect.narnia.charm.duration.name", "Duration");
        add("effect.narnia.charm.duration.description", "The number of seconds to charm to when hitting players. The first two seconds are free.");

        add("effect.narnia.speed.name", "Speed");
        add("effect.narnia.speed.description", "Increases or decreases the movement speed of the entity hit. Creates a potion cloud if a block is hit.");
        add("effect.narnia.speed.duration.name", "Duration");
        add("effect.narnia.speed.duration.description", "The number of seconds to apply the movement speed.");
        add("effect.narnia.speed.multiplier.name", "Multiplier");
        add("effect.narnia.speed.multiplier.description", "The amount to modify movement speed by. Negative values slow the target, while positive values speed up the target.");

        add("effect.narnia.ward.name", "Ward");
        add("effect.narnia.ward.description", "Grants resistance to the entity hit. Makes blocks hit harder to mine.");
        add("effect.narnia.ward.duration.name", "Duration");
        add("effect.narnia.ward.duration.description", "The number of seconds to apply the resistance for when hitting entities. The first three seconds are free.");
        add("effect.narnia.ward.strength.name", "Multiplier");
        add("effect.narnia.ward.strength.description", "The amount of resistance to grant when hitting entities (up to 4), or the ward strength of the hit block.");

        add("effect.narnia.sonic_disruption.name", "Sonic Disruption");
        add("effect.narnia.sonic_disruption.description", "Plays a sound at the hit location.");
        add("effect.narnia.sonic_disruption.sound.name", "Sound");
        add("effect.narnia.sonic_disruption.sound.description", "The sound to play. You can find a list of sound names online, or by typing '/playsound '. Mod sounds must start with '<mod-name>:'");
        add("effect.narnia.sonic_disruption.volume.name", "Volume");
        add("effect.narnia.sonic_disruption.volume.description", "How loud the sound is.");
        add("effect.narnia.sonic_disruption.pitch.name", "Pitch");
        add("effect.narnia.sonic_disruption.pitch.description", "How high or low the sound is.");

        add("effect.narnia.summon_arrow.name", "Summon Arrow");
        add("effect.narnia.summon_arrow.description", "Summons an arrow into the world.");
        add("effect.narnia.summon_arrow.speed.name", "Speed");
        add("effect.narnia.summon_arrow.speed.description", "The speed the arrow will fly at in blocks per second.");

        add("effect.narnia.fey_light.name", "Fey Light");
        add("effect.narnia.fey_light.description", "Creates a magical light at a given position.");
        add("effect.narnia.fey_light.color.name", "Color");
        add("effect.narnia.fey_light.color.description", "The color of the light in the format 'r g b' where red, green, and blue are values between 0 and 255.");

        add("delivery_method.narnia.self.name", "Self");
        add("delivery_method.narnia.self.description", "Applies the effects to the last affected entity. This is the spell caster or last hit entity by the previous spell stage. Does nothing when applied to blocks.");

        add("delivery_method.narnia.projectile.name", "Projectile");
        add("delivery_method.narnia.projectile.description", "Shoots a projectile that applies the effects to block or entity hit.");
        add("delivery_method.narnia.projectile.range.name", "Range");
        add("delivery_method.narnia.projectile.range.description", "The range of the projectile in blocks.");
        add("delivery_method.narnia.projectile.speed.name", "Speed");
        add("delivery_method.narnia.projectile.speed.description", "The speed of the projectile in blocks/second.");
        add("delivery_method.narnia.projectile.color.name", "Color");
        add("delivery_method.narnia.projectile.color.description", "The color of the projectile in the format 'r g b' where red, green, and blue are values between 0 and 255.");

        add("delivery_method.narnia.aoe.name", "AOE");
        add("delivery_method.narnia.aoe.description", "Applies the effects in a sphere around the block or entity hit by the previous spell stage.");
        add("delivery_method.narnia.aoe.radius.name", "Radius");
        add("delivery_method.narnia.aoe.radius.description", "The area of effect radius in blocks.");
        add("delivery_method.narnia.aoe.shell_only.name", "Shell Only");
        add("delivery_method.narnia.aoe.shell_only.description", "Checked means effects should only be applied at the outer shell of the AOE, and not at the center.");
        add("delivery_method.narnia.aoe.color.name", "Color");
        add("delivery_method.narnia.aoe.color.description", "The color of the AOE in the format 'r g b' where red, green, and blue are values between 0 and 255.");

        add("delivery_method.narnia.chain.name", "Chain");
        add("delivery_method.narnia.chain.description", "Applies the effects to a chain of entities.");
        add("delivery_method.narnia.chain.max_distance.name", "Max Distance");
        add("delivery_method.narnia.chain.max_distance.description", "The maximum distance the chain can jump between entities.");
        add("delivery_method.narnia.chain.max_hops.name", "Max Hops");
        add("delivery_method.narnia.chain.max_hops.description", "The maximum number of entities that the chain can hit.");

        add("delivery_method.narnia.laser.name", "Laser");
        add("delivery_method.narnia.laser.description", "Shoots a laser beam that applies the effects to block or entity hit.");
        add("delivery_method.narnia.laser.range.name", "Range");
        add("delivery_method.narnia.laser.range.description", "The range of the laser in blocks.");
        add("delivery_method.narnia.laser.hit_liquids.name", "Hit Liquids");
        add("delivery_method.narnia.laser.hit_liquids.description", "'True' to let liquid blocks be hit, or 'false' to go through them.");
        add("delivery_method.narnia.laser.color.name", "Color");
        add("delivery_method.narnia.laser.color.description", "The color of the laser in the format 'r g b' where red, green, and blue are values between 0 and 255.");

        add("delivery_method.narnia.delay.name", "Delay");
        add("delivery_method.narnia.delay.description", "Applies the effects after a delay to the block or entity hit by the previous spell stage.");
        add("delivery_method.narnia.delay.delay.name", "Delay");
        add("delivery_method.narnia.delay.delay.description", "The delay of the delivery in seconds.");

        add("delivery_method.narnia.rotate.name", "Rotate");
        add("delivery_method.narnia.rotate.description", "Applies the effects to the block or entity hit by the previous spell stage. The next delivery method will be rotated before triggering.");
        add("delivery_method.narnia.rotate.yaw.name", "Yaw");
        add("delivery_method.narnia.rotate.yaw.description", "The yaw (left/right rotation) to apply in degrees. Yaw gets applied before pitch.");
        add("delivery_method.narnia.rotate.pitch.name", "Pitch");
        add("delivery_method.narnia.rotate.pitch.description", "The pitch (up/down rotation) to apply in degrees. Yaw gets applied before pitch.");

        add("delivery_method.narnia.imbue.name", "Imbue");
        add("delivery_method.narnia.imbue.description", "Writes the remainder of the spell after this delivery method to an empty spell scroll in the target's inventory. Imbue will be replaced by the 'self' as the first delivery method on the spell scroll. If the target does not have an empty spell scroll, the spell will fizzle out.");
        add("delivery_method.narnia.imbue.uses.name", "Uses");
        add("delivery_method.narnia.imbue.uses.description", "The number of uses the imbued spell scroll will have.");

        add("delivery_method.narnia.cone.name", "Cone");
        add("delivery_method.narnia.cone.description", "Applies the effects to blocks in a cone starting from the block or entity hit by the previous spell stage.");
        add("delivery_method.narnia.cone.radius.name", "Radius");
        add("delivery_method.narnia.cone.radius.description", "The cone's base radius in blocks.");
        add("delivery_method.narnia.cone.length.name", "Length");
        add("delivery_method.narnia.cone.length.description", "The distance the cone covers from the source in blocks.");
        add("delivery_method.narnia.cone.shell_only.name", "Shell Only");
        add("delivery_method.narnia.cone.shell_only.description", "Checked means effects should only be applied at the outer shell of the cone, and not at the center.");
        add("delivery_method.narnia.cone.color.name", "Color");
        add("delivery_method.narnia.cone.color.description", "The color of the cone in the format 'r g b' where red, green, and blue are values between 0 and 255.");

        add("delivery_method.narnia.wall.name", "Wall");
        add("delivery_method.narnia.wall.description", "Applies the effects to blocks in a rectangle starting from the block or entity hit by the previous spell stage.");
        add("delivery_method.narnia.wall.width.name", "Width");
        add("delivery_method.narnia.wall.width.description", "The rectangle's width.");
        add("delivery_method.narnia.wall.height.name", "Height");
        add("delivery_method.narnia.wall.height.description", "The rectangle's height.");
        add("delivery_method.narnia.wall.color.name", "Color");
        add("delivery_method.narnia.wall.color.description", "The color of the wall in the format 'r g b' where red, green, and blue are values between 0 and 255.");

        add("property_error.narnia.color.format", "RGB must be in the format 'r g b' (without the quotes)");
        add("property_error.narnia.color.value_range", "All 3 'r g b' values must be between 0 to 255");
        add("property_error.narnia.color.value_type", "All 3 'r g b' values must be integers between 0 to 255");

        add("property_error.narnia.potion.type", "Invalid potion type %1$s, it was not found in the registry.");

        add("property_error.narnia.boolean.format", "%1$s must be 'true' or 'false'");

        add("property_error.narnia.double.format", "%1$s is not a valid decimal value");
        add("property_error.narnia.double.too_small", "%1$s must be larger than or equal to %2$f");
        add("property_error.narnia.double.too_large", "%1$s must be smaller than or equal to %2$f");

        add("property_error.narnia.enum.format", "%1$s must in the list of options");

        add("property_error.narnia.float.format", "%1$s is not a valid decimal value");
        add("property_error.narnia.float.too_small", "%1$s must be larger than or equal to %2$f");
        add("property_error.narnia.float.too_large", "%1$s must be smaller than than or equal to %2$f");

        add("property_error.narnia.integer.format", "%1$s is not a valid integer");
        add("property_error.narnia.integer.too_small", "%1$s must be larger than or equal to %2$d");
        add("property_error.narnia.integer.too_large", "%1$s must be smaller than than or equal to %2$d");

        add("property_error.narnia.long.format", "%1$s is not a valid integer");
        add("property_error.narnia.long.too_small", "%1$s must be larger than or equal to %2$d");
        add("property_error.narnia.long.too_large", "%1$s must be smaller than than or equal to %2$d");

        add("property_error.narnia.registry_entry.invalid_resource_location", "%1$s is not a valid name");
        add("property_error.narnia.registry_entry.missing_entry", "%1$s does not exist");
        add("property_error.narnia.registry_entry.disallowed_entry", "%1$s is not allowed");

        add("message.narnia.spell.wrong_dimension", "My mind is too clouded to cast spells here.");
        add("message.narnia.spell.invalid", "Invalid spell. Make sure to have delivery methods on each spell stage!");
        add("message.narnia.spell.power_source_changed", "§oSpells will be cast with %1$s");
    }
}
