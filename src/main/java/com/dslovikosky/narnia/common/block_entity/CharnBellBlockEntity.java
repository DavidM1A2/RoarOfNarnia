package com.dslovikosky.narnia.common.block_entity;

import com.dslovikosky.narnia.client.proxy.ClientProxy;
import com.dslovikosky.narnia.common.constants.ModBlockEntities;
import com.dslovikosky.narnia.common.constants.ModSoundEvents;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.ARGB;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import javax.annotation.ParametersAreNonnullByDefault;
import java.time.Duration;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class CharnBellBlockEntity extends BlockEntity {
    private static final Duration RING_DURATION = Duration.ofSeconds(14);

    private boolean hitNorth = true;
    private long lastHitTime = 0;

    public CharnBellBlockEntity(final BlockPos pos, final BlockState blockState) {
        super(ModBlockEntities.CHARN_BELL.get(), pos, blockState);
    }

    public static void tick(final Level level, final BlockPos pos, final BlockState state, final BlockEntity blockEntity) {
        if (!(blockEntity instanceof CharnBellBlockEntity charnBellBlockEntity)) {
            return;
        }

        if (!charnBellBlockEntity.isRinging(level)) {
            return;
        }

        final long ticksRinging = charnBellBlockEntity.getTicksRinging(level);
        if (ticksRinging == 0) {
            level.playPlayerSound(ModSoundEvents.CHARN_BELL_RING.get(), SoundSource.BLOCKS, 1f, 1f);
        }
        if (ticksRinging == 110) {
            level.playPlayerSound(ModSoundEvents.CHARN_EARTH_SHAKE.get(), SoundSource.BLOCKS, 1f, 1f);
            if (level.isClientSide()) {
                ClientProxy.CHARN_SCREEN_SHAKE_HANDLER.start(0.0f, 8f, 9f);
            }
        }
        if (ticksRinging > 110) {
            if (level.isClientSide()) {
                final int particlesAtStage = (ticksRinging > 220) ? 3 : (ticksRinging > 180) ? 2 : 1;
                for (int i = 0; i < particlesAtStage; i++) {
                    final LocalPlayer player = Minecraft.getInstance().player;
                    final RandomSource random = player.getRandom();
                    level.addParticle(new DustParticleOptions(ARGB.color(100, 100, 100), 4f),
                            player.getRandomX(10),
                            player.getY((random.nextDouble() - 0.25) * 4),
                            player.getRandomZ(10),
                            (random.nextDouble() - 0.5) * 0.5,
                            -random.nextDouble() * 0.2 - 0.2,
                            (random.nextDouble() - 0.5) * 0.5);
                }
            }
        }
        if (ticksRinging == RING_DURATION.toSeconds() * 20 - 10) {
            if (!level.isClientSide()) {
                final LightningBolt lightningBolt = new LightningBolt(EntityType.LIGHTNING_BOLT, level);
                lightningBolt.setPos(pos.getX() + 0.5, pos.getY() + 1.5, pos.getZ() + 0.5);
                level.addFreshEntity(lightningBolt);
            }
        }
    }

    public boolean isRinging(final Level level) {
        return getTicksRinging(level) <= RING_DURATION.toSeconds() * 20;
    }

    public long getTicksRinging(final Level level) {
        return level.getGameTime() - lastHitTime;
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return this.saveWithoutMetadata(registries);
    }

    @Override
    public void saveWithoutMetadata(ValueOutput output) {
        super.saveWithoutMetadata(output);
        output.putBoolean("hitNorth", hitNorth);
        output.putLong("lastHitTime", lastHitTime);
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void onDataPacket(Connection net, ValueInput valueInput) {
        super.onDataPacket(net, valueInput);
        this.hitNorth = valueInput.getBooleanOr("hitNorth", true);
        this.lastHitTime = valueInput.getLongOr("lastHitTime", 0);
    }

    public boolean isHitNorth() {
        return hitNorth;
    }

    public void setHitNorth(final boolean hitNorth) {
        this.hitNorth = hitNorth;
    }

    public long getLastHitTime() {
        return lastHitTime;
    }

    public void setLastHitTime(final long lastHitTime) {
        this.lastHitTime = lastHitTime;
    }
}
