package net.hollowed.hss.common.client.particles;

import net.hollowed.hss.HollowedsSwordsSorcery;
import team.lodestar.lodestone.registry.common.particle.LodestoneScreenParticleRegistry;
import team.lodestar.lodestone.systems.particle.screen.LodestoneScreenParticleType;
import team.lodestar.lodestone.systems.particle.screen.ScreenParticleOptions;
import team.lodestar.lodestone.systems.particle.screen.ScreenParticleType;

@SuppressWarnings("unused")
public class ModScreenParticles extends LodestoneScreenParticleRegistry {
    public static final ScreenParticleType<ScreenParticleOptions> SQUARE = registerType(new LodestoneScreenParticleType());

    public static void registerParticleFactory() {
        registerProvider(SQUARE, new LodestoneScreenParticleType.Factory(getSpriteSet(HollowedsSwordsSorcery.hssPath("square"))));
    }
}