package net.hollowed.hss.common.entity.renderer;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.hollowed.hss.common.entity.custom.ItemProjectileEntity;
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

		if(!(entity.getOwner() instanceof LivingEntity living)) return;

		ItemStack stack = entity.asItemStack();
		matrices.push();

			float lerpedYaw = MathHelper.lerp(tickDelta, entity.prevYaw, entity.getYaw());
			float lerpedPitch = MathHelper.lerp(tickDelta, entity.prevPitch, entity.getPitch());

			matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(lerpedYaw - 180.0F));
			matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(lerpedPitch - 90.0F));
			heldItemRenderer.renderItem(
				living,
				stack,
				ModelTransformationMode.THIRD_PERSON_RIGHT_HAND,
				false,
				matrices,
				vertexConsumers,
				light
			);

		matrices.pop();

	}

	@Override
	public Identifier getTexture(ItemProjectileEntity entity) {
		return null;
	}

}