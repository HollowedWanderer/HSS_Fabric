package net.hollowed.hss.common.block.entities;

import net.hollowed.hss.common.client.render.LaserRenderer;
import net.hollowed.hss.common.client.render.SquareRenderer;
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

import java.util.Objects;
import java.util.Random;

public class PedestalRenderer implements BlockEntityRenderer<PedestalBlockEntity> {

    public PedestalRenderer(BlockEntityRendererFactory.Context context) {
    }

    private static final Vec3d ITEM_POS = new Vec3d(0.5, 1.5, 0.5);

    @Override
    public void render(PedestalBlockEntity pedestalBlockEntity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        ItemStack heldItem = pedestalBlockEntity.getStack(0);

        // Debug: Check if rendering is proceeding
        //System.out.println("Rendering: " + heldItem + " | Is Client: " + Objects.requireNonNull(pedestalBlockEntity.getWorld()).isClient());

        // Only proceed with rendering if the item stack is not empty
        if (!heldItem.isEmpty()) {
            PlayerEntity player = MinecraftClient.getInstance().player;
            if (player != null) {
                World world = player.getWorld();
                float bob = (float) (Math.sin((player.age + tickDelta) * 0.1f) * 0.0875f);
                float rotation = player.age * 2 + tickDelta;
                renderItem(heldItem, ITEM_POS.add(0, bob, 0), rotation, matrices, vertexConsumers, light, overlay, world);
            }
        }
    }

    private void renderItem(ItemStack itemStack, Vec3d offset, float yRot, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, World world) {
        matrices.push();
        //LaserRenderer.renderBeam(matrices, 0f, 0f, 0f, 1f);

        // Debug: Log render call
        //System.out.println("Rendering item at offset: " + offset + " with rotation: " + yRot);

        ItemRenderer itemRenderer = MinecraftClient.getInstance().getItemRenderer();
        matrices.translate(offset.x, offset.y, offset.z);
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(yRot));
        matrices.scale(0.65f, 0.65f, 0.65f);

        if (itemStack.getItem() instanceof SwordItem || itemStack.getItem() instanceof ToolItem) {
            matrices.translate(0, 0.1f, 0);
            matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(-45));
        }

        //matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-90));
        //SquareRenderer.renderSquare(matrices, 0f, 0f, 5f, 1.0f);
        //SquareRenderer.renderSquare2(matrices, 0f, 0f, 5f, 1.0f);
        itemRenderer.renderItem(itemStack, ModelTransformationMode.FIXED, light, overlay, matrices, vertexConsumers, world, (int) world.getTime());
        matrices.pop();
    }
}
