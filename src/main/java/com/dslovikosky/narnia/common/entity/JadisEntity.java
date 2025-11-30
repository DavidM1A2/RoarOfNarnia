package com.dslovikosky.narnia.common.entity;

import com.dslovikosky.narnia.common.constants.ModEntityTypes;
import com.dslovikosky.narnia.common.model.chat.Audience;
import com.dslovikosky.narnia.common.model.chat.EntityChatEngine;
import net.minecraft.ChatFormatting;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

public class JadisEntity extends PathfinderMob {
    private final EntityChatEngine chatEngine = new EntityChatEngine(this, "jadis", ChatFormatting.DARK_PURPLE);

    private int lifetime = 0;

    public JadisEntity(final EntityType<JadisEntity> entityType, final Level level) {
        super(entityType, level);
        chatEngine.defineSequence("introduction", sequence -> sequence.audience(Audience.DEFAULT_NEARBY)
                .line(20 * 4)
                .line(20 * 8)
                .line(20 * 8)
                .line(20));
    }

    public JadisEntity(final Level level) {
        this(ModEntityTypes.JADIS.get(), level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return PathfinderMob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 100.0)
                .add(Attributes.MOVEMENT_SPEED, 4)
                .add(Attributes.FOLLOW_RANGE, 64.0);
    }

    @Override
    public void tick() {
        super.tick();
        lifetime++;
        chatEngine.tick();

        if (lifetime == 100) {
            chatEngine.playSequence("introduction");
        }
    }

    @Override
    protected void addAdditionalSaveData(final ValueOutput valueOutput) {
        super.addAdditionalSaveData(valueOutput);
        valueOutput.putInt("lifetime", lifetime);
        chatEngine.save(valueOutput);
    }

    @Override
    protected void readAdditionalSaveData(final ValueInput valueInput) {
        super.readAdditionalSaveData(valueInput);
        this.lifetime = valueInput.getIntOr("lifetime", 0);
        chatEngine.load(valueInput);
    }
}
