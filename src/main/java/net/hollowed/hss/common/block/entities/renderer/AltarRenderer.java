package net.hollowed.hss.common.block.entities.renderer;

import net.hollowed.hss.common.block.entities.ResonatingAltarBlockEntity;
import net.hollowed.hss.common.client.renderers.ThaumiteLaserRenderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolItem;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import team.lodestar.lodestone.handlers.ScreenshakeHandler;
import team.lodestar.lodestone.systems.easing.Easing;
import team.lodestar.lodestone.systems.screenshake.PositionedScreenshakeInstance;
import team.lodestar.lodestone.systems.screenshake.ScreenshakeInstance;

import static net.hollowed.hss.common.block.entities.ResonatingAltarBlockEntity.HEIGHT_INCREMENT;
import static net.hollowed.hss.common.block.entities.ResonatingAltarBlockEntity.ROTATION_INCREMENT;

public class AltarRenderer implements BlockEntityRenderer<ResonatingAltarBlockEntity> {

    private static final Vec3d ITEM_POS = new Vec3d(0.5, 2.5, 0.5);

    public AltarRenderer(BlockEntityRendererFactory.Context context) {
    }

    @Override
    public void render(ResonatingAltarBlockEntity altarBlockEntity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        ItemStack heldItem = altarBlockEntity.getStack(0);
        PlayerEntity player = MinecraftClient.getInstance().player;

        if (player != null) {
            World world = player.getWorld();

            if (heldItem.isEmpty()) {
                altarBlockEntity.yOffset = 0F;
                altarBlockEntity.rotationSpeed = 0.05F;
            }

            altarBlockEntity.rotationSpeed += ROTATION_INCREMENT;
            altarBlockEntity.yOffset += HEIGHT_INCREMENT;

            // Calculate rotation based on player age and tickDelta
            float rotation = (world.getTime() + tickDelta) * altarBlockEntity.rotationSpeed;

            // Render the item with the updated offset and rotation
            renderItem(heldItem, ITEM_POS.add(0, altarBlockEntity.yOffset, 0), rotation, matrices, vertexConsumers, light, overlay, world, altarBlockEntity);
        }
    }

    private void renderItem(ItemStack itemStack, Vec3d offset, float yRot, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, World world, ResonatingAltarBlockEntity entity) {
        matrices.push();
        matrices.translate(offset.x, 0, offset.z);

        if (!itemStack.isEmpty() && entity.yOffset > 0.5) {
            ThaumiteLaserRenderer.renderLaser(matrices, entity.getPos().getX(), entity.getPos().getY(), entity.getPos().getZ(), 1f, entity);
        }

        if (!itemStack.isEmpty() && entity.yOffset > 1.5) {
            ScreenshakeHandler.addScreenshake(new PositionedScreenshakeInstance(20, new Vec3d(entity.getPos().getX(), entity.getPos().getY(), entity.getPos().getZ()), 25, 40).setIntensity(0.004F).setEasing(Easing.EXPO_IN_OUT));
        } else if (!itemStack.isEmpty() && entity.yOffset > 1) {
            ScreenshakeHandler.addScreenshake(new PositionedScreenshakeInstance(20, new Vec3d(entity.getPos().getX(), entity.getPos().getY(), entity.getPos().getZ()), 10, 20).setIntensity(0.006F).setEasing(Easing.EXPO_IN_OUT));
        } else if (!itemStack.isEmpty() && entity.yOffset > 0.5) {
            ScreenshakeHandler.addScreenshake(new PositionedScreenshakeInstance(20, new Vec3d(entity.getPos().getX(), entity.getPos().getY(), entity.getPos().getZ()), 6, 10).setIntensity(0.005F).setEasing(Easing.EXPO_IN_OUT));
        }

        matrices.translate(0, offset.y, 0);

        ItemRenderer itemRenderer = MinecraftClient.getInstance().getItemRenderer();

        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(yRot));
        matrices.scale(0.65f, 0.65f, 0.65f);

        if (itemStack.getItem() instanceof SwordItem || itemStack.getItem() instanceof ToolItem) {
            matrices.translate(0, 0.1f, 0);
            matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(45));
        }

        itemRenderer.renderItem(itemStack, ModelTransformationMode.GUI, light, overlay, matrices, vertexConsumers, world, (int) world.getTime());
        matrices.pop();
    }
}