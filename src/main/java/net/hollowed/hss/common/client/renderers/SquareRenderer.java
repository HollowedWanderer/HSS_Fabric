package net.hollowed.hss.common.client.renderers;

import net.hollowed.hss.HollowedsSwordsSorcery;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.joml.Vector3f;
import team.lodestar.lodestone.handlers.RenderHandler;
import team.lodestar.lodestone.registry.client.LodestoneRenderTypeRegistry;
import team.lodestar.lodestone.systems.rendering.LodestoneRenderType;
import team.lodestar.lodestone.systems.rendering.VFXBuilders;
import team.lodestar.lodestone.systems.rendering.rendeertype.RenderTypeToken;

import java.awt.*;

public class SquareRenderer {

    private static RenderTypeToken getRenderTypeToken() {
        return RenderTypeToken.createToken(new Identifier(HollowedsSwordsSorcery.MOD_ID, "textures/vfx/white.png"));
    }

    private static RenderTypeToken getRenderTypeToken2() {
        return RenderTypeToken.createToken(new Identifier(HollowedsSwordsSorcery.MOD_ID, "textures/vfx/white_mirror.png"));
    }

    private static final LodestoneRenderType RENDER_LAYER = LodestoneRenderTypeRegistry.TRANSPARENT_TEXTURE.applyAndCache(
            getRenderTypeToken());
    private static final LodestoneRenderType RENDER_LAYER2 = LodestoneRenderTypeRegistry.TRANSPARENT_TEXTURE.applyAndCache(
            getRenderTypeToken2());

    public static void renderSquare(MatrixStack matrixStack, float x, float y, float z, float size) {
        VFXBuilders.WorldVFXBuilder builder = VFXBuilders.createWorld();
        builder.replaceBufferSource(RenderHandler.LATE_DELAYED_RENDER.getTarget())
                .setRenderType(RENDER_LAYER)
                .setColor(new Color(255, 255, 255, 255))
                .setAlpha(1.0f); // Ensure full opacity

        float halfSize = size / 2.0f;

        // Define squareFront vertices
        Vector3f[] squareFront = new Vector3f[]{
                createVec3f(x - halfSize, y, z - halfSize),
                createVec3f(x + halfSize, y, z - halfSize),
                createVec3f(x + halfSize, y, z + halfSize),
                createVec3f(x - halfSize, y, z + halfSize)
        };

        matrixStack.push();
        builder.renderQuad(matrixStack, squareFront, 1f);
        matrixStack.pop();
    }

    public static void renderSquare2(MatrixStack matrixStack, float x, float y, float z, float size) {
        VFXBuilders.WorldVFXBuilder builder = VFXBuilders.createWorld();
        builder.replaceBufferSource(RenderHandler.LATE_DELAYED_RENDER.getTarget())
                .setRenderType(RENDER_LAYER2)
                .setColor(new Color(255, 255, 255, 255))
                .setAlpha(1.0f); // Ensure full opacity

        float halfSize = size / 2.0f;

        // Create squareBack by mirroring squareFront along the x-axis
        Vector3f[] squareBack = new Vector3f[]{
                createVec3f(x - halfSize, y, z - halfSize),
                createVec3f(x + halfSize, y, z - halfSize),
                createVec3f(x + halfSize, y, z + halfSize),
                createVec3f(x - halfSize, y, z + halfSize)
        };

        // Adjust z coordinates to mirror squareBack along the x-axis
        for (int i = 0; i < squareBack.length; i++) {
            squareBack[i] = new Vector3f(-squareBack[i].x(), squareBack[i].y(), squareBack[i].z());
        }

        matrixStack.push();
        builder.renderQuad(matrixStack, squareBack, 1f);
        matrixStack.pop();
    }

    private static Vector3f createVec3f(float x, float y, float z) {
        return new Vector3f(x, y, z);
    }
}
