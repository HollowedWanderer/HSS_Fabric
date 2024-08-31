package net.hollowed.hss.common.client.particles.custom;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.hollowed.hss.common.client.particles.ModParticles;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import team.lodestar.lodestone.registry.common.particle.LodestoneParticleRegistry;
import team.lodestar.lodestone.systems.easing.Easing;
import team.lodestar.lodestone.systems.particle.builder.WorldParticleBuilder;
import team.lodestar.lodestone.systems.particle.data.GenericParticleData;
import team.lodestar.lodestone.systems.particle.data.color.ColorParticleData;
import team.lodestar.lodestone.systems.particle.render_types.LodestoneWorldParticleRenderType;
import team.lodestar.lodestone.systems.particle.world.behaviors.components.DirectionalBehaviorComponent;

import java.awt.*;

@Environment(EnvType.CLIENT)
public class AltarPlacePopParticleEffect {

    public static void spawnParticles(World level, Vec3d pos) {
        Color startingColor = new Color(255, 255, 255);
        Color endingColor = new Color(255, 137, 190);

        WorldParticleBuilder.create(ModParticles.THIN_RING)
                .setScaleData(GenericParticleData.create(0.1f, 2.5f).build())
                .setTransparencyData(GenericParticleData.create(0.9f, 0f).build())
                .setColorData(ColorParticleData.create(startingColor, endingColor).setCoefficient(1.0f).setEasing(Easing.LINEAR).build())
                .setLifetime(10)
                .setRenderType(LodestoneWorldParticleRenderType.TRANSPARENT.withDepthFade())
                .disableCull()
                //.setBehavior(new DirectionalBehaviorComponent(new Vec3d(0, 1, 0)))
                .spawn(level, pos.x, pos.y, pos.z);
    }
}
