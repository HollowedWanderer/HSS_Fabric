package net.hollowed.hss.mixin;

import net.hollowed.hss.common.EntitiesClient;
import net.hollowed.hss.common.client.renderers.BackSlotFeatureRenderer;
import net.hollowed.hss.common.client.renderers.ChilledFeatureRenderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.render.item.HeldItemRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntityRenderer.class)
public abstract class PlayerFeaturesMixin extends LivingEntityRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {

    public PlayerFeaturesMixin(EntityRendererFactory.Context ctx, PlayerEntityModel<AbstractClientPlayerEntity> model, float shadowRadius) {
        super(ctx, model, shadowRadius);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void addCustomFeature(EntityRendererFactory.Context ctx, boolean slim, CallbackInfo ci) {
        HeldItemRenderer heldItemRenderer = MinecraftClient.getInstance().getEntityRenderDispatcher().getHeldItemRenderer();

        this.addFeature(new ChilledFeatureRenderer(this, ctx.getModelLoader().getModelPart(EntitiesClient.CHILLED_LAYER)));
        this.addFeature(new BackSlotFeatureRenderer(this, heldItemRenderer));
    }
}