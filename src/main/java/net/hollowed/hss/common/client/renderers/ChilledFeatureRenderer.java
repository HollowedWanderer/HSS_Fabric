package net.hollowed.hss.common.client.renderers;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.hollowed.hss.common.client.models.ChilledModel;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

import java.util.Random;

@Environment(EnvType.CLIENT)
public class ChilledFeatureRenderer extends FeatureRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {
	private final ChilledModel chilledModel;
	private final Random random = new Random();
	private long lastBlinkTime = 0;
	private boolean isBlinking = false;

	private static final Identifier NORMAL_TEXTURE = new Identifier("hss", "textures/entity/ice_player.png");
	private static final Identifier BLINK_TEXTURE = new Identifier("hss", "textures/entity/ice_player_blink.png");
	private static final long BLINK_DURATION = 400L; // Blink duration in milliseconds (100ms)
	private static final long BLINK_COOLDOWN = 4000L; // Cooldown period in milliseconds (3000ms = 3 seconds)
	private static final double BLINK_CHANCE = 0.003; // Blink chance (0.1%)

	public ChilledFeatureRenderer(FeatureRendererContext<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> context, ModelPart modelPart) {
		super(context);
		this.chilledModel = new ChilledModel(modelPart);
	}

	@Override
	public void render(
			MatrixStack matrixStack,
			VertexConsumerProvider vertexConsumerProvider,
			int light,
			AbstractClientPlayerEntity playerEntity,
			float limbAngle,
			float limbDistance,
			float tickDelta,
			float age,
			float headYaw,
			float headPitch
	) {
		if (!playerEntity.isInvisible() && "HollowedWanderer".equals(playerEntity.getName().getString())) {
			matrixStack.push();

			// Sync ChilledModel with PlayerEntityModel
			PlayerEntityModel<AbstractClientPlayerEntity> playerModel = this.getContextModel();
			syncModelPose(playerModel);

			// Render chilledModel
			VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(RenderLayer.getEntityTranslucent(getTexture(playerEntity)));
			chilledModel.render(matrixStack, vertexConsumer, light, LivingEntityRenderer.getOverlay(playerEntity, 0.0F), 1.0F, 1.0F, 1.0F, 1.0F);
			matrixStack.pop();
		}
	}

	private void syncModelPose(PlayerEntityModel<AbstractClientPlayerEntity> playerModel) {
		// Sync head
		chilledModel.head.copyTransform(playerModel.head);

		// Sync body
		chilledModel.body.copyTransform(playerModel.body);

		// Sync arms
		chilledModel.left_arm.copyTransform(playerModel.leftArm);
		chilledModel.right_arm.copyTransform(playerModel.rightArm);

		// Sync legs
		chilledModel.left_leg.copyTransform(playerModel.leftLeg);
		chilledModel.right_leg.copyTransform(playerModel.rightLeg);
	}

	@Override
	public Identifier getTexture(AbstractClientPlayerEntity playerEntity) {
		long currentTime = System.currentTimeMillis();

		// Check if we are blinking
		if (isBlinking && (currentTime - lastBlinkTime) > BLINK_DURATION) {
			isBlinking = false;
			lastBlinkTime = currentTime; // Start cooldown
		}

		// Check if we can start blinking (not in cooldown)
		if (!isBlinking && (currentTime - lastBlinkTime) > BLINK_COOLDOWN) {
			// Randomly trigger blinking
			if (random.nextDouble() < BLINK_CHANCE) {
				isBlinking = true;
				lastBlinkTime = currentTime;
			}
		}

		// Return the appropriate texture
		return isBlinking ? BLINK_TEXTURE : NORMAL_TEXTURE;
	}
}