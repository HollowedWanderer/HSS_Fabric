package net.hollowed.hss.common.client.particles.custom;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import team.lodestar.lodestone.systems.particle.builder.WorldParticleBuilder;
import team.lodestar.lodestone.systems.particle.data.GenericParticleData;
import team.lodestar.lodestone.systems.particle.data.color.ColorParticleData;
import team.lodestar.lodestone.systems.particle.render_types.LodestoneWorldParticleRenderType;
import team.lodestar.lodestone.systems.particle.world.behaviors.components.DirectionalBehaviorComponent;
import team.lodestar.lodestone.systems.particle.world.type.LodestoneWorldParticleType;

@Environment(EnvType.CLIENT)
public class LodestoneParticleSpawner {
    public static void spawnParticles(World level, Vec3d pos,
                                      LodestoneWorldParticleType particle, float spinStart, float spinEnd,
                                      float transparencyStart, float transparencyEnd, int lifetime, boolean directional, Vec3d direction,
                                      LodestoneWorldParticleRenderType renderType, ColorParticleData colorParticleData, float gravity,
                                      float randomMotionSpeed, Vec3d motion, boolean cull, boolean noClip
    ) {

        if (directional) {

            // Spawn directional particle
            WorldParticleBuilder.create(particle)
                    .setScaleData(GenericParticleData.create(spinStart, spinEnd).build())
                    .setTransparencyData(GenericParticleData.create(transparencyStart, transparencyEnd).build())
                    .setColorData(colorParticleData)
                    .setLifetime(lifetime)
                    .setRenderType(renderType)
                    .setGravityStrength(gravity)
                    .setRandomMotion(randomMotionSpeed)
                    .setMotion(motion)
                    .setNoClip(noClip)
                    .setShouldCull(cull)
                    .setBehavior(new DirectionalBehaviorComponent(direction))
                    .spawn(level, pos.x, pos.y, pos.z);
        } else {

            // Spawn directional particle
            WorldParticleBuilder.create(particle)
                    .setScaleData(GenericParticleData.create(spinStart, spinEnd).build())
                    .setTransparencyData(GenericParticleData.create(transparencyStart, transparencyEnd).build())
                    .setColorData(colorParticleData)
                    .setLifetime(lifetime)
                    .setRenderType(renderType)
                    .setGravityStrength(gravity)
                    .setRandomMotion(randomMotionSpeed)
                    .setMotion(motion)
                    .setNoClip(noClip)
                    .setShouldCull(cull)
                    .spawn(level, pos.x, pos.y, pos.z);
        }
    }
}
