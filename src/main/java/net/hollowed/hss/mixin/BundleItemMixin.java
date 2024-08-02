package net.hollowed.hss.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BundleItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtElement;
import net.minecraft.registry.Registries;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.*;

import java.util.List;
import java.util.Optional;

@Mixin(BundleItem.class)
public abstract class BundleItemMixin {

    @Shadow
    private static int getBundleOccupancy(ItemStack stack) {
        return 0; // Placeholder
    }

    @Shadow
    private static int getItemOccupancy(ItemStack stack) {
        return 0; // Placeholder
    }

    @Shadow
    private static Optional<NbtCompound> canMergeStack(ItemStack stack, NbtList items) {
        return Optional.empty(); // Placeholder
    }

    @Final
    @Shadow
    public static final int MAX_STORAGE = 256;

    @Shadow
    private static Optional<ItemStack> removeFirstStack(ItemStack stack) {
        return Optional.empty(); // Placeholder
    }

    @Shadow
    private static boolean dropAllBundledItems(ItemStack stack, PlayerEntity player) {
        return false; // Placeholder
    }

    @Unique
    private static final int MAX_ITEM_TYPES = 64;

    @Unique
    private static int currentItemIndex = 0;

    /**
     * @author Hollowed
     * @reason bundles
     */
    @Overwrite
    public static float getAmountFilled(ItemStack stack) {
        return (float) getBundleOccupancy(stack) / MAX_STORAGE;
    }

    /**
     * @author Hollowed
     * @reason bundles
     */
    @Overwrite
    private static int addToBundle(ItemStack bundle, ItemStack stack) {
        if (!stack.isEmpty() && stack.getItem().canBeNested()) {
            NbtCompound nbtCompound = bundle.getOrCreateNbt();
            if (!nbtCompound.contains("Items")) {
                nbtCompound.put("Items", new NbtList());
            }

            NbtList nbtList = nbtCompound.getList("Items", NbtElement.COMPOUND_TYPE);
            // Check the number of different item types
            List<String> itemTypes = nbtList.stream()
                    .map(NbtCompound.class::cast)
                    .map(nbt -> nbt.getString("id"))
                    .distinct().toList();

            // Check if the bundle is already at the max type limit
            boolean canAddItem = itemTypes.size() < MAX_ITEM_TYPES || itemTypes.stream().anyMatch(id -> id.equals(Registries.ITEM.getId(stack.getItem()).toString()));

            if (!canAddItem) {
                return 0; // Do not add item if type limit is reached
            }

            int i = getBundleOccupancy(bundle);
            int j = getItemOccupancy(stack);
            int k = Math.min(stack.getCount(), (MAX_STORAGE - i) / j);
            if (k == 0) {
                return 0;
            } else {
                Optional<NbtCompound> optional = canMergeStack(stack, nbtList);
                if (optional.isPresent()) {
                    NbtCompound nbtCompound2 = optional.get();
                    ItemStack itemStack = ItemStack.fromNbt(nbtCompound2);
                    if (itemStack.isStackable() && itemStack.getCount() + k <= itemStack.getMaxCount()) {
                        itemStack.increment(k);
                        itemStack.writeNbt(nbtCompound2);
                        nbtList.remove(nbtCompound2);
                        nbtList.add(0, nbtCompound2);
                    } else {
                        ItemStack itemStack2 = stack.copyWithCount(k);
                        NbtCompound nbtCompound3 = new NbtCompound();
                        itemStack2.writeNbt(nbtCompound3);
                        nbtList.add(0, nbtCompound3);
                    }
                } else {
                    ItemStack itemStack2 = stack.copyWithCount(k);
                    NbtCompound nbtCompound3 = new NbtCompound();
                    itemStack2.writeNbt(nbtCompound3);
                    nbtList.add(0, nbtCompound3);
                }

                return k;
            }
        } else {
            return 0;
        }
    }


    /**
     * @author Hollowed
     * @reason bundles
     */
    @Overwrite
    public boolean isItemBarVisible(ItemStack stack) {
        return getBundleOccupancy(stack) > 0;
    }

    /**
     * @author Hollowed
     * @reason bundles
     */
    @Overwrite
    public int getItemBarStep(ItemStack stack) {
        return Math.min(1 + 12 * getBundleOccupancy(stack) / MAX_STORAGE, 13);
    }

    /**
     * @author Hollowed
     * @reason bundles
     */
    @Overwrite
    public void appendTooltip(ItemStack stack, net.minecraft.world.World world, List<Text> tooltip, net.minecraft.client.item.TooltipContext context) {
        tooltip.add(Text.translatable("item.minecraft.bundle.fullness", getBundleOccupancy(stack), MAX_STORAGE).formatted(Formatting.GRAY));
    }

    /**
     * @author Hollowed
     * @reason bundles
     */
    @Overwrite
    public boolean onStackClicked(ItemStack stack, net.minecraft.screen.slot.Slot slot, net.minecraft.util.ClickType clickType, PlayerEntity player) {
        NbtCompound nbtCompound = stack.getOrCreateNbt();
        NbtList nbtList = nbtCompound.getList("Items", NbtElement.COMPOUND_TYPE);
        // Check the number of different item types
        List<String> itemTypes = nbtList.stream()
                .map(NbtCompound.class::cast)
                .map(nbt -> nbt.getString("id"))
                .distinct().toList();

        // Check if the bundle is already at the max type limit
        boolean canAddItem = itemTypes.size() < MAX_ITEM_TYPES || itemTypes.stream().anyMatch(id -> id.equals(Registries.ITEM.getId(stack.getItem()).toString()));
        if (clickType != net.minecraft.util.ClickType.RIGHT) {
            return false;
        } else {
            ItemStack itemStack = slot.getStack();
            if (itemStack.isEmpty()) {
                playRemoveOneSound(player);
                removeFirstStack(stack).ifPresent(slot::insertStack);
            } else if (itemStack.getItem().canBeNested()) {
                if (!canAddItem) {
                    return false;
                }
                int i = (MAX_STORAGE - getBundleOccupancy(stack)) / getItemOccupancy(itemStack);
                int j = addToBundle(stack, slot.takeStackRange(itemStack.getCount(), i, player));
                if (j > 0) {
                    playInsertSound(player);
                } else if (j == 0) {
                    // Restore item to slot if it was not added
                    slot.insertStack(itemStack.copy());
                    //player.sendMessage(Text.translatable("message.hss.bundle_full"), true);
                }
            }

            return true;
        }
    }

    /**
     * @author Hollowed
     * @reason bundles
     */
    @Overwrite
    public boolean onClicked(ItemStack stack, ItemStack otherStack, net.minecraft.screen.slot.Slot slot, net.minecraft.util.ClickType clickType, PlayerEntity player, net.minecraft.inventory.StackReference cursorStackReference) {
        NbtCompound nbtCompound = stack.getOrCreateNbt();
        NbtList nbtList = nbtCompound.getList("Items", NbtElement.COMPOUND_TYPE);
        // Check the number of different item types
        List<String> itemTypes = nbtList.stream()
                .map(NbtCompound.class::cast)
                .map(nbt -> nbt.getString("id"))
                .distinct().toList();

        // Check if the bundle is already at the max type limit
        boolean canAddItem = itemTypes.size() < MAX_ITEM_TYPES || itemTypes.stream().anyMatch(id -> id.equals(Registries.ITEM.getId(stack.getItem()).toString()));

        if (!canAddItem) {
            return false;
        }
        if (clickType == net.minecraft.util.ClickType.RIGHT && slot.canTakePartial(player)) {
            if (otherStack.isEmpty()) {
                removeFirstStack(stack).ifPresent(itemStack -> {
                    playRemoveOneSound(player);
                    cursorStackReference.set(itemStack);
                });
            } else {
                int i = addToBundle(stack, otherStack);
                if (i > 0) {
                    playInsertSound(player);
                    otherStack.decrement(i);
                } else if (i == 0) {
                    // Restore item to cursor if it was not added
                    cursorStackReference.set(otherStack.copy());
                    //player.sendMessage(Text.translatable("message.hss.bundle_full"), true);
                }
            }

            return true;
        } else {
            return false;
        }
    }


    /**
     * @author Hollowed
     * @reason bundles
     */
    @Overwrite
    public net.minecraft.util.TypedActionResult<ItemStack> use(net.minecraft.world.World world, PlayerEntity user, net.minecraft.util.Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        if (dropAllBundledItems(itemStack, user)) {
            playDropContentsSound(user);
            user.incrementStat(net.minecraft.stat.Stats.USED.getOrCreateStat(Items.BUNDLE));
            return net.minecraft.util.TypedActionResult.success(itemStack, world.isClient());
        } else {
            return net.minecraft.util.TypedActionResult.fail(itemStack);
        }
    }


    /**
     * @author Hollowed
     * @reason bundles
     */
    @Overwrite
    public void onItemEntityDestroyed(net.minecraft.entity.ItemEntity entity) {
        net.minecraft.item.ItemUsage.spawnItemContents(entity, getBundledStacks(entity.getStack()));
    }

    @Unique
    private static java.util.stream.Stream<ItemStack> getBundledStacks(ItemStack stack) {
        NbtCompound nbtCompound = stack.getNbt();
        if (nbtCompound == null) {
            return java.util.stream.Stream.empty();
        } else {
            NbtList nbtList = nbtCompound.getList("Items", NbtElement.COMPOUND_TYPE);
            return nbtList.stream().map(NbtCompound.class::cast).map(ItemStack::fromNbt);
        }
    }

    @Unique
    private void playRemoveOneSound(Entity entity) {
        entity.playSound(SoundEvents.ITEM_BUNDLE_REMOVE_ONE, 0.8F, 0.8F + entity.getWorld().getRandom().nextFloat() * 0.4F);
    }

    @Unique
    private void playInsertSound(Entity entity) {
        entity.playSound(SoundEvents.ITEM_BUNDLE_INSERT, 0.8F, 0.8F + entity.getWorld().getRandom().nextFloat() * 0.4F);
    }

    @Unique
    private void playDropContentsSound(Entity entity) {
        entity.playSound(SoundEvents.ITEM_BUNDLE_DROP_CONTENTS, 0.8F, 0.8F + entity.getWorld().getRandom().nextFloat() * 0.4F);
    }
}
