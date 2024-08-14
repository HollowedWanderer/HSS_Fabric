package net.hollowed.hss.mixin;

import net.hollowed.hss.HollowedsSwordsSorcery;
import net.hollowed.hss.common.item.ModItems;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ItemRenderer.class)
public abstract class ItemRendererMixin {
    @ModifyVariable(method = "renderItem", at = @At(value = "HEAD"), argsOnly = true)
    public BakedModel useLargeModel(BakedModel value, ItemStack stack, ModelTransformationMode renderMode, boolean leftHanded, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        if (stack.isOf(ModItems.HOLLOWED_BLADE) && renderMode != ModelTransformationMode.GUI && renderMode != ModelTransformationMode.FIXED && renderMode != ModelTransformationMode.GROUND) {
            assert stack.getNbt() != null;
            if (!stack.getNbt().getBoolean("Shattered")) {
                return ((ItemRendererAccessor) this).getModels().getModelManager().getModel(new ModelIdentifier(HollowedsSwordsSorcery.MOD_ID, "hollowed_blade_hand", "inventory"));
            } else {
                return ((ItemRendererAccessor) this).getModels().getModelManager().getModel(new ModelIdentifier(HollowedsSwordsSorcery.MOD_ID, "hollowed_blade_hand_shattered", "inventory"));
            }
        }
        if (stack.isOf(ModItems.CRYO_SHARD) && renderMode != ModelTransformationMode.GUI && renderMode != ModelTransformationMode.FIXED && renderMode != ModelTransformationMode.GROUND) {
            return ((ItemRendererAccessor) this).getModels().getModelManager().getModel(new ModelIdentifier(HollowedsSwordsSorcery.MOD_ID, "cryo_shard_hand", "inventory"));
        }
        return value;
    }
}