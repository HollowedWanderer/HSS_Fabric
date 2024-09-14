package net.hollowed.hss.common.util;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.hollowed.hss.common.networking.ModKeyBindings;
import net.hollowed.hss.common.networking.packets.BackSlotPacket;
import net.minecraft.client.MinecraftClient;

public class KeybindEventHandler {
    private static final MinecraftClient client = MinecraftClient.getInstance();
    private static boolean wasBackSlotKeyPressed = false;

    public static void register() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            boolean isKeyPressed = ModKeyBindings.backSlotBinding.isPressed();

            if (isKeyPressed && !wasBackSlotKeyPressed && client.player != null) {
                // Send a packet to the server to handle the back slot key press
                BackSlotPacket.send();
            }

            // Update the key press state
            wasBackSlotKeyPressed = isKeyPressed;
        });
    }
}