package net.hollowed.hss.common.networking.packets;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.hollowed.hss.common.networking.NetworkingConstants;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

public class AltarUpdatePacket {
    public static void sendUpdatePacket(ServerWorld world, BlockPos pos, ItemStack stack) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeBlockPos(pos);
        buf.writeItemStack(stack);

        for (ServerPlayerEntity player : world.getPlayers()) {
            ServerPlayNetworking.send(player, NetworkingConstants.ALTAR_UPDATE_PACKET_ID, buf);
        }
    }
}
