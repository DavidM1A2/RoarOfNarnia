package com.dslovikosky.narnia.common.model.chapter;

import com.dslovikosky.narnia.common.constants.ModRegistries;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.UUID;

public final class Actor {
    public static final Codec<Actor> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ModRegistries.CHARACTER.byNameCodec().optionalFieldOf("character").forGetter(it -> Optional.ofNullable(it.getCharacter())),
            Codec.BOOL.fieldOf("player_controlled").forGetter(Actor::isPlayerControlled),
            UUIDUtil.CODEC.fieldOf("entity_id").forGetter(Actor::getEntityId)
    ).apply(instance, Actor::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, Actor> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.optional(ByteBufCodecs.registry(ModRegistries.CHARACTER_KEY)), it -> Optional.ofNullable(it.getCharacter()),
            ByteBufCodecs.BOOL, Actor::isPlayerControlled,
            UUIDUtil.STREAM_CODEC, Actor::getEntityId,
            Actor::new);

    private final Character character;
    private final boolean playerControlled;
    private final UUID entityId;

    private Actor(final Optional<Character> character, final boolean playerControlled, final UUID entityId) {
        this.character = character.orElse(null);
        this.playerControlled = playerControlled;
        this.entityId = entityId;
    }

    public Actor(final Character character, final Player player) {
        this(Optional.of(character), true, player.getUUID());
    }

    public Actor(final Player player) {
        this(Optional.empty(), true, player.getUUID());
    }

    public Actor(final Character character, final Entity entity) {
        this(Optional.of(character), false, entity.getUUID());
    }

    public Component getName() {
        if (character == null) {
            return Component.translatable("character.narnia.spectator.name");
        } else {
            return character.getName();
        }
    }

    public boolean represents(@Nullable final Character other) {
        return character == other;
    }

    public boolean representedBy(final Player player) {
        return entityId.equals(player.getUUID());
    }

    public Entity getEntity(final Level level) {
        if (level instanceof ServerLevel serverLevel) {
            return serverLevel.getEntity(entityId);
        }
        return null;
    }

    public Character getCharacter() {
        return character;
    }

    public boolean isPlayerControlled() {
        return playerControlled;
    }

    public UUID getEntityId() {
        return entityId;
    }

    @Override
    public String toString() {
        return "Actor[" +
                "character=" + character + ", " +
                "playerControlled=" + playerControlled + ", " +
                "entityId=" + entityId + ']';
    }

}
