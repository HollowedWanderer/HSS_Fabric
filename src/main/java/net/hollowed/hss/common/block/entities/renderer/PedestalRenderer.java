package net.hollowed.hss.common.block.entities.renderer;

import net.hollowed.hss.common.block.entities.PedestalBlockEntity;
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

public class PedestalRenderer implements BlockEntityRenderer<PedestalBlockEntity> {

    public PedestalRenderer(BlockEntityRendererFactory.Context context) {
    }

    private static final Vec3d ITEM_POS = new Vec3d(0.5, 1.5, 0.5);

    @Override
    public void render(PedestalBlockEntity pedestalBlockEntity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        ItemStack heldItem = pedestalBlockEntity.getStack(0);

        MinecraftClient client = MinecraftClient.getInstance();

        // Only proceed with rendering if the item stack is not empty
        if (!heldItem.isEmpty()) {
            PlayerEntity player = MinecraftClient.getInstance().player;
            if (player != null) {
                World world = player.getWorld();
                long worldTime = world.getTime(); // Get the current world time

                // Linear interpolation for smoother bobbing and rotation
                float previousRotation = (worldTime - 1) * 2; // Previous frame's rotation
                float currentRotation = worldTime * 2;        // Current frame's rotation
                float rotation = lerp(client.getTickDelta(), previousRotation, currentRotation);

                float previousBob = (float) (Math.sin((worldTime - 1) * 0.1f) * 0.0875f); // Previous frame's bob
                float currentBob = (float) (Math.sin(worldTime * 0.1f) * 0.0875f);        // Current frame's bob
                float bob = lerp(client.getTickDelta(), previousBob, currentBob);

                // Render the item with smooth rotation and bobbing
                renderItem(heldItem, ITEM_POS.add(0, bob, 0), rotation, matrices, vertexConsumers, light, overlay, world);
            }
        }
    }

    // Linear interpolation method (lerp)
    private float lerp(float delta, float start, float end) {
        return start + delta * (end - start);
    }


    private void renderItem(ItemStack itemStack, Vec3d offset, float yRot, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, World world) {
        matrices.push();

        ItemRenderer itemRenderer = MinecraftClient.getInstance().getItemRenderer();
        matrices.translate(offset.x, offset.y, offset.z);
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
