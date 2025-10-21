package com.dslovikosky.narnia.common.model.attachment_type;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public class ThermalData {
    public static final Codec<ThermalData> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                            Codec.DOUBLE.fieldOf("vitae").forGetter(ThermalData::getVitae),
                            Codec.DOUBLE.fieldOf("heat").forGetter(ThermalData::getHeat)
                    )
                    .apply(instance, ThermalData::new)
    );
    public static final StreamCodec<FriendlyByteBuf, ThermalData> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.DOUBLE,
            ThermalData::getVitae,
            ByteBufCodecs.DOUBLE,
            ThermalData::getHeat,
            ThermalData::new
    );

    private double vitae;
    private double heat;

    public ThermalData(final double vitae, final double heat) {
        this.vitae = vitae;
        this.heat = heat;
    }

    public double getVitae() {
        return vitae;
    }

    public void setVitae(final double vitae) {
        this.vitae = vitae;
    }

    public double getHeat() {
        return heat;
    }

    public void setHeat(final double heat) {
        this.heat = heat;
    }
}
