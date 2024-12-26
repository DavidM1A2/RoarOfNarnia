package com.dslovikosky.narnia.common.world.saved;

import com.dslovikosky.narnia.common.constants.Constants;
import com.dslovikosky.narnia.common.model.chapter.NarniaChapterInstance;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.saveddata.SavedData;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class ChapterSavedData extends SavedData {
    private NarniaChapterInstance runningInstance;

    /**
     * Create new with defaults
     */
    public ChapterSavedData() {
        this.runningInstance = null;
    }

    /**
     * Create new from NBT
     */
    public ChapterSavedData(final CompoundTag tag, final HolderLookup.Provider lookupProvider) {

    }

    public static ChapterSavedData get() {
        final MinecraftServer minecraftServer = ServerLifecycleHooks.getCurrentServer();
        if (minecraftServer == null) {
            return null;
        }
        return minecraftServer.overworld().getDataStorage().computeIfAbsent(new Factory<>(ChapterSavedData::new, ChapterSavedData::new),
                ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "chapter").toString().replace(':', '_'));
    }

    @Override
    @Nonnull
    public CompoundTag save(final CompoundTag pTag, final HolderLookup.Provider pRegistries) {
        return pTag;
    }
}
