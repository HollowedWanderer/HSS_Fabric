// Made with Blockbench 4.10.4
// Exported for Minecraft version 1.17+ for Yarn
// Paste this class into your mod and generate all required imports

package net.hollowed.hss.common.client.models;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.*;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;

import java.awt.*;

public class ChilledModel extends EntityModel<PlayerEntity> {

	public final ModelPart right_leg;
    public final ModelPart left_leg;
    public final ModelPart right_arm;
    public final ModelPart left_arm;
    public final ModelPart body;
    public final ModelPart head;

    public ChilledModel(ModelPart root) {
		this.right_leg = root.getChild("right_leg");
        this.left_leg = root.getChild("left_leg");
        this.right_arm = root.getChild("right_arm");
        this.left_arm = root.getChild("left_arm");
        this.body = root.getChild("body");
        this.head = root.getChild("head");
    }
	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData right_leg = modelPartData.addChild("right_leg", ModelPartBuilder.create().uv(16, 44).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(-2.0F, 12.0F, 0.0F));

		ModelPartData right_pant = right_leg.addChild("right_pant", ModelPartBuilder.create().uv(68, 0).cuboid(-3.0F, -14.0F, -2.0F, 4.0F, 14.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(1.0F, 12.0F, 0.0F));

		ModelPartData left_leg = modelPartData.addChild("left_leg", ModelPartBuilder.create().uv(0, 44).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(2.0F, 12.0F, 0.0F));

		ModelPartData left_pant = left_leg.addChild("left_pant", ModelPartBuilder.create().uv(0, 60).cuboid(-3.0F, -14.0F, -2.0F, 4.0F, 14.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(1.0F, 12.0F, 0.0F));

		ModelPartData right_arm = modelPartData.addChild("right_arm", ModelPartBuilder.create().uv(46, 44).cuboid(-2.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, new Dilation(0.0F))
				.uv(56, 6).cuboid(-4.0F, -2.0F, -2.0F, 2.0F, 2.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(-5.0F, 2.0F, 0.0F));

		ModelPartData right_sleeve = right_arm.addChild("right_sleeve", ModelPartBuilder.create().uv(44, 64).cuboid(-2.0F, -10.0F, -2.0F, 3.0F, 12.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 8.0F, 0.0F));

		ModelPartData left_arm = modelPartData.addChild("left_arm", ModelPartBuilder.create().uv(32, 44).cuboid(-1.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, new Dilation(0.0F))
				.uv(56, 0).cuboid(0.0F, -4.0F, -2.0F, 2.0F, 2.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(5.0F, 2.0F, 0.0F));

		ModelPartData left_sleeve = left_arm.addChild("left_sleeve", ModelPartBuilder.create().uv(58, 64).cuboid(-3.0F, -10.0F, -2.0F, 3.0F, 12.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(2.0F, 8.0F, 0.0F));

		ModelPartData body = modelPartData.addChild("body", ModelPartBuilder.create().uv(32, 0).cuboid(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData body_layer = body.addChild("body_layer", ModelPartBuilder.create().uv(0, 87).cuboid(-3.0F, -10.0F, -2.0F, 8.0F, 12.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(-1.0F, 10.0F, 0.0F));

		ModelPartData head = modelPartData.addChild("head", ModelPartBuilder.create().uv(0, 16).cuboid(-10.0F, -14.0F, -3.0F, 14.0F, 14.0F, 0.0F, new Dilation(0.0F))
				.uv(0, 0).cuboid(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new Dilation(0.0F))
				.uv(0, 30).cuboid(-10.0F, -14.0F, -1.0F, 14.0F, 14.0F, 0.0F, new Dilation(0.0F))
				.uv(28, 30).cuboid(-10.0F, -14.0F, 1.0F, 14.0F, 14.0F, 0.0F, new Dilation(0.0F))
				.uv(28, 16).cuboid(-10.0F, -14.0F, 3.0F, 14.0F, 14.0F, 0.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData hat = head.addChild("hat", ModelPartBuilder.create().uv(0, 112).cuboid(-3.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new Dilation(0.0F).add(0.25F)), ModelTransform.pivot(-1.0F, 0.0F, 0.0F));
		return TexturedModelData.of(modelData, 128, 128);
	}

	@Override
	public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
		AbstractClientPlayerEntity playerEntity = MinecraftClient.getInstance().player;
		if (playerEntity != null) {
			if (!playerEntity.getStatusEffects().isEmpty()) {
				Color color = new Color(0, 0, 0);
				for (StatusEffectInstance effect : playerEntity.getStatusEffects()) {
					color = new Color(effect.getEffectType().getColor());
				}

				RenderSystem.setShaderColor((float) (color.getRed() * 0.01), (float) (color.getGreen() * 0.01), (float) (color.getBlue() * 0.01), 1);
			}
		}
		right_leg.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
		left_leg.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
		right_arm.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
		left_arm.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
		body.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
		head.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);

		RenderSystem.setShaderColor(1, 1, 1, 1);
	}

	@Override
	public void setAngles(PlayerEntity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {

	}
}