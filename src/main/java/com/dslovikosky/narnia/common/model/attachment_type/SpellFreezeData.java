package com.dslovikosky.narnia.common.model.attachment_type;

import com.dslovikosky.narnia.common.utils.CustomCodec;
import com.dslovikosky.narnia.common.utils.CustomStreamCodec;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.phys.Vec3;

public final class SpellFreezeData {
    public static final Codec<SpellFreezeData> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                            Codec.INT.fieldOf("freeze_ticks").forGetter(SpellFreezeData::getFreezeTicks),
                            CustomCodec.VEC3.fieldOf("freeze_position").forGetter(SpellFreezeData::getFreezePosition),
                            Codec.FLOAT.fieldOf("freeze_pitch").forGetter(SpellFreezeData::getFreezePitch),
                            Codec.FLOAT.fieldOf("freeze_yaw").forGetter(SpellFreezeData::getFreezeYaw)
                    )
                    .apply(instance, SpellFreezeData::new)
    );
    public static final StreamCodec<FriendlyByteBuf, SpellFreezeData> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT,
            SpellFreezeData::getFreezeTicks,
            CustomStreamCodec.VEC3,
            SpellFreezeData::getFreezePosition,
            ByteBufCodecs.FLOAT,
            SpellFreezeData::getFreezePitch,
            ByteBufCodecs.FLOAT,
            SpellFreezeData::getFreezeYaw,
            SpellFreezeData::new
    );

    private int freezeTicks;
    private Vec3 freezePosition;
    private float freezePitch;
    private float freezeYaw;

    public SpellFreezeData(int freezeTicks, Vec3 freezePosition, float freezePitch, float freezeYaw) {
        this.freezeTicks = freezeTicks;
        this.freezePosition = freezePosition;
        this.freezePitch = freezePitch;
        this.freezeYaw = freezeYaw;
    }

    public int getFreezeTicks() {
        return freezeTicks;
    }

    public void setFreezeTicks(int freezeTicks) {
        this.freezeTicks = freezeTicks;
    }

    public Vec3 getFreezePosition() {
        return freezePosition;
    }

    public void setFreezePosition(Vec3 freezePosition) {
        this.freezePosition = freezePosition;
    }

    public float getFreezePitch() {
        return freezePitch;
    }

    public void setFreezePitch(float freezePitch) {
        this.freezePitch = freezePitch;
    }

    public float getFreezeYaw() {
        return freezeYaw;
    }

    public void setFreezeYaw(float freezeYaw) {
        this.freezeYaw = freezeYaw;
    }
}
