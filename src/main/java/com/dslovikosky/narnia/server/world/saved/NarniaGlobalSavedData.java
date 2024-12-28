package com.dslovikosky.narnia.server.world.saved;

import com.dslovikosky.narnia.common.constants.Constants;
import com.dslovikosky.narnia.common.model.NarniaGlobalData;
import com.dslovikosky.narnia.common.model.chapter.Scene;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.saveddata.SavedData;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class NarniaGlobalSavedData extends SavedData {
    private static final String NBT_ACTIVE_CHAPTER = "active_chapter";

    private NarniaGlobalSavedData() {
    }

    private NarniaGlobalSavedData(final CompoundTag tag, final HolderLookup.Provider lookupProvider) {
        if (tag.contains(NBT_ACTIVE_CHAPTER)) {
            NarniaGlobalData.getInstance(false).setActiveScene(Scene.CODEC.decode(NbtOps.INSTANCE, tag.getCompound(NBT_ACTIVE_CHAPTER)).getOrThrow().getFirst());
        }
    }

    public static NarniaGlobalSavedData get() {
        return get(ServerLifecycleHooks.getCurrentServer());
    }

    public static NarniaGlobalSavedData get(@Nullable final MinecraftServer server) {
        if (server == null) {
            return null;
        }
        return server.overworld().getDataStorage().computeIfAbsent(new Factory<>(NarniaGlobalSavedData::new, NarniaGlobalSavedData::new),
                ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "global").toString().replace(':', '_'));
    }

    @Override
    @Nonnull
    public CompoundTag save(final CompoundTag pTag, final HolderLookup.Provider pRegistries) {
        final Scene activeChapter = NarniaGlobalData.getInstance(false).getActiveScene();
        if (activeChapter != null) {
            pTag.put(NBT_ACTIVE_CHAPTER, Scene.CODEC.encodeStart(NbtOps.INSTANCE, activeChapter).getOrThrow());
        }
        return pTag;
    }
}
