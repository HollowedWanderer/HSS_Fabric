package net.hollowed.hss.common.networking.packets;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.hollowed.hss.HollowedsSwordsSorcery;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static net.hollowed.hss.ModComponents.UTILITY_CHECKER;
import static net.hollowed.hss.common.networking.NetworkingConstants.UTILITY_MOVE_PACKET_ID;

public class UtilityMovePacket {
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public static void send() {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        ClientPlayNetworking.send(UTILITY_MOVE_PACKET_ID, buf);
    }

    public static void register() {
        ServerPlayNetworking.registerGlobalReceiver(UTILITY_MOVE_PACKET_ID, (server, player, handler, buf, responseSender) -> {
            server.execute(() -> {
                // Set the value to true
                UTILITY_CHECKER.get(player).setValue(true);

                // Schedule the task to set the value to false after 5 ticks (100ms each tick = 500ms)
                scheduler.schedule(() -> {
                    server.execute(() -> {
                        UTILITY_CHECKER.get(player).setValue(false);
                    });
                }, 50, TimeUnit.MILLISECONDS); // 1 ticks, 50ms each tick
            });
        });
    }
}