package net.hollowed.hss.common.networking;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.hollowed.hss.common.item.custom.CharmItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;

import static net.hollowed.hss.ModComponents.BOUND_INVENTORY;

public class CharmChecker {

    public static void initialize() {
        // Register a server tick event to constantly check the player's inventory
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            for (ServerWorld world : server.getWorlds()) {
                for (ServerPlayerEntity player : world.getPlayers()) {
                    checkPlayerInventory(player);
                }
            }
        });
    }

    // Method to check if the player has an instance of CharmItem in their inventory
    public static void checkPlayerInventory(PlayerEntity player) {
        boolean hasCharm = false;
        for (ItemStack stack : player.getInventory().main) {
            if (stack.getItem() instanceof CharmItem) {
                hasCharm = true;
                break;
            }
        }

        BOUND_INVENTORY.get(player).setValue(hasCharm);
        BOUND_INVENTORY.sync(player);
    }
}
