package net.hollowed.hss.common;

import net.fabricmc.api.ClientModInitializer;
import net.hollowed.hss.common.block.ModBlockEntities;
import net.hollowed.hss.common.block.entities.PedestalRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;

public class HollowedsSwordsSorceryClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BlockEntityRendererFactories.register(ModBlockEntities.PEDESTAL_BLOCK_ENTITY, PedestalRenderer::new);
    }
}
