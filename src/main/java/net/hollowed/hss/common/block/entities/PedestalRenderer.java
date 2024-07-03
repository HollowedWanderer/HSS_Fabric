package net.hollowed.hss.common.block.entities;

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

    private static Vec3d ITEM_POS = new Vec3d(0.5, 1.5, 0.5);

    @Override
    public void render(PedestalBlockEntity pedestalBlockEntity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        ItemStack heldItem = pedestalBlockEntity.getStack(0);

        if (!heldItem.isEmpty()) {
            PlayerEntity player = MinecraftClient.getInstance().player;
            assert player != null;
            World world = player.getWorld();
            float bob = (float) (Math.sin((player.age + tickDelta) * 0.1f) * 0.0875f);
            float rotation = player.age * 2 + tickDelta;
            renderItem(heldItem, ITEM_POS.add(0, bob, 0), rotation, pedestalBlockEntity, tickDelta, matrices, vertexConsumers, light, overlay, world);
        }
    }

    public void renderItem(ItemStack itemStack, Vec3d offset, float yRot, PedestalBlockEntity pedestalBlockEntity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, World world) {
        if (itemStack.isEmpty()) {
            return;  // Do not render if the item stack is empty
        }

        matrices.push();
        int renderId = (int) pedestalBlockEntity.getPos().asLong();
        ItemRenderer itemRenderer = MinecraftClient.getInstance().getItemRenderer();

        matrices.translate(offset.x, offset.y, offset.z);
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(yRot));
        matrices.scale(0.65f, 0.65f, 0.65f);
        if (itemStack.getItem() instanceof SwordItem || itemStack.getItem() instanceof ToolItem) {
            matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(-45));
            ITEM_POS = new Vec3d(0.5, 1.55, 0.5);
        }

        itemRenderer.renderItem(itemStack, ModelTransformationMode.FIXED, light, overlay, matrices, vertexConsumers, world, renderId);
        matrices.pop();
    }
}
