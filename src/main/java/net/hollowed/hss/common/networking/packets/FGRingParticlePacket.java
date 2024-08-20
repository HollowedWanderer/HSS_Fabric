package net.hollowed.hss.common.networking.packets;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import static net.hollowed.hss.common.networking.NetworkingConstants.FG_RING_PACKET_ID;
import static net.hollowed.hss.common.networking.NetworkingConstants.SHATTER_RING_PACKET_ID;

public class FGRingParticlePacket {
    public static void send(Vec3d position, World world, Vec3d facing) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeDouble(position.x);
        buf.writeDouble(position.y);
        buf.writeDouble(position.z);
        buf.writeDouble(facing.x);
        buf.writeDouble(facing.y);
        buf.writeDouble(facing.z);

        for (PlayerEntity player2 : world.getPlayers()) {
            ServerPlayNetworking.send((ServerPlayerEntity) player2, FG_RING_PACKET_ID, buf);
        }
    }
}
