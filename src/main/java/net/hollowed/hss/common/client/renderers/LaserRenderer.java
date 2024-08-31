package net.hollowed.hss.common.client.renderers;

import net.hollowed.hss.HollowedsSwordsSorcery;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import team.lodestar.lodestone.handlers.RenderHandler;
import team.lodestar.lodestone.registry.client.LodestoneRenderTypeRegistry;
import team.lodestar.lodestone.systems.rendering.LodestoneRenderType;
import team.lodestar.lodestone.systems.rendering.rendeertype.RenderTypeToken;

import java.awt.*;

public class LaserRenderer {

    private static RenderTypeToken getRenderTypeToken() {
        return RenderTypeToken.createToken(new Identifier(HollowedsSwordsSorcery.MOD_ID, "textures/vfx/chain.png"));
    }

    private static final LodestoneRenderType RENDER_LAYER = LodestoneRenderTypeRegistry.TRANSPARENT_TEXTURE.applyAndCache(
            getRenderTypeToken());

    public static void renderLaser(MatrixStack matrixStack, float x, float y, float z, float size, Entity entity) {
        ModVFXBuilders.WorldVFXBuilder builder = ModVFXBuilders.createWorld();
        builder.replaceBufferSource(RenderHandler.LATE_DELAYED_RENDER.getTarget())
                .setRenderType(RENDER_LAYER)
                .setColor(new Color(255, 255, 255, 255))
                .setAlpha(1.0f);
        matrixStack.push();

        Matrix4f matrix4f = matrixStack.peek().getPositionMatrix();

        Vec3d startPos = new Vec3d(x, y, z);
        Vec3d endPos = new Vec3d(x, y + 50, z);

        matrixStack.translate(-entity.getX(), -entity.getY(), -entity.getZ());
        builder.renderChain(matrix4f, startPos, endPos, size);

        matrixStack.pop();
    }
}
