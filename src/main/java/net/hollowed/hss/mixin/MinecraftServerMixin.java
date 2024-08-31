package net.hollowed.hss.mixin;

import net.minecraft.SharedConstants;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {

    @Inject(method = "<init>", at = @At("RETURN"))
    private void onClientInit(CallbackInfo ci) {
        SharedConstants.isDevelopment = true;
    }
}
