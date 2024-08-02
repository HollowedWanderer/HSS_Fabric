package net.hollowed.hss.common.client.particles;

import io.github.fabricators_of_create.porting_lib.util.LazyRegistrar;
import io.github.fabricators_of_create.porting_lib.util.RegistryObject;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.hollowed.hss.HollowedsSwordsSorcery;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.particle.ParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import team.lodestar.lodestone.LodestoneLib;
import team.lodestar.lodestone.registry.common.particle.LodestoneParticleRegistry;
import team.lodestar.lodestone.systems.particle.screen.LodestoneScreenParticleType;
import team.lodestar.lodestone.systems.particle.screen.ScreenParticleOptions;
import team.lodestar.lodestone.systems.particle.screen.ScreenParticleType;
import team.lodestar.lodestone.systems.particle.world.type.LodestoneWorldParticleType;

import java.util.ArrayList;

import static team.lodestar.lodestone.registry.common.particle.LodestoneScreenParticleRegistry.getSpriteSet;
import static team.lodestar.lodestone.registry.common.particle.LodestoneScreenParticleRegistry.registerProvider;

public class ModParticles extends LodestoneParticleRegistry {
    public static final ArrayList<ScreenParticleType<?>> PARTICLE_TYPES = new ArrayList<>();

    public static LazyRegistrar<ParticleType<?>> PARTICLES = LazyRegistrar.create(Registries.PARTICLE_TYPE, HollowedsSwordsSorcery.MOD_ID);

    public static final RegistryObject<LodestoneWorldParticleType> LASER = PARTICLES.register("laser", LodestoneWorldParticleType::new);

    public static final RegistryObject<LodestoneWorldParticleType> SQUARE = PARTICLES.register("square", LodestoneWorldParticleType::new);

    public static final RegistryObject<LodestoneWorldParticleType> DUST = PARTICLES.register("dust", LodestoneWorldParticleType::new);

    public static final RegistryObject<LodestoneWorldParticleType> RING = PARTICLES.register("ring", LodestoneWorldParticleType::new);
}