package com.dslovikosky.narnia.common.model.scene;

import com.dslovikosky.narnia.common.constants.ModRegistries;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.LivingEntity;

import javax.annotation.Nullable;
import java.util.UUID;

public class Actor {
    public static final Codec<Actor> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ModRegistries.CHARACTER.byNameCodec().fieldOf("character").forGetter(Actor::getCharacter),
            UUIDUtil.CODEC.fieldOf("entity_id").forGetter(Actor::getEntityId)
    ).apply(instance, Actor::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, Actor> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.registry(ModRegistries.CHARACTER_KEY), Actor::getCharacter,
            UUIDUtil.STREAM_CODEC, Actor::getEntityId,
            Actor::new);

    private final Character character;
    private final UUID entityId;

    private Actor(final Character character, final UUID entityId) {
        this.character = character;
        this.entityId = entityId;
    }

    public Actor(final Character character, final LivingEntity entity) {
        this.character = character;
        this.entityId = entity.getUUID();
    }

    public Component getName() {
        if (character == null) {
            return Component.translatable("character.narnia.spectator.name");
        } else {
            return character.getName();
        }
    }

    public @Nullable Character getCharacter() {
        return character;
    }

    public UUID getEntityId() {
        return entityId;
    }

    @Override
    public String toString() {
        return "Actor[" +
                "character=" + character + ", " +
                "entityId=" + entityId + ']';
    }

}
