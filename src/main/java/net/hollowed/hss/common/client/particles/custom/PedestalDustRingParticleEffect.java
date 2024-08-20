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
import team.lodestar.lodestone.systems.particle.render_types.LodestoneWorldParticleRenderType;
import team.lodestar.lodestone.systems.particle.world.behaviors.components.DirectionalBehaviorComponent;

import java.awt.*;

@Environment(EnvType.CLIENT)
public class PedestalDustRingParticleEffect {

    public static void spawnParticles(World level, Vec3d pos) {
        Color startingColor = new Color((int) (140 * 1.1), (int) (132 * 1.1), (int) (123 * 1.1));
        Color endingColor = new Color((int) (140 * 0.6), (int) (132 * 0.6), (int) (123 * 0.6));

        WorldParticleBuilder.create(ModParticles.RING)
                .setScaleData(GenericParticleData.create(0.3f, 0.8f).build())
                .setTransparencyData(GenericParticleData.create(0.9f, 0f).build())
                .setColorData(ColorParticleData.create(startingColor, endingColor).setCoefficient(1.0f).setEasing(Easing.LINEAR).build())
                .setLifetime(10)
                .addMotion(0, 0.05, 0)
                .setRenderType(LodestoneWorldParticleRenderType.TRANSPARENT.withDepthFade())
                .disableCull()
                .setBehavior(new DirectionalBehaviorComponent(new Vec3d(0, 1, 0)))
                .spawn(level, pos.x, pos.y - 0.2, pos.z);
    }
}
