package com.dslovikosky.narnia.common.model;

import net.minecraft.util.StringRepresentable;

public enum CharnStatueType implements StringRepresentable {
    JADIS("jadis"),
    OTHER_1("other_1");

    private final String name;

    CharnStatueType(String name) {
        this.name = name;
    }

    @Override
    public String getSerializedName() {
        return name;
    }
}
