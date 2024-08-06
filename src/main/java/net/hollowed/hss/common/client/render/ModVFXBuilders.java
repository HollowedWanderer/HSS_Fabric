package net.hollowed.hss.common.client.render;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import team.lodestar.lodestone.helpers.VecHelper;
import team.lodestar.lodestone.systems.rendering.VFXBuilders;

import java.util.function.Consumer;

public class ModVFXBuilders extends VFXBuilders {
    public static ModVFXBuilders.WorldVFXBuilder createWorld() {
        return new ModVFXBuilders.WorldVFXBuilder();
    }
    public static class WorldVFXBuilder extends VFXBuilders.WorldVFXBuilder {
        public WorldVFXBuilder renderChain(Matrix4f last, BlockPos start, BlockPos end, float width) {
            return renderChain(last, VecHelper.getCenterOf(start), VecHelper.getCenterOf(end), width);
        }

        public WorldVFXBuilder renderChain(@Nullable Matrix4f last, Vec3d start, Vec3d end, float width) {
            MinecraftClient minecraft = MinecraftClient.getInstance();
            Vec3d cameraPosition = minecraft.getBlockEntityRenderDispatcher().camera.getPos();
            return renderChain(last, start, end, width, cameraPosition);
        }

        public WorldVFXBuilder renderChain(@Nullable Matrix4f last, Vec3d start, Vec3d end, float width, Consumer<WorldVFXBuilder> consumer) {
            MinecraftClient minecraft = MinecraftClient.getInstance();
            Vec3d cameraPosition = minecraft.getBlockEntityRenderDispatcher().camera.getPos();
            return renderChain(last, start, end, width, cameraPosition, consumer);
        }

        public WorldVFXBuilder renderChain(@Nullable Matrix4f last, Vec3d start, Vec3d end, float width, Vec3d cameraPosition) {
            return renderChain(last, start, end, width, cameraPosition, builder -> {});
        }

        public WorldVFXBuilder renderChain(@Nullable Matrix4f last, Vec3d start, Vec3d end, float width, Vec3d cameraPosition, Consumer<WorldVFXBuilder> consumer) {
            Vec3d delta = end.subtract(start);
            Vec3d normal = start.subtract(cameraPosition).crossProduct(delta).normalize().multiply(width / 2f, width / 2f, width / 2f);
            double length = delta.length();

            double u0 = 0.0;
            double u1 = 1.0;
            double v0 = 0.0;
            double v1 = length * 1/width;

            Vec3d[] positions = new Vec3d[]{start.subtract(normal), start.add(normal), end.add(normal), end.subtract(normal)};

            supplier.placeVertex(getVertexConsumer(), last, this, (float) positions[0].x, (float) positions[0].y, (float) positions[0].z, (float) u0, (float) v1);
            supplier.placeVertex(getVertexConsumer(), last, this, (float) positions[1].x, (float) positions[1].y, (float) positions[1].z, (float) u1, (float) v1);
            consumer.accept(this);
            supplier.placeVertex(getVertexConsumer(), last, this, (float) positions[2].x, (float) positions[2].y, (float) positions[2].z, (float) u1, (float) v0);
            supplier.placeVertex(getVertexConsumer(), last, this, (float) positions[3].x, (float) positions[3].y, (float) positions[3].z, (float) u0, (float) v0);

            return this;
        }

    }
}
