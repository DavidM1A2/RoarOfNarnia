package com.dslovikosky.narnia.common.model.chapter;

import com.dslovikosky.narnia.common.constants.ModRegistries;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.UUID;

public record Actor(Character character, boolean playerControlled, UUID entityId) {
    public static final Codec<Actor> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ModRegistries.CHARACTER.byNameCodec().fieldOf("character").forGetter(Actor::character),
            Codec.BOOL.fieldOf("player_controlled").forGetter(Actor::playerControlled),
            UUIDUtil.CODEC.fieldOf("entity_id").forGetter(Actor::entityId)
    ).apply(instance, Actor::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, Actor> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.registry(ModRegistries.CHARACTER_KEY), Actor::character,
            ByteBufCodecs.BOOL, Actor::playerControlled,
            UUIDUtil.STREAM_CODEC, Actor::entityId,
            Actor::new);

    public Actor(final Character character, final Player player) {
        this(character, true, player.getUUID());
    }

    public Actor(final Character character, final Entity entity) {
        this(character, false, entity.getUUID());
    }

    public Entity getEntity(final Level level) {
        if (level instanceof ServerLevel serverLevel) {
            return serverLevel.getEntity(entityId);
        }
        return null;
    }
}
