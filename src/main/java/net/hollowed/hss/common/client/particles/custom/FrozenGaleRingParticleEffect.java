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
public class FrozenGaleRingParticleEffect {

    public static void spawnParticles(World level, Vec3d pos, Vec3d facing) {
        Color startingColor = new Color((int) (81 * 1.9), (int) (133 * 1.9), (int) (142 * 1.8));
        Color endingColor = new Color((int) (33 * 1.9), (int) (36 * 1.9), (int) (56 * 1.7));

        WorldParticleBuilder.create(ModParticles.THIN_RING)
                .setScaleData(GenericParticleData.create(0.5f, 3f).build())
                .setTransparencyData(GenericParticleData.create(0.9f, 0f).build())
                .setColorData(ColorParticleData.create(startingColor, endingColor).setCoefficient(1.0f).setEasing(Easing.LINEAR).build())
                .setLifetime(10)
                .setRenderType(LodestoneWorldParticleRenderType.TRANSPARENT.withDepthFade())
                .disableCull()
                .setBehavior(new DirectionalBehaviorComponent(facing))
                .spawn(level, pos.x, pos.y + 0.8, pos.z);
    }
}
