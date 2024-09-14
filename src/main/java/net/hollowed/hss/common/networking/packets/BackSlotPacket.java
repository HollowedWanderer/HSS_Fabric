package net.hollowed.hss.common.networking.packets;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.hollowed.hss.common.item.custom.HollowedBladeItem;
import net.hollowed.hss.common.sound.ModSounds;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;

import static net.hollowed.hss.common.networking.NetworkingConstants.BACKSLOT_PACKET_ID;

public class BackSlotPacket {
    public static void send() {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        ClientPlayNetworking.send(BACKSLOT_PACKET_ID, buf);
    }

    public static void register() {
        ServerPlayNetworking.registerGlobalReceiver(BACKSLOT_PACKET_ID, (server, player, handler, buf, responseSender) -> {
            server.execute(() -> {
                if (player == null || player.currentScreenHandler == null) {
                    return;
                }

                PlayerScreenHandler screenHandler = (PlayerScreenHandler) player.currentScreenHandler;
                Slot backSlot = screenHandler.getSlot(46);

                ItemStack offhandStack = player.getOffHandStack();
                ItemStack handStack = player.getMainHandStack();
                ItemStack backStack = backSlot.getStack();

                if (handStack != ItemStack.EMPTY) {
                    player.setStackInHand(Hand.MAIN_HAND, backStack.copy());
                    backSlot.setStack(handStack.copy());
                } else if (offhandStack != ItemStack.EMPTY) {
                    player.setStackInHand(Hand.OFF_HAND, backStack.copy());
                    backSlot.setStack(offhandStack.copy());
                } else {
                    player.setStackInHand(Hand.MAIN_HAND, backStack.copy());
                    backSlot.setStack(ItemStack.EMPTY);
                }

                // Play a funny noise depending on the item
                if (backStack.getItem() instanceof HollowedBladeItem) {
                    player.getWorld().playSoundFromEntity(null, player, ModSounds.SWORD_UNSHEATH, SoundCategory.PLAYERS, 1F, 0.8F);
                    player.getWorld().playSoundFromEntity(null, player, SoundEvents.ITEM_ARMOR_EQUIP_CHAIN, SoundCategory.PLAYERS, 1F, 0.8F);
                } else if (backStack.getItem() instanceof AxeItem) {
                    player.getWorld().playSoundFromEntity(null, player, ModSounds.SWORD_UNSHEATH, SoundCategory.PLAYERS, 1F, 0.9F);
                    player.getWorld().playSoundFromEntity(null, player, SoundEvents.ITEM_ARMOR_EQUIP_CHAIN, SoundCategory.PLAYERS, 1F, 0.9F);
                } else if (backStack.getItem() instanceof SwordItem) {
                    player.getWorld().playSoundFromEntity(null, player, ModSounds.SWORD_UNSHEATH, SoundCategory.PLAYERS, 1F, 1.0F);
                    player.getWorld().playSoundFromEntity(null, player, SoundEvents.ITEM_ARMOR_EQUIP_CHAIN, SoundCategory.PLAYERS, 1F, 1F);
                } else if (backStack != ItemStack.EMPTY) {
                    player.getWorld().playSoundFromEntity(null, player, SoundEvents.ITEM_ARMOR_EQUIP_CHAIN, SoundCategory.PLAYERS, 1F, 1F);
                }

                if (offhandStack != ItemStack.EMPTY || handStack != ItemStack.EMPTY) {
                    player.getWorld().playSoundFromEntity(null, player, SoundEvents.ITEM_ARMOR_EQUIP_CHAIN, SoundCategory.PLAYERS, 1F, 1F);
                }

                // Sync the player's inventory back to the client
                player.currentScreenHandler.sendContentUpdates();
            });
        });
    }
}