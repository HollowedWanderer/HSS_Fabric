package net.hollowed.hss;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.hollowed.hss.common.block.ModBlockEntities;
import net.hollowed.hss.common.block.ModBlocks;
import net.hollowed.hss.common.block.entities.renderer.AltarRenderer;
import net.hollowed.hss.common.block.entities.renderer.PedestalRenderer;
import net.hollowed.hss.common.client.particles.custom.CryoShardParticle;
import net.hollowed.hss.common.entity.ModEntities;
import net.hollowed.hss.common.entity.renderer.ItemProjectileEntityRenderer;
import net.hollowed.hss.common.item.ModModelPredicateProvider;
import net.hollowed.hss.common.networking.ModKeyBindings;
import net.hollowed.hss.common.networking.RightClickHandler;
import net.hollowed.hss.common.util.ModWorldEvents;
import net.hollowed.hss.common.worldEvents.TestEventRenderer;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.client.util.ModelIdentifier;
import team.lodestar.lodestone.registry.client.LodestoneWorldEventRendererRegistry;
import team.lodestar.lodestone.systems.particle.world.type.LodestoneWorldParticleType;

import static net.hollowed.hss.common.client.particles.ModParticles.*;

public class HollowedsSwordsSorceryClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        BlockEntityRendererFactories.register(ModBlockEntities.PEDESTAL_BLOCK_ENTITY, PedestalRenderer::new);
        BlockEntityRendererFactories.register(ModBlockEntities.RESONATING_ALTAR_BLOCK_ENTITY, AltarRenderer::new);
        BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutout(), ModBlocks.ROSE, ModBlocks.POTTED_ROSE, ModBlocks.WHITE_ROSE, ModBlocks.POTTED_WHITE_ROSE, ModBlocks.WHITE_ROSE_BUSH, ModBlocks.RESONATING_ALTAR, ModBlocks.PEDESTAL);
        BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getTranslucent(), ModBlocks.THAUMITE_GLASS);

        EntityRendererRegistry.register(ModEntities.CRYO_SHARD, ItemProjectileEntityRenderer::new);
        EntityRendererRegistry.register(ModEntities.FRAGILE_CRYO_SHARD, ItemProjectileEntityRenderer::new);
        EntityRendererRegistry.register(ModEntities.SPIRAL_FRAGILE_CRYO_SHARD, ItemProjectileEntityRenderer::new);

        ParticleFactoryRegistry.getInstance().register(LASER.get(), LodestoneWorldParticleType.Factory::new);
        ParticleFactoryRegistry.getInstance().register(SQUARE.get(), LodestoneWorldParticleType.Factory::new);
        ParticleFactoryRegistry.getInstance().register(DUST.get(), LodestoneWorldParticleType.Factory::new);
        ParticleFactoryRegistry.getInstance().register(RING.get(), LodestoneWorldParticleType.Factory::new);
        ParticleFactoryRegistry.getInstance().register(THIN_RING.get(), LodestoneWorldParticleType.Factory::new);

        ParticleFactoryRegistry.getInstance().register(CRYO_SHARD, CryoShardParticle.Factory::new);

        ModModelPredicateProvider.register();

        LodestoneWorldEventRendererRegistry.registerRenderer(ModWorldEvents.TEST_EVENT, new TestEventRenderer());

        ModelLoadingPlugin.register((context) -> {
            context.addModels(new ModelIdentifier(HollowedsSwordsSorcery.MOD_ID, "hollowed_blade_hand", "inventory"));
            context.addModels(new ModelIdentifier(HollowedsSwordsSorcery.MOD_ID, "hollowed_blade_hand_shattered", "inventory"));
            context.addModels(new ModelIdentifier(HollowedsSwordsSorcery.MOD_ID, "cryo_shard_hand", "inventory"));
            context.addModels(new ModelIdentifier(HollowedsSwordsSorcery.MOD_ID, "katana_hand", "inventory"));
            context.addModels(new ModelIdentifier(HollowedsSwordsSorcery.MOD_ID, "hollowed_wrench_hand", "inventory"));
        });

        ModKeyBindings.initialize();

        ClientTickEvents.END_CLIENT_TICK.register(client -> RightClickHandler.checkRightClickInAir());
    }
}
