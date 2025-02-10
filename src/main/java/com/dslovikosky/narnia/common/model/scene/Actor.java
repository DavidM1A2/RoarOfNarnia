package com.dslovikosky.narnia.common.model.scene;

import com.dslovikosky.narnia.common.constants.ModRegistries;
import com.dslovikosky.narnia.common.utils.CustomStreamCodec;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

import java.util.UUID;

public class Actor {
    public static final Codec<Actor> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ModRegistries.CHARACTER.byNameCodec().fieldOf("character").forGetter(Actor::getCharacter),
            UUIDUtil.CODEC.fieldOf("entity_id").forGetter(Actor::getEntityId),
            Vec3.CODEC.fieldOf("target_position").forGetter(Actor::getTargetPosition),
            Vec3.CODEC.fieldOf("look_position").forGetter(Actor::getLookPosition),
            Codec.BOOL.fieldOf("can_use_doors").forGetter(Actor::isCanUseDoors)
    ).apply(instance, Actor::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, Actor> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.registry(ModRegistries.CHARACTER_KEY), Actor::getCharacter,
            UUIDUtil.STREAM_CODEC, Actor::getEntityId,
            CustomStreamCodec.VEC3, Actor::getTargetPosition,
            CustomStreamCodec.VEC3, Actor::getLookPosition,
            ByteBufCodecs.BOOL, Actor::isCanUseDoors,
            Actor::new);

    private final Character character;

    private UUID entityId;
    private Vec3 targetPosition;
    private Vec3 lookPosition;
    private boolean canUseDoors;

    private Actor(final Character character, final UUID entityId, final Vec3 targetPosition, final Vec3 lookPosition, final boolean canUseDoors) {
        this.character = character;
        this.entityId = entityId;
        this.targetPosition = targetPosition;
        this.lookPosition = lookPosition;
        this.canUseDoors = canUseDoors;
    }

    public Actor(final Character character) {
        this(character, new UUID(0, 0), Vec3.ZERO, Vec3.ZERO, true);
    }

    public Component getName() {
        if (character == null) {
            return Component.translatable("character.narnia.spectator.name");
        } else {
            return character.getName();
        }
    }

    public Character getCharacter() {
        return character;
    }

    public UUID getEntityId() {
        return entityId;
    }

    public void setEntity(final LivingEntity entity) {
        this.entityId = entity.getUUID();
    }

    public Vec3 getTargetPosition() {
        return targetPosition;
    }

    public void setTargetPosition(Vec3 targetPosition) {
        this.targetPosition = targetPosition;
    }

    public Vec3 getLookPosition() {
        return lookPosition;
    }

    public void setLookPosition(Vec3 lookPosition) {
        this.lookPosition = lookPosition;
    }

    public boolean isCanUseDoors() {
        return canUseDoors;
    }

    public void setCanUseDoors(final boolean canUseDoors) {
        this.canUseDoors = canUseDoors;
    }

    @Override
    public String toString() {
        return "Actor{" +
                "character=" + character +
                ", entityId=" + entityId +
                ", targetPosition=" + targetPosition +
                ", lookPosition=" + lookPosition +
                ", canUseDoors=" + canUseDoors +
                '}';
    }
}
