package com.dslovikosky.narnia.common.chapter;

import com.dslovikosky.narnia.common.chapter.goal.ActorMoveAtUncleAndrewsHouseGoal;
import com.dslovikosky.narnia.common.chapter.goal.InstantiateAtUncleAndrewsHouseGoal;
import com.dslovikosky.narnia.common.chapter.goal.LocateUncleAndrewsHouseGoal;
import com.dslovikosky.narnia.common.constants.ModBooks;
import com.dslovikosky.narnia.common.model.scene.Chapter;
import com.dslovikosky.narnia.common.model.scene.goal.ChatLine;
import com.dslovikosky.narnia.common.model.scene.goal.ConversationChapterGoal;
import com.dslovikosky.narnia.common.model.scene.goal.ParallelChapterGoal;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;

import java.util.List;

import static com.dslovikosky.narnia.common.constants.ModCharacters.DIGORY;
import static com.dslovikosky.narnia.common.constants.ModCharacters.POLLY;

public class TheWrongDoorChapter extends Chapter {
    public TheWrongDoorChapter(final ResourceLocation id) {
        super(id, ModBooks.THE_MAGICIANS_NEPHEW, List.of(DIGORY, POLLY));
        addGoal(new LocateUncleAndrewsHouseGoal());
        addGoal(new InstantiateAtUncleAndrewsHouseGoal(DIGORY, new Vec3(10, 1, 20), Vec3.directionFromRotation(0f, 270f)));
        addGoal(new InstantiateAtUncleAndrewsHouseGoal(POLLY, new Vec3(16, 1, 20), Vec3.directionFromRotation(0f, 90f)));
        addGoal(new ConversationChapterGoal(Component.literal("Digory meets Polly"),
                ChatLine.between(POLLY, DIGORY, Component.literal("Hullo")),
                ChatLine.between(DIGORY, POLLY, Component.literal("Hullo")),
                ChatLine.between(DIGORY, POLLY, Component.literal("What's your name?")),
                ChatLine.between(POLLY, DIGORY, Component.literal("Polly")),
                ChatLine.between(POLLY, DIGORY, Component.literal("What's yours?")),
                ChatLine.between(DIGORY, POLLY, Component.literal("Digory"))
        ));
        addGoal(new ActorMoveAtUncleAndrewsHouseGoal(DIGORY, new Vec3(15, 1, 20)));
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
                new ActorMoveAtUncleAndrewsHouseGoal(DIGORY, new Vec3(18, 1, 22)),
                new ActorMoveAtUncleAndrewsHouseGoal(POLLY, new Vec3(19, 1, 21))
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
                new ActorMoveAtUncleAndrewsHouseGoal(DIGORY, new Vec3(18, 1, 11)),
                new ActorMoveAtUncleAndrewsHouseGoal(POLLY, new Vec3(19, 1, 12))
        ));
        addGoal(new ConversationChapterGoal(Component.literal("Digory meets Polly"),
                ChatLine.between(DIGORY, POLLY, Component.literal("Well he's either mad, or there's some other mystery. He has a study on the top floor and Aunt Letty says I must never go up there. Well, that looks fishy to begin with. And then there's another thing. Whenever he tries to say anything to me at meal times — he never even tries to talk to her — she always shuts him up. She says, 'Don't worry the boy, Andrew', or, 'I'm sure Digory doesn't want to hear about that', or else, 'Now, Digory, wouldn't you like to go out and play in the garden?'"))
        ));
    }
}
