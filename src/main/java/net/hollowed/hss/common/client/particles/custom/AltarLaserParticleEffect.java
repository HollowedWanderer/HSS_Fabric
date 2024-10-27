package net.hollowed.hss.common.client.particles.custom;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.hollowed.hss.common.client.particles.ModParticles;
import net.hollowed.hss.common.networking.DelayHandler;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import team.lodestar.lodestone.systems.easing.Easing;
import team.lodestar.lodestone.systems.particle.builder.WorldParticleBuilder;
import team.lodestar.lodestone.systems.particle.data.GenericParticleData;
import team.lodestar.lodestone.systems.particle.data.color.ColorParticleData;
import team.lodestar.lodestone.systems.particle.render_types.LodestoneWorldParticleRenderType;
import team.lodestar.lodestone.systems.particle.world.behaviors.components.DirectionalBehaviorComponent;

import java.awt.*;

@Environment(EnvType.CLIENT)
public class AltarLaserParticleEffect {
    public static void spawnParticles(World level, Vec3d pos) {
        // Common particle properties
        Color startingColor = new Color((int) (255 ), (int) (137 ), (int) (190));
        Color endingColor = new Color((int) (255 * 0.5), (int) (137 * 0.5), (int) (190 * 0.5));



        WorldParticleBuilder.create(ModParticles.SQUARE)
                .setScaleData(GenericParticleData.create(0.2f, 0F).build())
                .setTransparencyData(GenericParticleData.create(1f, 0f).build())
                .setColorData(ColorParticleData.create(startingColor, endingColor).setCoefficient(1.0f).setEasing(Easing.LINEAR).build())
                .setLifetime(12)
                .setRenderType(LodestoneWorldParticleRenderType.TRANSPARENT.withDepthFade())
                .disableCull()
                .setRandomMotion(0.1, 0, 0.1)
                .setMotion(0, 0.3, 0)
                .spawn(level, pos.x, pos.y, pos.z);
    }
}