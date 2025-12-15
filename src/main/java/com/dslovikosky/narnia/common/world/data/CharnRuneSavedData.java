package com.dslovikosky.narnia.common.world.data;

import com.dslovikosky.narnia.common.block.RuneBlock;
import com.dslovikosky.narnia.common.utils.CustomCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;

import java.util.HashSet;
import java.util.Set;

public class CharnRuneSavedData extends SavedData {
    public static final SavedDataType<CharnRuneSavedData> ID = new SavedDataType<>(
            "charn_rune",
            CharnRuneSavedData::new,
            RecordCodecBuilder.create(instance -> instance.group(
                    CustomCodec.set(StringRepresentable.fromEnum(RuneBlock.Color::values)).fieldOf("broken_runes").forGetter(CharnRuneSavedData::getBrokenRunes)
            ).apply(instance, CharnRuneSavedData::new))
    );

    private final Set<RuneBlock.Color> brokenRunes;

    private CharnRuneSavedData() {
        this.brokenRunes = new HashSet<>();
    }

    private CharnRuneSavedData(final Set<RuneBlock.Color> brokenRunes) {
        this.brokenRunes = new HashSet<>(brokenRunes);
    }

    public void setRune(RuneBlock.Color color, final boolean broken) {
        if (broken) {
            brokenRunes.add(color);
        } else {
            brokenRunes.remove(color);
        }
        setDirty();
    }

    public boolean isRuneBroken(RuneBlock.Color color) {
        return brokenRunes.contains(color);
    }

    private Set<RuneBlock.Color> getBrokenRunes() {
        return brokenRunes;
    }
}
