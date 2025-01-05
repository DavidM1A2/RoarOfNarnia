package com.dslovikosky.narnia.common.entity.human_child;

import com.dslovikosky.narnia.common.model.NarniaGlobalData;
import com.dslovikosky.narnia.common.model.chapter.Scene;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Collections;
import java.util.UUID;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class HumanChildEntity extends LivingEntity implements SceneEntity {
    private final AnimationState idleAnimationState = new AnimationState();
    private final AnimationState talkAnimationState = new AnimationState();
    private UUID sceneId;

    public HumanChildEntity(final EntityType<? extends LivingEntity> entityType, final Level level) {
        super(entityType, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return LivingEntity.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 30D)
                .add(Attributes.MOVEMENT_SPEED, 0.35D)
                .add(Attributes.FOLLOW_RANGE, 24D);
    }

    @Override
    public void tick() {
        super.tick();

        if (!level().isClientSide()) {
            final Scene activeScene = NarniaGlobalData.getInstance(level()).getActiveScene();
            if (activeScene == null || !activeScene.getId().equals(sceneId)) {
                remove(RemovalReason.DISCARDED);
            }
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
}
