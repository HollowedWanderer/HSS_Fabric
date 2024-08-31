package net.hollowed.hss.common.client.particles.custom;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.hollowed.hss.common.client.particles.ModParticles;
import net.hollowed.hss.common.entity.custom.FragileCryoShardEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import team.lodestar.lodestone.registry.common.particle.LodestoneParticleRegistry;
import team.lodestar.lodestone.systems.easing.Easing;
import team.lodestar.lodestone.systems.particle.builder.WorldParticleBuilder;
import team.lodestar.lodestone.systems.particle.data.GenericParticleData;
import team.lodestar.lodestone.systems.particle.data.color.ColorParticleData;
import team.lodestar.lodestone.systems.particle.data.spin.SpinParticleData;
import team.lodestar.lodestone.systems.particle.render_types.LodestoneWorldParticleRenderType;
import team.lodestar.lodestone.systems.particle.world.behaviors.components.DirectionalBehaviorComponent;

import java.awt.*;

@Environment(EnvType.CLIENT)
public class AltarExplodeParticleEffect {
    public static void spawnParticles(World level, Vec3d pos) {
        // Common particle properties
        Color startingColor = new Color((int) (255 ), (int) (137 ), (int) (190));
        Color endingColor = new Color((int) (255 * 0.7), (int) (137 * 0.7), (int) (190 * 0.7));

        Color startingColor1 = new Color(255, 255, 255);
        Color startingColor2 = new Color((int) (255), (int) (137 * 1.3), (int) (190 * 1.2));

        // Spawn the second particle
        WorldParticleBuilder.create(ModParticles.THIN_RING)
                .setScaleData(GenericParticleData.create(0.2f, 10F).build())
                .setTransparencyData(GenericParticleData.create(1f, 0f).build())
                .setColorData(ColorParticleData.create(startingColor, endingColor).setCoefficient(1.0f).setEasing(Easing.LINEAR).build())
                .setLifetime(25)
                .setRenderType(LodestoneWorldParticleRenderType.TRANSPARENT.withDepthFade())
                .disableCull()
                .setBehavior(new DirectionalBehaviorComponent(new Vec3d(0, 1, 0)))
                .spawn(level, pos.x, pos.y, pos.z);

        // Spawn the second particle
        WorldParticleBuilder.create(ModParticles.THIN_RING)
                .setScaleData(GenericParticleData.create(0.2f, 4F).build())
                .setTransparencyData(GenericParticleData.create(1f, 0f).build())
                .setColorData(ColorParticleData.create(startingColor1, endingColor).setCoefficient(1.0f).setEasing(Easing.LINEAR).build())
                .setLifetime(20)
                .setRenderType(LodestoneWorldParticleRenderType.TRANSPARENT.withDepthFade())
                .disableCull()
                .spawn(level, pos.x, pos.y, pos.z);

        double goldenRatio = (1 + Math.sqrt(5)) / 2;
        double angleIncrement = Math.PI * 2 * goldenRatio;

        for (int i = 0; i < 96; i++) {
            double t = (double)i / (96 - 1);
            double inclination = Math.acos(1 - 2 * t);
            double azimuth = angleIncrement * i;

            double xVelocity = Math.sin(inclination) * Math.cos(azimuth);
            double yVelocity = Math.cos(inclination);
            double zVelocity = Math.sin(inclination) * Math.sin(azimuth);

            double speed = 1;
            xVelocity *= speed;
            yVelocity *= speed;
            zVelocity *= speed;


            WorldParticleBuilder.create(ModParticles.SQUARE)
                    .setScaleData(GenericParticleData.create(0.2f, 0F).build())
                    .setTransparencyData(GenericParticleData.create(1f, 0f).build())
                    .setColorData(ColorParticleData.create(startingColor2, endingColor).setCoefficient(1.0f).setEasing(Easing.LINEAR).build())
                    .setLifetime(20)
                    .setRenderType(LodestoneWorldParticleRenderType.TRANSPARENT.withDepthFade())
                    .disableCull()
                    .setMotion(xVelocity * 0.7, yVelocity * 0.7, zVelocity * 0.7)
                    .setGravityStrength(1f)
                    .spawn(level, pos.x, pos.y + 0.5, pos.z);
        }
    }
}
