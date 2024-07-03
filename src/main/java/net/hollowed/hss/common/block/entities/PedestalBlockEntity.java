package net.hollowed.hss.common.block.entities;

import net.hollowed.hss.common.block.ModBlockEntities;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

public class PedestalBlockEntity extends BlockEntity implements ImplementedInventory {

    private final DefaultedList<ItemStack> items = DefaultedList.ofSize(1, ItemStack.EMPTY);

    public PedestalBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.PEDESTAL_BLOCK_ENTITY, pos, state);
    }

    public void drops() {
        if (world != null) {
            for (ItemStack stack : items) {
                ItemEntity itemEntity = new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), stack);
                world.spawnEntity(itemEntity);
            }
        }
    }

    @Override
    public void markDirty() {
        super.markDirty();
        if (world != null) {
            world.updateListeners(pos, getCachedState(), getCachedState(), Block.NOTIFY_ALL);
        }
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        Inventories.writeNbt(nbt, items);
        super.writeNbt(nbt);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        Inventories.readNbt(nbt, items);
    }

    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        return createNbt();
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return items;
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        items.set(slot, stack);
        markDirty(); // Mark the block entity as dirty to save the new state
        if (world != null && !world.isClient) {
            world.updateListeners(this.pos, this.getCachedState(), this.getCachedState(), Block.NOTIFY_ALL);
        }
    }

}
