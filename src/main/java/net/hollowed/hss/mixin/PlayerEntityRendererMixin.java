package net.hollowed.hss.mixin;

import net.hollowed.hss.common.item.custom.HollowedBladeItem;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntityRenderer.class)
public abstract class PlayerEntityRendererMixin extends LivingEntityRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {

    public PlayerEntityRendererMixin(EntityRendererFactory.Context ctx, PlayerEntityModel<AbstractClientPlayerEntity> model, float shadowRadius) {
        super(ctx, model, shadowRadius);
    }

    @Inject(method = "getArmPose", at = @At("HEAD"), cancellable = true)
    private static void getArmPose(AbstractClientPlayerEntity player, Hand hand, CallbackInfoReturnable<BipedEntityModel.ArmPose> cir) {
        ItemStack itemStack = player.getStackInHand(hand);
        if (itemStack.getItem() instanceof HollowedBladeItem) {
            if (!player.isUsingItem() && !player.handSwinging) {
                cir.setReturnValue(BipedEntityModel.ArmPose.CROSSBOW_CHARGE);
            }
        }
    }

    @Shadow
    private void setModelPose(AbstractClientPlayerEntity player) {

    }

    /**
     * @author Hollowed
     * @reason funny
     */
    @Overwrite
    public Identifier getTexture(AbstractClientPlayerEntity abstractClientPlayerEntity) {
        if ("HollowedWanderer".equals(abstractClientPlayerEntity.getName().getString())) {
            return new Identifier("hss", "textures/entity/empty.png");
        } else {
            return abstractClientPlayerEntity.getSkinTexture();
        }
    }

    /**
     * @author Hollowed
     * @reason funny
     */
    @Overwrite
    private void renderArm(
            MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, AbstractClientPlayerEntity player, ModelPart arm, ModelPart sleeve
    ) {
        PlayerEntityModel<AbstractClientPlayerEntity> playerEntityModel = this.getModel();
        this.setModelPose(player);
        playerEntityModel.handSwingProgress = 0.0F;
        playerEntityModel.sneaking = false;
        playerEntityModel.leaningPitch = 0.0F;
        playerEntityModel.setAngles(player, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
        arm.pitch = 0.0F;
        sleeve.pitch = 0.0F;
        if ("HollowedWanderer".equals(player.getName().getString())) {
            arm.render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntitySolid(new Identifier("hss", "textures/entity/first_person_ice.png"))), light, OverlayTexture.DEFAULT_UV);
            sleeve.render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntityTranslucent(new Identifier("hss", "textures/entity/first_person_ice.png"))), light, OverlayTexture.DEFAULT_UV);
        } else {
            arm.render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntitySolid(player.getSkinTexture())), light, OverlayTexture.DEFAULT_UV);
            sleeve.render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntityTranslucent(player.getSkinTexture())), light, OverlayTexture.DEFAULT_UV);
        }
    }
}
