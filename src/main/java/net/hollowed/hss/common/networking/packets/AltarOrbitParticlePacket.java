package net.hollowed.hss.common.networking.packets;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import static net.hollowed.hss.common.networking.NetworkingConstants.*;

public class AltarOrbitParticlePacket {
    public static void send(World world, Vec3d pos1, Vec3d pos2) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeDouble(pos1.x);
        buf.writeDouble(pos1.y);
        buf.writeDouble(pos1.z);

        buf.writeDouble(pos2.x);
        buf.writeDouble(pos2.y);
        buf.writeDouble(pos2.z);

        for (PlayerEntity player2 : world.getPlayers()) {
            ServerPlayNetworking.send((ServerPlayerEntity) player2, ALTAR_ORBIT_PACKET_ID, buf);
        }
    }
}
