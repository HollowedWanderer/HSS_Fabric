package net.hollowed.hss.common.worldEvents;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.hollowed.hss.common.client.renderers.SquareRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import team.lodestar.lodestone.systems.worldevent.WorldEventRenderer;

@Environment(EnvType.CLIENT)
public class TestEventRenderer extends WorldEventRenderer<TestEvent> {

    @Override
    public boolean canRender(TestEvent instance) {
        return true;
    }

    @Override
    public void render(TestEvent instance, MatrixStack poseStack, VertexConsumerProvider bufferSource, float partialTicks) {
        SquareRenderer.renderSquare(poseStack, 0, 100, 0, 250);
        SquareRenderer.renderSquare2(poseStack, 0, 100, 0, 250);
    }
}