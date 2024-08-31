package net.hollowed.hss.common;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.hollowed.hss.common.client.models.ChilledModel;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class EntitiesClient implements ClientModInitializer {
    public static final EntityModelLayer CHILLED_LAYER = new EntityModelLayer(new Identifier("hss", "ice_player_layer"), "main");
    @Override
    public void onInitializeClient() {
        EntityModelLayerRegistry.registerModelLayer(CHILLED_LAYER, ChilledModel::getTexturedModelData);
    }
}