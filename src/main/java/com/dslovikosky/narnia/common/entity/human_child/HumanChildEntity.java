package com.dslovikosky.narnia.common.entity.human_child;

import com.dslovikosky.narnia.common.model.NarniaGlobalData;
import com.dslovikosky.narnia.common.model.scene.Actor;
import com.dslovikosky.narnia.common.model.scene.Scene;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.OpenDoorGoal;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class HumanChildEntity extends Mob implements SceneEntity {
    private static final EntityDataAccessor<Optional<UUID>> SCENE_ID = SynchedEntityData.defineId(HumanChildEntity.class, EntityDataSerializers.OPTIONAL_UUID);
    private static final Logger LOG = LogManager.getLogger();
    private final AnimationState idleAnimationState = new AnimationState();
    private final AnimationState talkAnimationState = new AnimationState();

    public HumanChildEntity(final EntityType<? extends Mob> entityType, final Level level) {
        super(entityType, level);
        final GroundPathNavigation groundNavigation = (GroundPathNavigation) this.getNavigation();
        groundNavigation.setCanOpenDoors(true);
        groundNavigation.setCanPassDoors(true);
        // No despawning!
        setPersistenceRequired();
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 30D)
                .add(Attributes.MOVEMENT_SPEED, 0.8D)
                .add(Attributes.FOLLOW_RANGE, 24D);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder pBuilder) {
        super.defineSynchedData(pBuilder);
        pBuilder.define(SCENE_ID, Optional.empty());
    }

    @Override
    public void tick() {
        super.tick();

        if (!level().isClientSide()) {
            final Scene activeScene = NarniaGlobalData.getInstance(level()).getActiveScene();
            if (activeScene == null || !activeScene.getId().equals(getSceneId().orElse(null))) {
                remove(RemovalReason.DISCARDED);
                return;
            }
            final Actor actor = activeScene.getChapter().getActor(activeScene, this);
            if (actor == null) {
                LOG.error("Entity was: {}", getUUID());
                LOG.error("Scene had: {}", activeScene.getActors().values());
            }
            final Vec3 targetPosition = new Vec3(actor.getTargetPosition().x(), actor.getTargetPosition().y(), actor.getTargetPosition().z());
            getNavigation().moveTo(targetPosition.x, targetPosition.y, targetPosition.z, 0.35);
        }

        if (level().isClientSide()) {
            if (!this.idleAnimationState.isStarted()) {
                this.idleAnimationState.start(this.tickCount);
            }
        }
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new OpenDoorGoal(this, true));
    }

    public AnimationState getIdleAnimationState() {
        return idleAnimationState;
    }

    public AnimationState getTalkAnimationState() {
        return talkAnimationState;
    }

    @Override
    public Optional<UUID> getSceneId() {
        return getEntityData().get(HumanChildEntity.SCENE_ID);
    }

    @Override
    public void setSceneId(@Nonnull UUID sceneId) {
        getEntityData().set(HumanChildEntity.SCENE_ID, Optional.of(sceneId));
    }

    @Override
    public Iterable<ItemStack> getArmorSlots() {
        return Collections.emptyList();
    }

    @Override
    public ItemStack getItemBySlot(EquipmentSlot pSlot) {
        return ItemStack.EMPTY;
    }

    @Override
    public void setItemSlot(EquipmentSlot pSlot, ItemStack pStack) {
    }

    @Override
    public HumanoidArm getMainArm() {
        return HumanoidArm.RIGHT;
    }

    @Override
    public boolean mayBeLeashed() {
        return false;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        getSceneId().ifPresent(sceneId -> pCompound.putUUID("scene_id", sceneId));
    }

    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        if (pCompound.contains("scene_id")) {
            setSceneId(pCompound.getUUID("scene_id"));
        }
    }
}
