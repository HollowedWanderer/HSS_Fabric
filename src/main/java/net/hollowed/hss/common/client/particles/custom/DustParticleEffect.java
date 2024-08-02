package net.hollowed.hss.common.client.particles.custom;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.hollowed.hss.common.client.particles.ModParticles;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import team.lodestar.lodestone.systems.easing.Easing;
import team.lodestar.lodestone.systems.particle.builder.WorldParticleBuilder;
import team.lodestar.lodestone.systems.particle.data.GenericParticleData;
import team.lodestar.lodestone.systems.particle.data.color.ColorParticleData;
import team.lodestar.lodestone.systems.particle.data.spin.SpinParticleData;
import team.lodestar.lodestone.systems.particle.render_types.LodestoneWorldParticleRenderType;

import java.awt.Color;
import java.util.Random;

@Environment(EnvType.CLIENT)
public class DustParticleEffect {

    public static void initialize() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            ClientPlayerEntity player = MinecraftClient.getInstance().player;
            if (player != null && "HollowedWanderer".equals(player.getName().getString())) {
                //spawnExampleParticles(player.getWorld(), player.getPos());
            }
        });
    }

    public static void spawnExampleParticles(World level, Vec3d pos) {
        Random random = new Random();
        float randomX = -0.1f + random.nextFloat() * 0.2f; // Random value between -0.1 and 0.1
        float randomZ = -0.1f + random.nextFloat() * 0.2f; // Random value between -0.1 and 0.1
        float randomY = 0.025f + random.nextFloat() * 0.075f; // Random value between 0.025 and 0.1

        Color startingColor = new Color((int) (140 * 1.1), (int) (132 * 1.1), (int) (123 * 1.1));
        Color endingColor = new Color(54, 50, 45);

        WorldParticleBuilder.create(ModParticles.DUST)
                .setScaleData(GenericParticleData.create(0.3f, 0).build())
                .setTransparencyData(GenericParticleData.create(0.75f, 0.25f).build())
                .setColorData(ColorParticleData.create(startingColor, endingColor).setCoefficient(1.0f).setEasing(Easing.LINEAR).build())
                .setSpinData(SpinParticleData.create(0.2f, 0.4f).setSpinOffset((level.getTime() * 0.2f) % 6.28f).setEasing(Easing.LINEAR).build())
                .setLifetime(7)
                .addMotion(randomX, randomY, randomZ)
                .setRenderType(LodestoneWorldParticleRenderType.TRANSPARENT.withDepthFade())
                .disableCull()
                //.setBehavior(new DirectionalBehaviorComponent(new Vec3d(Math.cos(level.getTime() * 0.05), 0, Math.sin(level.getTime() * 0.05))))
                .spawn(level, pos.x, pos.y, pos.z);
    }
}
