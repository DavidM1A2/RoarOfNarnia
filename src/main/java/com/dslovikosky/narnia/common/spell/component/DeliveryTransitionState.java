package com.dslovikosky.narnia.common.spell.component;

import com.dslovikosky.narnia.common.spell.Spell;
import com.dslovikosky.narnia.common.spell.SpellStage;
import com.dslovikosky.narnia.common.utils.UuidUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

import java.util.Optional;
import java.util.UUID;

public class DeliveryTransitionState {
    private static final String NBT_SPELL = "spell";
    private static final String NBT_STAGE_INDEX = "stage_index";
    private static final String NBT_WORLD = "world";
    private static final String NBT_POSITION = "position";
    private static final String NBT_NORMAL = "normal";
    private static final String NBT_BLOCK_POSITION = "block_position";
    private static final String NBT_DIRECTION = "direction";
    private static final String NBT_CASTER_ENTITY_ID = "caster_entity_id";
    private static final String NBT_ENTITY_ID = "entity_id";
    private static final String NBT_DELIVERY_ENTITY_ID = "delivery_entity_id";

    private final Spell spell;
    private final int stageIndex;
    private final Vec3 position;
    private final BlockPos blockPosition;
    private final Vec3 direction;
    private final Vec3 normal;
    private final ResourceKey<Level> level;
    private final UUID casterEntityId;
    private final UUID entityId;
    private final UUID deliveryEntityId;

    private DeliveryTransitionState(
            final Spell spell,
            final int stageIndex,
            final Vec3 position,
            final BlockPos blockPosition,
            final Vec3 direction,
            final Vec3 normal,
            final ResourceKey<Level> level,
            final UUID casterEntityId,
            final UUID entityId,
            final UUID deliveryEntityId) {
        this.spell = spell;
        this.stageIndex = stageIndex;
        this.position = position;
        this.blockPosition = blockPosition;
        this.direction = direction;
        this.normal = normal;
        this.level = level;
        this.casterEntityId = casterEntityId;
        this.entityId = entityId;
        this.deliveryEntityId = deliveryEntityId;
    }

    public DeliveryTransitionState(
            final Spell spell,
            final int stageIndex,
            final Level level,
            final Vec3 position,
            final BlockPos blockPosition,
            final Vec3 direction,
            final Vec3 normal,
            final Entity casterEntity,
            final Entity entity,
            final Entity deliveryEntity) {
        this(spell, stageIndex, position, blockPosition, direction, normal, level.dimension(),
                Optional.ofNullable(casterEntity).map(Entity::getUUID).orElse(null),
                Optional.ofNullable(entity).map(Entity::getUUID).orElse(null),
                Optional.ofNullable(deliveryEntity).map(Entity::getUUID).orElse(null));
    }

    public DeliveryTransitionState(final CompoundTag nbt) {
        this.spell = new Spell(nbt.getCompound(NBT_SPELL).get());
        this.stageIndex = nbt.getInt(NBT_STAGE_INDEX).get();
        this.level = ResourceKey.create(Registries.DIMENSION, ResourceLocation.parse(nbt.getString(NBT_WORLD).get()));
        this.position = new Vec3(
                nbt.getDouble(NBT_POSITION + "_x").get(),
                nbt.getDouble(NBT_POSITION + "_y").get(),
                nbt.getDouble(NBT_POSITION + "_z").get()
        );
        this.normal = new Vec3(
                nbt.getDouble(NBT_NORMAL + "_x").get(),
                nbt.getDouble(NBT_NORMAL + "_y").get(),
                nbt.getDouble(NBT_NORMAL + "_z").get()
        );
        this.blockPosition = new BlockPos(
                nbt.getInt(NBT_BLOCK_POSITION + "_x").get(),
                nbt.getInt(NBT_BLOCK_POSITION + "_y").get(),
                nbt.getInt(NBT_BLOCK_POSITION + "_z").get()
        );
        this.direction = new Vec3(
                nbt.getDouble(NBT_DIRECTION + "_x").get(),
                nbt.getDouble(NBT_DIRECTION + "_y").get(),
                nbt.getDouble(NBT_DIRECTION + "_z").get()
        );
        this.casterEntityId = UuidUtils.read(NBT_CASTER_ENTITY_ID, nbt).orElse(null);
        this.entityId = UuidUtils.read(NBT_ENTITY_ID, nbt).orElse(null);
        this.deliveryEntityId = UuidUtils.read(NBT_DELIVERY_ENTITY_ID, nbt).orElse(null);
    }

    public CompoundTag toTag() {
        final CompoundTag nbt = new CompoundTag();
        nbt.put(NBT_SPELL, spell.serializeNbt());
        nbt.putInt(NBT_STAGE_INDEX, stageIndex);
        nbt.putString(NBT_WORLD, level.location().toString());
        nbt.putDouble(NBT_POSITION + "_x", position.x);
        nbt.putDouble(NBT_POSITION + "_y", position.y);
        nbt.putDouble(NBT_POSITION + "_z", position.z);
        nbt.putInt(NBT_BLOCK_POSITION + "_x", blockPosition.getX());
        nbt.putInt(NBT_BLOCK_POSITION + "_y", blockPosition.getY());
        nbt.putInt(NBT_BLOCK_POSITION + "_z", blockPosition.getZ());
        nbt.putDouble(NBT_DIRECTION + "_x", direction.x);
        nbt.putDouble(NBT_DIRECTION + "_y", direction.y);
        nbt.putDouble(NBT_DIRECTION + "_z", direction.z);
        nbt.putDouble(NBT_NORMAL + "_x", normal.x);
        nbt.putDouble(NBT_NORMAL + "_y", normal.y);
        nbt.putDouble(NBT_NORMAL + "_z", normal.z);
        if (casterEntityId != null) {
            UuidUtils.write(NBT_CASTER_ENTITY_ID, nbt, casterEntityId);
        }
        if (entityId != null) {
            UuidUtils.write(NBT_ENTITY_ID, nbt, entityId);
        }
        if (deliveryEntityId != null) {
            UuidUtils.write(NBT_DELIVERY_ENTITY_ID, nbt, deliveryEntityId);
        }
        return nbt;
    }

    public DeliveryTransitionState copy(final int stageIndex, final Entity deliveryEntity) {
        return new DeliveryTransitionState(
                spell,
                stageIndex,
                position,
                blockPosition,
                direction,
                normal,
                level,
                casterEntityId,
                entityId,
                Optional.ofNullable(deliveryEntity).map(Entity::getUUID).orElse(null)
        );
    }

    public DeliveryTransitionState copy(final Vec3 direction, final Vec3 normal) {
        return new DeliveryTransitionState(
                spell,
                stageIndex,
                position,
                blockPosition,
                direction,
                normal,
                level,
                casterEntityId,
                entityId,
                deliveryEntityId
        );
    }

    public DeliveryTransitionState copy(final Vec3 position) {
        return new DeliveryTransitionState(
                spell,
                stageIndex,
                position,
                blockPosition,
                direction,
                normal,
                level,
                casterEntityId,
                entityId,
                deliveryEntityId
        );
    }

    public DeliveryTransitionState copy(final Vec3 position, final BlockPos blockPosition, final Vec3 direction, final Vec3 normal) {
        return new DeliveryTransitionState(
                spell,
                stageIndex,
                position,
                blockPosition,
                direction,
                normal,
                level,
                casterEntityId,
                entityId,
                deliveryEntityId
        );
    }

    public SpellStage getCurrentStage() {
        return this.spell.getStage(this.stageIndex);
    }

    public Spell getSpell() {
        return spell;
    }

    public int getStageIndex() {
        return stageIndex;
    }

    public Vec3 getPosition() {
        return position;
    }

    public BlockPos getBlockPosition() {
        return blockPosition;
    }

    public Vec3 getDirection() {
        return direction;
    }

    public Vec3 getNormal() {
        return normal;
    }

    public ServerLevel getLevel() {
        return ServerLifecycleHooks.getCurrentServer().getLevel(level);
    }

    public Entity getCasterEntity() {
        return getLevel().getEntity(casterEntityId);
    }

    public Entity getEntity() {
        return getLevel().getEntity(entityId);
    }

    public Entity getDeliveryEntity() {
        return getLevel().getEntity(deliveryEntityId);
    }
}
