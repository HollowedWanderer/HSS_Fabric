package net.hollowed.hss.common.inventory;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;

public class BackSlotInventory implements Inventory {
    private ItemStack backSlot = ItemStack.EMPTY;

    @Override
    public int size() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return backSlot.isEmpty();
    }

    @Override
    public ItemStack getStack(int slot) {
        return slot == 0 ? backSlot : ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeStack(int slot, int amount) {
        if (slot == 0 && !backSlot.isEmpty()) {
            return backSlot.split(amount);
        }
        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeStack(int slot) {
        if (slot == 0) {
            ItemStack stack = backSlot;
            backSlot = ItemStack.EMPTY;
            return stack;
        }
        return ItemStack.EMPTY;
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        if (slot == 0) {
            backSlot = stack;
        }
    }

    @Override
    public void markDirty() {
        // Handle inventory changes if needed
    }

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        return true;
    }

    @Override
    public void clear() {
        backSlot = ItemStack.EMPTY;
    }
}
