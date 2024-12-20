package com.dslovikosky.narnia.common.item;

import com.dslovikosky.narnia.common.constants.Constants;
import com.dslovikosky.narnia.common.world.schematic.Schematic;
import com.dslovikosky.narnia.common.world.schematic.SchematicLoader;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.Optional;
import java.util.UUID;

public class DebugItem extends Item {
    public DebugItem() {
        super(new Properties().stacksTo(1));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(final Level pLevel, final Player pPlayer, final InteractionHand pUsedHand) {
        if (!pLevel.isClientSide()) {
            final Schematic schematic = SchematicLoader.load(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "schematic/uncle_andrews_house.schem"));
            final BlockPos position = pPlayer.blockPosition();
            for (int x = 0; x < schematic.width(); x++) {
                for (int y = 0; y < schematic.height(); y++) {
                    for (int z = 0; z < schematic.length(); z++) {
                        final int index = (y * schematic.length() * schematic.width()) + (z * schematic.width()) + x;
                        pLevel.setBlockAndUpdate(position.offset(x, y, z), schematic.blocks()[index]);
                    }
                }
            }
            for (int i = 0; i < schematic.blockEntities().size(); i++) {
                final CompoundTag blockEntityTag = schematic.blockEntities().getCompound(i);
                final int[] posTag = blockEntityTag.getIntArray("Pos");
                double x = posTag[0] + position.getX();
                double y = posTag[1] + position.getY();
                double z = posTag[2] + position.getZ();
                final BlockEntity blockEntity = pLevel.getBlockEntity(new BlockPos((int) x, (int) y, (int) z));
                if (blockEntity != null) {
                    blockEntity.loadWithComponents(blockEntityTag.getCompound("Data"), pLevel.registryAccess());
                }
            }
            for (int i = 0; i < schematic.entities().size(); i++) {
                final CompoundTag entityTag = schematic.entities().getCompound(i);

                String entityId = entityTag.getString("Id");
                Optional<EntityType<?>> entityTypeOpt = EntityType.byString(entityId);

                if (entityTypeOpt.isPresent()) {
                    final EntityType<?> entityType = entityTypeOpt.get();
                    ListTag posTag = entityTag.getList("Pos", Tag.TAG_DOUBLE);
                    double x = posTag.getDouble(0) + position.getX();
                    double y = posTag.getDouble(1) + position.getY();
                    double z = posTag.getDouble(2) + position.getZ();

                    final Entity entity = entityType.create(pLevel);
                    if (entity != null) {
                        final CompoundTag dataTag = entityTag.getCompound("Data");
                        dataTag.putString("id", entityId);
                        entity.load(dataTag);
                        entity.setUUID(UUID.randomUUID());
                        entity.setPos(x, y, z);
                        pLevel.addFreshEntity(entity);
                    }
                }
            }
            System.out.println(schematic);
        }

        return super.use(pLevel, pPlayer, pUsedHand);
    }
}
