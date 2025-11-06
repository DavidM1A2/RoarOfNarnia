package com.dslovikosky.narnia.common.model.attachment_type;

import com.dslovikosky.narnia.common.utils.UuidUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.UUID;

public final class SpellCharmData {
    public static final Codec<SpellCharmData> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                            Codec.INT.fieldOf("charm_ticks").forGetter(SpellCharmData::getCharmTicks),
                            UuidUtils.CODEC.fieldOf("charming_entity_id").forGetter(SpellCharmData::getCharmingEntityId)
                    )
                    .apply(instance, SpellCharmData::new)
    );
    public static final StreamCodec<FriendlyByteBuf, SpellCharmData> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT,
            SpellCharmData::getCharmTicks,
            UuidUtils.STREAM_CODEC,
            SpellCharmData::getCharmingEntityId,
            SpellCharmData::new
    );
    private final UUID charmingEntityId;
    private int charmTicks;

    public SpellCharmData(int charmTicks, UUID charmingEntityId) {
        this.charmTicks = charmTicks;
        this.charmingEntityId = charmingEntityId;
    }

    public int getCharmTicks() {
        return charmTicks;
    }

    public void setCharmTicks(int charmTicks) {
        this.charmTicks = charmTicks;
    }

    public UUID getCharmingEntityId() {
        return charmingEntityId;
    }
}
