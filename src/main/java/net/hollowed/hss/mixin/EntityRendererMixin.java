package net.hollowed.hss.mixin;

import net.hollowed.hss.common.client.render.LaserRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.VillagerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntityRenderer.class)
public abstract class EntityRendererMixin {

    @Inject(method = "render*", at = @At("RETURN"))
    private void renderBeam(LivingEntity livingEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, CallbackInfo ci) {
        if (livingEntity instanceof VillagerEntity) {
            //LaserRenderer.renderLaser(matrixStack, (float) livingEntity.getX(), (float) livingEntity.getY(), (float) livingEntity.getZ(), 1.5f, livingEntity);
        }
    }
}