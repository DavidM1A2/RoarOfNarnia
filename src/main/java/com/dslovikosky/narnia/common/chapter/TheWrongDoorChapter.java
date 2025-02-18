package com.dslovikosky.narnia.common.chapter;

import com.dslovikosky.narnia.common.constants.ModAttachmentTypes;
import com.dslovikosky.narnia.common.constants.ModBooks;
import com.dslovikosky.narnia.common.constants.ModDimensions;
import com.dslovikosky.narnia.common.constants.ModSchematics;
import com.dslovikosky.narnia.common.model.PreTeleportLocation;
import com.dslovikosky.narnia.common.model.scene.Chapter;
import com.dslovikosky.narnia.common.model.scene.Scene;
import com.dslovikosky.narnia.common.model.scene.goal.ActorLookChapterGoal;
import com.dslovikosky.narnia.common.model.scene.goal.ActorMoveChapterGoal;
import com.dslovikosky.narnia.common.model.scene.goal.ChatLine;
import com.dslovikosky.narnia.common.model.scene.goal.ConversationChapterGoal;
import com.dslovikosky.narnia.common.model.scene.goal.InstantiateActorChapterGoal;
import com.dslovikosky.narnia.common.model.scene.goal.ParallelChapterGoal;
import com.dslovikosky.narnia.common.model.scene.goal.PlayerInAABBChapterGoal;
import com.dslovikosky.narnia.common.model.schematic.SchematicMarkerPosition;
import com.dslovikosky.narnia.common.utils.TeleportPlayerToPreTeleportPosition;
import com.google.common.collect.ImmutableSet;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.portal.DimensionTransition;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

import static com.dslovikosky.narnia.common.constants.ModCharacters.DIGORY;
import static com.dslovikosky.narnia.common.constants.ModCharacters.POLLY;

public class TheWrongDoorChapter extends Chapter {
    public TheWrongDoorChapter(final ResourceLocation id) {
        super(id, ModBooks.THE_MAGICIANS_NEPHEW, List.of(DIGORY, POLLY), ModDimensions.LONDON, new Vec3(2.5, 65, 4.5),
                ImmutableSet.of(new ChunkPos(0, 0), new ChunkPos(0, 1), new ChunkPos(1, 0), new ChunkPos(1, 1)));
        final SchematicMarkerPosition position = new SchematicMarkerPosition(ModSchematics.UNCLE_ANDREWS_HOUSE, new Vec3(0.0, 64.0, 0.0));
        addGoal(new PlayerInAABBChapterGoal(Component.literal("Walk in to Uncle Andrew's house yard"), true,
                () -> new AABB(0, 64, 10,
                        ModSchematics.UNCLE_ANDREWS_HOUSE.get().getWidth() - 1,
                        64 + ModSchematics.UNCLE_ANDREWS_HOUSE.get().getHeight() - 1,
                        ModSchematics.UNCLE_ANDREWS_HOUSE.get().getLength() - 1)));
        addGoal(new InstantiateActorChapterGoal(DIGORY, position.named("the_wrong_door:digory_spawn"), Vec3.directionFromRotation(0f, 270f)));
        addGoal(new InstantiateActorChapterGoal(POLLY, position.named("the_wrong_door:polly_spawn"), Vec3.directionFromRotation(0f, 90f)));
        addGoal(new ActorLookChapterGoal(DIGORY, POLLY));
        addGoal(new ActorLookChapterGoal(POLLY, DIGORY));
        addGoal(new ConversationChapterGoal(Component.literal("Digory meets Polly"),
                ChatLine.between(POLLY, DIGORY, Component.literal("Hullo")),
                ChatLine.between(DIGORY, POLLY, Component.literal("Hullo")),
                ChatLine.between(DIGORY, POLLY, Component.literal("What's your name?")),
                ChatLine.between(POLLY, DIGORY, Component.literal("Polly")),
                ChatLine.between(POLLY, DIGORY, Component.literal("What's yours?")),
                ChatLine.between(DIGORY, POLLY, Component.literal("Digory"))
        ));
        addGoal(new ActorMoveChapterGoal(DIGORY, position.named("the_wrong_door:hop_fence")));
        addGoal(new ConversationChapterGoal(Component.literal("Digory meets Polly"),
                ChatLine.between(POLLY, DIGORY, Component.literal("I say, what a funny name!")),
                ChatLine.between(DIGORY, POLLY, Component.literal("It isn't half so funny as Polly")),
                ChatLine.between(POLLY, DIGORY, Component.literal("Yes it is")),
                ChatLine.between(DIGORY, POLLY, Component.literal("No, it isn't")),
                ChatLine.between(POLLY, DIGORY, Component.literal("At any rate, I do wash my face")),
                ChatLine.between(POLLY, DIGORY, Component.literal("Which is what you need to do; especially after—"), 100),
                ChatLine.between(DIGORY, POLLY, Component.literal("All right, I have then")),
                ChatLine.between(DIGORY, POLLY, Component.literal("And so would you, if you'd lived all your life in the country, and had a pony, and a river at the bottom of the garden, and then been brought to live in a beastly Hole like this")),
                ChatLine.between(POLLY, DIGORY, Component.literal("London isn't a Hole")),
                ChatLine.between(DIGORY, POLLY, Component.literal("And your father was away in India — and you had to come and live with an Aunt and Uncle who's mad — and if the reason was that they were looking after your Mother — and if your Mother was ill and was going to")),
                ChatLine.between(DIGORY, POLLY, Component.literal("going to")),
                ChatLine.between(DIGORY, POLLY, Component.literal("die.")),
                ChatLine.between(POLLY, DIGORY, Component.literal("I didn't know. I'm sorry"))
        ));
        addGoal(new ParallelChapterGoal(
                new ActorMoveChapterGoal(DIGORY, position.named("the_wrong_door:polly_garden")),
                new ActorMoveChapterGoal(POLLY, position.named("the_wrong_door:digory_garden"))
        ));
        addGoal(new ConversationChapterGoal(Component.literal("Digory meets Polly"),
                ChatLine.between(POLLY, DIGORY, Component.literal("Is Mr Ketterley really mad?")),
                ChatLine.between(DIGORY, POLLY, Component.literal("Well he's either mad, or there's some other mystery. He has a study on the top floor and Aunt Letty says I must never go up there. Well, that looks fishy to begin with. And then there's another thing. Whenever he tries to say anything to me at meal times — he never even tries to talk to her — she always shuts him up. She says, 'Don't worry the boy, Andrew', or, 'I'm sure Digory doesn't want to hear about that', or else, 'Now, Digory, wouldn't you like to go out and play in the garden?'")),
                ChatLine.between(POLLY, DIGORY, Component.literal("What sort of things does he try to say?")),
                ChatLine.between(DIGORY, POLLY, Component.literal("I don't know. He never gets far enough. But there's more than that. One night — it was last night in fact — as I was going past the foot of the attic—stairs on my way to bed. I'm sure I heard a yell")),
                ChatLine.between(POLLY, DIGORY, Component.literal("Perhaps he keeps a mad wife shut up there")),
                ChatLine.between(DIGORY, POLLY, Component.literal("Yes, I've thought of that")),
                ChatLine.between(POLLY, DIGORY, Component.literal("Or perhaps he's a coiner")),
                ChatLine.between(DIGORY, POLLY, Component.literal("Or he might have been a pirate, like the man at the beginning of Treasure Island, and be always hiding from his old shipmates")),
                ChatLine.between(POLLY, DIGORY, Component.literal("How exciting! I never knew your house was so interesting")),
                ChatLine.between(DIGORY, POLLY, Component.literal("You may think it interesting, But you wouldn't like it if you had to sleep there. How would you like to lie awake listening for Uncle Andrew's step to come creeping along the passage to your room? And he has such awful eyes"))
        ));
        addGoal(new ParallelChapterGoal(
                new ActorMoveChapterGoal(DIGORY, position.named("the_wrong_door:digory_inside")),
                new ActorMoveChapterGoal(POLLY, position.named("the_wrong_door:polly_inside"))
        ));
        addGoal(new PlayerInAABBChapterGoal(Component.literal("Explore Polly's home"), false,
                () -> AABB.unitCubeFromLowerCorner(position.named("the_wrong_door:upstairs_detector").get())
                        .move(-0.5, 0, -0.5)
                        .inflate(10, 0, 10)
                        .expandTowards(0, 2, 0)));
        addGoal(new ParallelChapterGoal(
                new ActorMoveChapterGoal(POLLY, position.named("the_wrong_door:polly_upstairs")),
                new ActorMoveChapterGoal(DIGORY, position.named("the_wrong_door:digory_upstairs"))
        ));
        addGoal(new PlayerInAABBChapterGoal(Component.literal("Find the cistern to access the home's attic"), false,
                () -> AABB.unitCubeFromLowerCorner(position.named("the_wrong_door:cistern_ladder_up").get())
                        .move(-0.5, 1, -0.5)
                        .inflate(1, 1, 1)));
        addGoal(new ParallelChapterGoal(
                new ActorMoveChapterGoal(POLLY, position.named("the_wrong_door:cistern_ladder_up", new Vec3(0, 0, -1.2))),
                new ActorMoveChapterGoal(DIGORY, position.named("the_wrong_door:cistern_ladder_up", new Vec3(0, 0, -0.5)))
        ));
        addGoal(new PlayerInAABBChapterGoal(Component.literal("Enter the attic"), false,
                () -> AABB.unitCubeFromLowerCorner(position.named("the_wrong_door:attic_up").get())
                        .move(-0.5, 1, -0.5)
                        .inflate(1, 1, 1)));
        addGoal(new ParallelChapterGoal(
                new ActorMoveChapterGoal(POLLY, position.named("the_wrong_door:cistern_ladder_top"), false),
                new ActorMoveChapterGoal(DIGORY, position.named("the_wrong_door:cistern_ladder_top"), false)
        ));
        addGoal(new ParallelChapterGoal(
                new ActorMoveChapterGoal(POLLY, position.named("the_wrong_door:cistern_ladder_top", new Vec3(0, 0, 1)), false),
                new ActorMoveChapterGoal(DIGORY, position.named("the_wrong_door:cistern_ladder_top", new Vec3(0, 0, 1)), false)
        ));
        addGoal(new ParallelChapterGoal(
                new ActorMoveChapterGoal(POLLY, position.named("the_wrong_door:attic_up", new Vec3(1, 0, 0))),
                new ActorMoveChapterGoal(DIGORY, position.named("the_wrong_door:attic_up", new Vec3(2, 0, 0)))
        ));
        addGoal(new ConversationChapterGoal(Component.literal("Digory and Polly Explore"),
                ChatLine.between(DIGORY, POLLY, Component.literal("Look here, How long does this tunnel go on for? I mean, does it stop where your house ends?")),
                ChatLine.between(POLLY, DIGORY, Component.literal("No, The walls don't go out to the roof. It goes on. I don't know how far")),
                ChatLine.between(DIGORY, POLLY, Component.literal("Then we could get the length of the whole row of houses")),
                ChatLine.between(POLLY, DIGORY, Component.literal("So we could, And oh, I say!")),
                ChatLine.between(DIGORY, POLLY, Component.literal("What?")),
                ChatLine.between(POLLY, DIGORY, Component.literal("We could get into the other houses")),
                ChatLine.between(DIGORY, POLLY, Component.literal("Yes, and get taken up for burglars! No thanks")),
                ChatLine.between(POLLY, DIGORY, Component.literal("Don't be so jolly clever. I was thinking of the house beyond yours")),
                ChatLine.between(DIGORY, POLLY, Component.literal("What about it?")),
                ChatLine.between(POLLY, DIGORY, Component.literal("Why, it's the empty one. Daddy says it's always been empty since we came here")),
                ChatLine.between(DIGORY, POLLY, Component.literal("I suppose we ought to have a look at it then")),
                ChatLine.between(DIGORY, POLLY, Component.literal("Shall we go and try it now?")),
                ChatLine.between(POLLY, DIGORY, Component.literal("Alright")),
                ChatLine.between(DIGORY, POLLY, Component.literal("Don't if you'd rather not")),
                ChatLine.between(POLLY, DIGORY, Component.literal("I'm game if you are")),
                ChatLine.between(DIGORY, POLLY, Component.literal("How are we to know we're in the next house but one?"))
        ));
        addGoal(new ParallelChapterGoal(
                new ActorMoveChapterGoal(POLLY, position.named("the_wrong_door:attic_up")),
                new ActorMoveChapterGoal(DIGORY, position.named("the_wrong_door:attic_up"))
        ));
        addGoal(new ConversationChapterGoal(Component.literal("Digory and Polly Explore"),
                ChatLine.between(DIGORY, POLLY, Component.literal("But I don't expect it's really empty at all")),
                ChatLine.between(POLLY, DIGORY, Component.literal("What do you expect?")),
                ChatLine.between(DIGORY, POLLY, Component.literal("I expect someone lives there in secret, only coming in and out at night, with a dark lantern. We shall probably discover a gang of desperate criminals and get a reward. It's all rot to say a house would be empty all those years unless there was some mystery")),
                ChatLine.between(POLLY, DIGORY, Component.literal("Daddy thought it must be the drains")),
                ChatLine.between(DIGORY, POLLY, Component.literal("Pooh! Grown-ups are always thinking of uninteresting explanations")),
                ChatLine.between(POLLY, DIGORY, Component.literal("We mustn't make a sound"))
        ));
        addGoal(new PlayerInAABBChapterGoal(Component.literal("Cross the attic"), false,
                () -> AABB.unitCubeFromLowerCorner(position.named("the_wrong_door:attic_down").get())
                        .move(-0.5, 1, -0.5)
                        .inflate(1, 1, 1)));
        addGoal(new ParallelChapterGoal(
                new ActorMoveChapterGoal(POLLY, position.named("the_wrong_door:attic_down", new Vec3(1, 0, 0))),
                new ActorMoveChapterGoal(DIGORY, position.named("the_wrong_door:attic_down", new Vec3(2, 0, 0))),
                new ConversationChapterGoal(Component.literal("Digory and Polly Explore"),
                        ChatLine.between(POLLY, DIGORY, Component.literal("this must be halfway through our house")),
                        ChatLine.between(DIGORY, POLLY, Component.literal("We're opposite your attic now"))
                )
        ));
        addGoal(new PlayerInAABBChapterGoal(Component.literal("Climb into the abandoned house"), false,
                () -> AABB.unitCubeFromLowerCorner(position.named("the_wrong_door:cistern_ladder_down").get())
                        .move(-0.5, 1, -0.5)
                        .inflate(1, 1, 1)));
        addGoal(new ParallelChapterGoal(
                new ActorMoveChapterGoal(POLLY, position.named("the_wrong_door:attic_down", new Vec3(0, 0, 1)), 10),
                new ActorMoveChapterGoal(DIGORY, position.named("the_wrong_door:attic_down", new Vec3(0, 0, 1)), 10)
        ));
        addGoal(new ParallelChapterGoal(
                new ActorMoveChapterGoal(POLLY, position.named("the_wrong_door:cistern_ladder_down", new Vec3(0, 0, -1)), 100),
                new ActorMoveChapterGoal(DIGORY, position.named("the_wrong_door:cistern_ladder_down", new Vec3(0, 0, -1)), 100)
        ));
        addGoal(new ActorMoveChapterGoal(DIGORY, position.named("the_wrong_door:cistern_ladder_down")));
        addGoal(new ConversationChapterGoal(Component.literal("Digory and Polly enter the abandoned house"),
                ChatLine.between(DIGORY, POLLY, Component.literal("Shall I?")),
                ChatLine.between(POLLY, DIGORY, Component.literal("I'm game if you are"))
        ));
        addGoal(new ParallelChapterGoal(
                new ActorMoveChapterGoal(POLLY, position.named("the_wrong_door:digory_inside_study")),
                new ActorMoveChapterGoal(DIGORY, position.named("the_wrong_door:polly_inside_study"))
        ));
        addGoal(new ConversationChapterGoal(Component.literal("Digory and Polly enter the abandoned house"),
                ChatLine.between(POLLY, DIGORY, Component.literal("It's alright; there's no one here")),
                ChatLine.between(DIGORY, POLLY, Component.literal("This is no good, It's not an empty house at all. We'd better bunk before anyone comes.")),
                ChatLine.between(POLLY, DIGORY, Component.literal("What do you think those are?")),
                ChatLine.between(DIGORY, POLLY, Component.literal("Oh come on, the sooner---"))
        ));
        addGoal(new ParallelChapterGoal(
                new ConversationChapterGoal(Component.literal("Digory and Polly enter the abandoned house"),
                        ChatLine.of(DIGORY, Component.literal("O-o-oh"), 40)),
                new ConversationChapterGoal(Component.literal("Digory and Polly enter the abandoned house"),
                        ChatLine.of(POLLY, Component.literal("O-o-oh"), 40))
        ));
    }

    @Override
    public boolean join(Scene scene, Player player) {
        final boolean joined = super.join(scene, player);
        if (joined) {
            final Level level = player.level();
            if (!level.isClientSide()) {
                player.setData(ModAttachmentTypes.PRE_LONDON_TELEPORT_LOCATION, new PreTeleportLocation(player.position().x(), player.position().y(), player.position().z(), player.level().dimension()));
                final ServerLevel london = level.getServer().getLevel(scene.getSetting().dimension());
                player.changeDimension(new DimensionTransition(london, player, new TeleportPlayerToUncleAndrewsHouse(london, scene.getSetting().spawnPosition())));
            }
        }
        return joined;
    }

    @Override
    public boolean leave(Scene scene, Player player) {
        final boolean left = super.leave(scene, player);
        if (left) {
            final Level level = player.level();
            if (!level.isClientSide()) {
                final PreTeleportLocation preTeleportLocation = player.getData(ModAttachmentTypes.PRE_LONDON_TELEPORT_LOCATION);
                final ServerLevel preTeleportLevel = level.getServer().getLevel(preTeleportLocation.level());
                player.changeDimension(new DimensionTransition(preTeleportLevel, player, new TeleportPlayerToPreTeleportPosition(preTeleportLocation)));
            }
        }
        return left;
    }

    @ParametersAreNonnullByDefault
    private record TeleportPlayerToUncleAndrewsHouse(ServerLevel level, Vec3 playerSpawnSpot) implements DimensionTransition.PostDimensionTransition {
        @Override
        public void onTransition(final Entity entity) {
            if (entity instanceof ServerPlayer) {
                ((ServerPlayer) entity).connection.teleport(playerSpawnSpot.x(), playerSpawnSpot.y(), playerSpawnSpot.z(), 0f, 0f);
            } else {
                entity.setPos(playerSpawnSpot);
            }
        }
    }
}
