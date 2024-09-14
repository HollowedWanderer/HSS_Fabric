package net.hollowed.hss.mixin.slots;


import com.google.common.collect.ImmutableList;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.collection.DefaultedList;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@Mixin(value = PlayerInventory.class, priority = 999)
public abstract class PlayerInventoryMixin implements Inventory {
    @Shadow
    @Final
    @Mutable
    private List<DefaultedList<ItemStack>> combinedInventory;
    @Shadow
    @Mutable
    @Final
    public PlayerEntity player;

    @Unique
    private DefaultedList<ItemStack> extraSlot1;

    @Unique
    private DefaultedList<ItemStack> extraSlot2;

    public PlayerInventoryMixin(PlayerEntity player) {
    }

    @Inject(method = "<init>*", at = @At("RETURN"))
    private void initMixin(PlayerEntity playerEntity, CallbackInfo info) {
        this.extraSlot1 = DefaultedList.ofSize(1, ItemStack.EMPTY);
        this.extraSlot2 = DefaultedList.ofSize(1, ItemStack.EMPTY);
        this.combinedInventory = new ArrayList<>(combinedInventory);
        this.combinedInventory.add(extraSlot1);
        this.combinedInventory.add(extraSlot2);
        this.combinedInventory = ImmutableList.copyOf(this.combinedInventory);
    }

    @Inject(method = "writeNbt", at = @At("TAIL"))
    public void writeNbtMixin(NbtList tag, CallbackInfoReturnable<NbtList> info) {
        if (!this.extraSlot1.get(0).isEmpty()) {
            NbtCompound compoundTag = new NbtCompound();
            compoundTag.putByte("Slot", (byte) (110));
            tag.add(this.extraSlot1.get(0).writeNbt(compoundTag));
        }
        if (!this.extraSlot2.get(0).isEmpty()) {
            NbtCompound compoundTag = new NbtCompound();
            compoundTag.putByte("Slot", (byte) (111));
            tag.add(this.extraSlot2.get(0).writeNbt(compoundTag));
        }
    }

    @Inject(method = "readNbt", at = @At("TAIL"))
    public void readNbtMixin(NbtList tag, CallbackInfo info) {
        this.extraSlot1.clear();
        this.extraSlot2.clear();
        for (int i = 0; i < tag.size(); ++i) {
            NbtCompound compoundTag = tag.getCompound(i);
            int slot = compoundTag.getByte("Slot") & 255;
            ItemStack itemStack = ItemStack.fromNbt(compoundTag);
            if (!itemStack.isEmpty()) {
                if (slot >= 110 && slot < this.extraSlot1.size() + 110) {
                    this.extraSlot1.set(slot - 110, itemStack);
                } else if (slot >= 111 && slot < this.extraSlot2.size() + 111) {
                    this.extraSlot2.set(slot - 111, itemStack);
                }
            }
        }
    }

    @Inject(method = "size", at = @At("RETURN"), cancellable = true)
    public void sizeMixin(CallbackInfoReturnable<Integer> info) {
        info.setReturnValue(info.getReturnValue() + 2);
    }

    @Inject(method = "isEmpty", at = @At("TAIL"), cancellable = true)
    public void isEmptyMixin(CallbackInfoReturnable<Boolean> info) {
        if (!this.extraSlot1.isEmpty() || !this.extraSlot2.isEmpty()) {
            info.setReturnValue(false);
        }
    }
}

