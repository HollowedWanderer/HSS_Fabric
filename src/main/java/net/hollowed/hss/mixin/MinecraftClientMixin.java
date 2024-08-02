package net.hollowed.hss.mixin;

import net.hollowed.hss.common.client.particles.ModScreenParticles;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {

    @Inject(method = "<init>", at = @At("RETURN"))
    private void onClientInit(CallbackInfo ci) {
        ModScreenParticles.registerParticleFactory();
    }
}
