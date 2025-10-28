package com.dslovikosky.narnia.common.model.data_serializer;

import com.dslovikosky.narnia.common.spell.Spell;
import io.netty.buffer.ByteBuf;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.nbt.NbtAccounter;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.syncher.EntityDataSerializer;

@MethodsReturnNonnullByDefault
public class SpellDataSerializer implements EntityDataSerializer<Spell> {
    private static final StreamCodec<ByteBuf, Spell> CODEC = StreamCodec.composite(
            ByteBufCodecs.compoundTagCodec(NbtAccounter::unlimitedHeap),
            Spell::serializeNbt,
            Spell::new
    );

    @Override
    public StreamCodec<? super RegistryFriendlyByteBuf, Spell> codec() {
        return CODEC;
    }

    @Override
    public Spell copy(Spell value) {
        return new Spell(value.serializeNbt());
    }
}
