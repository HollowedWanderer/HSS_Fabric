package net.hollowed.hss.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.CameraSubmersionType;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeKeys;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BackgroundRenderer.class)
public class BackgroundRendererMixin {

    @Shadow
    private static float red;
    @Shadow
    private static float green;
    @Shadow
    private static float blue;

    @Shadow
    private static int waterFogColor = -1;
    @Shadow
    private static int nextWaterFogColor = -1;
    @Shadow
    private static long lastWaterFogColorUpdateTime = -1L;

    @Unique
    private static float currentFogStart = 0.0f;
    @Unique
    private static float currentFogEnd = 0.0f;
    @Unique
    private static float targetFogStart = 0.0f;
    @Unique
    private static float targetFogEnd = 0.0f;
    @Unique
    private static long lastUpdateTime = System.currentTimeMillis();

    @Inject(method = "render", at = @At("RETURN"))
    private static void onRenderEnd(Camera camera, float tickDelta, ClientWorld world, int viewDistance, float skyDarkness, CallbackInfo ci) {
        CameraSubmersionType cameraSubmersionType = camera.getSubmersionType();
        Vec3d cameraPos = camera.getPos();
        double yPos = cameraPos.y;

        float f;
        float g;
        float h;

        // Define sea level and transition range
        double seaLevel = 55.0;
        double transitionRange = 32.0;  // Range over which the transition occurs

        // Calculate transition factor
        double transitionFactor = MathHelper.clamp((seaLevel - yPos + transitionRange) / (2 * transitionRange), 0.0, 1.0);

        // Apply color changes based on submersion type
        if (cameraSubmersionType == CameraSubmersionType.WATER) {
            long l = Util.getMeasuringTimeMs();
            int i = world.getBiome(BlockPos.ofFloored(camera.getPos())).value().getWaterFogColor();
            if (lastWaterFogColorUpdateTime < 0L) {
                waterFogColor = i;
                nextWaterFogColor = i;
                lastWaterFogColorUpdateTime = l;
            }

            int j = waterFogColor >> 16 & 255;
            int k = waterFogColor >> 8 & 255;
            int m = waterFogColor & 255;
            int n = nextWaterFogColor >> 16 & 255;
            int o = nextWaterFogColor >> 8 & 255;
            int p = nextWaterFogColor & 255;
            f = MathHelper.clamp((float) (l - lastWaterFogColorUpdateTime) / 5000.0F, 0.0F, 1.0F);
            g = MathHelper.lerp(f, (float) n, (float) j);
            h = MathHelper.lerp(f, (float) o, (float) k);
            float q = MathHelper.lerp(f, (float) p, (float) m);
            red = g / 255.0F;
            green = h / 255.0F;
            blue = q / 255.0F;
            if (waterFogColor != i) {
                waterFogColor = i;
                nextWaterFogColor = MathHelper.floor(g) << 16 | MathHelper.floor(h) << 8 | MathHelper.floor(q);
                lastWaterFogColorUpdateTime = l;
            }
        } else if (cameraSubmersionType == CameraSubmersionType.LAVA) {
            red = 0.9f;   // Reddish color for lava
            green = 0.3f;
            blue = 0.0f;
        } else if (cameraSubmersionType == CameraSubmersionType.POWDER_SNOW) {
            red = 0.6f;   // Whitish color for powder snow
            green = 0.8f;
            blue = 0.9f;
        } else {
            // Apply the transition effect for non-submersion types
            red = (float) (0.0 * transitionFactor + MathHelper.clamp(red * 1.5f, 0.0f, 0.92f) * (1 - transitionFactor));
            green = (float) (0.0 * transitionFactor + MathHelper.clamp(green * 1.5f, 0.0f, 1.0f) * (1 - transitionFactor));
            blue = (float) (0.0 * transitionFactor + MathHelper.clamp(blue * 1.5f, 0.0f, 1.05f) * (1 - transitionFactor));
        }
        // Apply the new color to the RenderSystem
        RenderSystem.clearColor(red, green, blue, 0.0f);
    }

    @Inject(method = "applyFog", at = @At("RETURN"))
    private static void onApplyFog(Camera camera, BackgroundRenderer.FogType fogType, float viewDistance, boolean thickFog, float tickDelta, CallbackInfo ci) {
        if (camera.getFocusedEntity() instanceof LivingEntity livingEntity) {
            if (livingEntity.hasStatusEffect(StatusEffects.BLINDNESS)) {
                return;
            } else if (livingEntity.hasStatusEffect(StatusEffects.DARKNESS)) {
                return;
            }
        }

        // Apply view distance scaling
        targetFogEnd = Math.min(targetFogEnd, viewDistance);
        targetFogStart = Math.min(viewDistance - 40, targetFogStart);

        CameraSubmersionType cameraSubmersionType = camera.getSubmersionType();
        World world = camera.getFocusedEntity().getWorld();
        Vec3d cameraPos = camera.getPos();
        double yPos = cameraPos.y;

        // Define sea level and transition range
        double seaLevel = 55.0;
        double transitionRange = 32.0;  // Range over which the transition occurs

        // Calculate transition factor
        double transitionFactor = MathHelper.clamp((seaLevel - yPos + transitionRange) / (2 * transitionRange), 0.0, 1.0);

        // Determine the target fog settings based on biome and submersion type
        if (cameraSubmersionType == CameraSubmersionType.WATER) {
            targetFogStart = 0.0f;
            targetFogEnd = 30.0f;  // Default water fog distance
        } else if (cameraSubmersionType == CameraSubmersionType.LAVA) {
            targetFogStart = 0.25f;
            targetFogEnd = 10.0f;   // Default lava fog distance
        } else if (cameraSubmersionType == CameraSubmersionType.POWDER_SNOW) {
            targetFogStart = 0.0f;
            targetFogEnd = 2.0f;   // Default powder snow fog distance
        } else {
            // Check if the player is in a mangrove swamp biome
            boolean isInMangroveSwamp = world.getBiome(camera.getBlockPos()).matchesKey(BiomeKeys.MANGROVE_SWAMP);
            boolean isInSwamp = world.getBiome(camera.getBlockPos()).matchesKey(BiomeKeys.SWAMP);
            boolean isInDarkForest = world.getBiome(camera.getBlockPos()).matchesKey(BiomeKeys.DARK_FOREST);
            boolean isInDesert = world.getBiome(camera.getBlockPos()).matchesKey(BiomeKeys.DESERT);

            // Get the time of day in ticks (0 to 23999)
            long timeOfDay = world.getTimeOfDay() % 24000;

            // Set fog density based on time of day
            if (timeOfDay < 1000 && !isInDesert) { // Morning
                targetFogStart = 20.0f;
                targetFogEnd = 150.0f;
            } else if (timeOfDay < 12000) { // Noon
                targetFogStart = 100.0f;
                targetFogEnd = 600.0f;
            } else if (timeOfDay < 18000) { // Evening
                targetFogStart = 20.0f;
                targetFogEnd = 350.0f;
            } else { // Night
                targetFogStart = 0.0f;
                targetFogEnd = 300.0f;
            }

            if (isInMangroveSwamp && yPos < 120) {
                // Target values for extremely thick fog in mangrove swamp
                targetFogStart = 0.0f;
                targetFogEnd = (float) (targetFogEnd * 0.166);
            } else if (isInSwamp && yPos < 120) {
                // Target values for thick fog in swamp
                targetFogStart = 0.0f;
                targetFogEnd = (float) (targetFogEnd * 0.266);
            } else if (isInDarkForest && yPos < 120) {
                // Target values for thick fog in dark forest
                targetFogStart = 0.0f;
                targetFogEnd = (float) (targetFogEnd * 0.266);
            }

            if (yPos < seaLevel) {
                targetFogStart = (float) MathHelper.lerp(transitionFactor, 20.0f, 0.0f);
                targetFogEnd = (float) MathHelper.lerp(transitionFactor, 120.0f, 80.0f);
            }

            // Apply view distance scaling
            targetFogEnd = Math.min(targetFogEnd, viewDistance);
            targetFogStart = Math.min(viewDistance - 60, targetFogStart);

            // Smoothly transition towards the target fog settings
            long currentTime = System.currentTimeMillis();
            float transitionSpeed = 0.5f;  // Adjust this value to change the transition speed
            float deltaTime = (currentTime - lastUpdateTime) / 1000.0f;

            currentFogStart = MathHelper.lerp(transitionSpeed * deltaTime, currentFogStart, targetFogStart);
            currentFogEnd = MathHelper.lerp(transitionSpeed * deltaTime, currentFogEnd, targetFogEnd);

            // Apply fog settings to RenderSystem
            RenderSystem.setShaderFogStart(currentFogStart);
            RenderSystem.setShaderFogEnd(currentFogEnd);

            lastUpdateTime = currentTime;
        }
    }
}
