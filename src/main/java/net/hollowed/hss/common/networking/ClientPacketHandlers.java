package net.hollowed.hss.common.networking;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.hollowed.hss.common.block.entities.PedestalBlockEntity;
import net.hollowed.hss.common.client.particles.custom.FrozenGaleRingParticleEffect;
import net.hollowed.hss.common.client.particles.custom.PedestalDustParticleEffect;
import net.hollowed.hss.common.client.particles.custom.PedestalDustRingParticleEffect;
import net.hollowed.hss.common.client.particles.custom.ShatterRingParticleEffect;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class ClientPacketHandlers {
    public static void register() {
        ClientPlayNetworking.registerGlobalReceiver(NetworkingConstants.PEDESTAL_UPDATE_PACKET_ID, (client, handler, buf, responseSender) -> {
            BlockPos pos = buf.readBlockPos();
            ItemStack stack = buf.readItemStack();

            client.execute(() -> {
                assert client.world != null;
                BlockEntity entity = client.world.getBlockEntity(pos);
                if (entity instanceof PedestalBlockEntity) {
                    ((PedestalBlockEntity) entity).setStack(0, stack);
                    client.world.updateListeners(pos, client.world.getBlockState(pos), client.world.getBlockState(pos), Block.NOTIFY_ALL);
                }
            });
        });
        ClientPlayNetworking.registerGlobalReceiver(NetworkingConstants.DUST_PARTICLE_PACKET_ID, (client, handler, buf, responseSender) -> {
            double x = buf.readDouble();
            double y = buf.readDouble();
            double z = buf.readDouble();

            client.execute(() -> {
                if (client.world != null) {
                    PedestalDustParticleEffect.spawnParticles(client.world, new Vec3d(x, y, z));
                }
            });
        });
        ClientPlayNetworking.registerGlobalReceiver(NetworkingConstants.RING_PARTICLE_PACKET_ID, (client, handler, buf, responseSender) -> {
            double x = buf.readDouble();
            double y = buf.readDouble();
            double z = buf.readDouble();

            client.execute(() -> {
                if (client.world != null) {
                    PedestalDustRingParticleEffect.spawnParticles(client.world, new Vec3d(x, y, z));
                }
            });
        });
        ClientPlayNetworking.registerGlobalReceiver(NetworkingConstants.SHATTER_RING_PACKET_ID, (client, handler, buf, responseSender) -> {
            double x = buf.readDouble();
            double y = buf.readDouble();
            double z = buf.readDouble();

            client.execute(() -> {
                if (client.world != null) {
                    ShatterRingParticleEffect.spawnParticles(client.world, new Vec3d(x, y, z));
                }
            });
        });
        ClientPlayNetworking.registerGlobalReceiver(NetworkingConstants.FG_RING_PACKET_ID, (client, handler, buf, responseSender) -> {
            double x = buf.readDouble();
            double y = buf.readDouble();
            double z = buf.readDouble();

            double facingX = buf.readDouble();
            double facingY = buf.readDouble();
            double facingZ = buf.readDouble();

            client.execute(() -> {
                if (client.world != null) {
                    FrozenGaleRingParticleEffect.spawnParticles(client.world, new Vec3d(x, y, z), new Vec3d(facingX, facingY, facingZ));
                }
            });
        });
    }
}
