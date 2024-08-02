package net.hollowed.hss.common.networking.packets;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static net.hollowed.hss.ModComponents.GRAB_CHECKER;
import static net.hollowed.hss.common.networking.NetworkingConstants.GRAB_PACKET_ID;

public class GrabPacket {
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public static void send() {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        ClientPlayNetworking.send(GRAB_PACKET_ID, buf);
    }

    public static void register() {
        ServerPlayNetworking.registerGlobalReceiver(GRAB_PACKET_ID, (server, player, handler, buf, responseSender) -> {
            server.execute(() -> {
                // Set the value to true
                GRAB_CHECKER.get(player).setValue(true);

                scheduler.schedule(() -> {
                    server.execute(() -> {
                        GRAB_CHECKER.get(player).setValue(false);
                    });
                }, 50, TimeUnit.MILLISECONDS); // 1 ticks, 50ms each tick
            });
        });
    }
}