package net.hollowed.hss.common.entity.renderer;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.hollowed.hss.common.entity.custom.ItemProjectileEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;

@Environment(EnvType.CLIENT)
public class ItemProjectileEntityRenderer extends EntityRenderer<ItemProjectileEntity> {

	private final HeldItemRenderer heldItemRenderer;

	public ItemProjectileEntityRenderer(EntityRendererFactory.Context ctx) {
		super(ctx);
		this.heldItemRenderer = ctx.getHeldItemRenderer();
	}

	@Override
	public void render(ItemProjectileEntity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
		long seed = entity.getUuid().getMostSignificantBits();
		ItemStack stack = entity.asItemStack();
		matrices.push();

		float lerpedYaw = MathHelper.lerp(tickDelta, entity.prevYaw, entity.getYaw());
		float lerpedPitch = MathHelper.lerp(tickDelta, entity.prevPitch, entity.getPitch());

		matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(lerpedYaw - 180.0F));
		matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(lerpedPitch - 90.0F));

		// Check if the owner is null
		if (entity.getOwner() != null) {
			heldItemRenderer.renderItem(
					(LivingEntity) entity.getOwner(),
					stack,
					ModelTransformationMode.THIRD_PERSON_RIGHT_HAND,
					false,
					matrices,
					vertexConsumers,
					light
			);
		} else {
			// Manually render the item stack without requiring an owner
			MinecraftClient.getInstance().getItemRenderer().renderItem(
					stack,
					ModelTransformationMode.THIRD_PERSON_RIGHT_HAND,
					light,
					0,
					matrices,
					vertexConsumers,
					entity.getWorld(),
                    (int) seed
            );
		}

		matrices.pop();
	}

	@Override
	public Identifier getTexture(ItemProjectileEntity entity) {
		return null;
	}

}