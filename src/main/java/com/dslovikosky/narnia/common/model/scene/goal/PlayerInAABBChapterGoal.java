package com.dslovikosky.narnia.common.model.scene.goal;

import com.dslovikosky.narnia.common.model.scene.GoalTickResult;
import com.dslovikosky.narnia.common.model.scene.Scene;
import com.google.common.base.Suppliers;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import java.util.List;
import java.util.function.Supplier;

public class PlayerInAABBChapterGoal extends ChapterGoal {
    private final Component description;
    private final boolean allPlayersIn;
    private final Supplier<AABB> aabbSupplier;

    public PlayerInAABBChapterGoal(final Component description, final boolean allPlayersIn, final Supplier<AABB> aabbSupplier) {
        this.description = description;
        this.allPlayersIn = allPlayersIn;
        this.aabbSupplier = Suppliers.memoize(aabbSupplier::get);
    }

    @Override
    public GoalTickResult tick(Scene scene, Level level) {
        final List<Player> players = scene.getChapter().getPlayers(scene, level);

        if (players.isEmpty()) {
            return GoalTickResult.CONTINUE;
        }

        final long playersInAABB = players.stream().filter(player -> aabbSupplier.get().contains(player.position())).count();

        if (playersInAABB == players.size() || (!allPlayersIn && playersInAABB > 0)) {
            return GoalTickResult.COMPLETED;
        }

        return GoalTickResult.CONTINUE;
    }

    @Override
    public Component getDescription(Scene scene, Level level) {
        return description;
    }
}
