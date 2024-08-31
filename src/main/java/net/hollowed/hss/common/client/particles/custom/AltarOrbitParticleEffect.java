package net.hollowed.hss.common.client.particles.custom;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.hollowed.hss.common.client.particles.ModParticles;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import team.lodestar.lodestone.systems.easing.Easing;
import team.lodestar.lodestone.systems.particle.builder.WorldParticleBuilder;
import team.lodestar.lodestone.systems.particle.data.GenericParticleData;
import team.lodestar.lodestone.systems.particle.data.color.ColorParticleData;
import team.lodestar.lodestone.systems.particle.data.spin.SpinParticleData;
import team.lodestar.lodestone.systems.particle.render_types.LodestoneWorldParticleRenderType;

import java.awt.*;
import java.util.Random;

@Environment(EnvType.CLIENT)
public class AltarOrbitParticleEffect {
    public static void spawnParticles(World level, Vec3d pos1, Vec3d pos2) {
        // Common particle properties
        Color startingColor = new Color((int) (255 * 0.8), (int) (137 * 0.8), (int) (190 * 0.8));
        Color endingColor = new Color((int) (255 * 0.5), (int) (137 * 0.5), (int) (190 * 0.5));

        // Spawn the first particle
        WorldParticleBuilder.create(ModParticles.SQUARE)
                .setScaleData(GenericParticleData.create(0.2f, 0).build())
                .setTransparencyData(GenericParticleData.create(0.75f, 0.25f).build())
                .setColorData(ColorParticleData.create(startingColor, endingColor).setCoefficient(1.0f).setEasing(Easing.LINEAR).build())
                .setSpinData(SpinParticleData.create(0.2f, 0.4f).setSpinOffset((level.getTime() * 0.2f) % 6.28f).setEasing(Easing.LINEAR).build())
                .setLifetime(12)
                .addMotion(0, 0.1, 0)
                .setRenderType(LodestoneWorldParticleRenderType.TRANSPARENT.withDepthFade())
                .disableCull()
                .spawn(level, pos1.x, pos1.y, pos1.z);

        // Spawn the second particle
        WorldParticleBuilder.create(ModParticles.SQUARE)
                .setScaleData(GenericParticleData.create(0.2f, 0).build())
                .setTransparencyData(GenericParticleData.create(0.75f, 0.25f).build())
                .setColorData(ColorParticleData.create(startingColor, endingColor).setCoefficient(1.0f).setEasing(Easing.LINEAR).build())
                .setSpinData(SpinParticleData.create(0.2f, 0.4f).setSpinOffset((level.getTime() * 0.2f) % 6.28f).setEasing(Easing.LINEAR).build())
                .setLifetime(12)
                .addMotion(0, 0.1, 0)
                .setRenderType(LodestoneWorldParticleRenderType.TRANSPARENT.withDepthFade())
                .disableCull()
                .spawn(level, pos2.x, pos2.y, pos2.z);
    }
}
