package com.dslovikosky.narnia.common.entity.human_child;

import com.dslovikosky.narnia.common.model.NarniaGlobalData;
import com.dslovikosky.narnia.common.model.scene.Actor;
import com.dslovikosky.narnia.common.model.scene.Scene;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Collections;
import java.util.UUID;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class HumanChildEntity extends Mob implements SceneEntity {
    private final AnimationState idleAnimationState = new AnimationState();
    private final AnimationState talkAnimationState = new AnimationState();
    private UUID sceneId;

    public HumanChildEntity(final EntityType<? extends Mob> entityType, final Level level) {
        super(entityType, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 30D)
                .add(Attributes.MOVEMENT_SPEED, 0.8D)
                .add(Attributes.FOLLOW_RANGE, 24D);
    }

    @Override
    public void tick() {
        super.tick();

        if (!level().isClientSide()) {
            final Scene activeScene = NarniaGlobalData.getInstance(level()).getActiveScene();
            if (activeScene == null || !activeScene.getId().equals(sceneId)) {
                remove(RemovalReason.DISCARDED);
                return;
            }
            final Actor actor = activeScene.getChapter().getActor(activeScene, this);
            final Vec3 targetPosition = actor.getTargetPosition();
            final GroundPathNavigation groundNavigation = (GroundPathNavigation) this.getNavigation();
            groundNavigation.setCanOpenDoors(true);
            groundNavigation.setCanPassDoors(true);
            groundNavigation.moveTo(targetPosition.x, targetPosition.y, targetPosition.z, 0.35);
        }

        if (level().isClientSide()) {
            if (!this.idleAnimationState.isStarted()) {
                this.idleAnimationState.start(this.tickCount);
            }
        }
    }

    public AnimationState getIdleAnimationState() {
        return idleAnimationState;
    }

    public AnimationState getTalkAnimationState() {
        return talkAnimationState;
    }

    @Override
    public UUID getSceneId() {
        return sceneId;
    }

    @Override
    public void setSceneId(UUID sceneId) {
        this.sceneId = sceneId;
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
}
