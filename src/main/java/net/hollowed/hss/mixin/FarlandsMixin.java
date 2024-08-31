package net.hollowed.hss.mixin;

import net.minecraft.util.math.noise.OctavePerlinNoiseSampler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({OctavePerlinNoiseSampler.class})
public class FarlandsMixin {
    public FarlandsMixin() {

    }

    @Inject(method = {"maintainPrecision"}, at = {@At("HEAD")}, cancellable = true)
    private static void injectMethod(double value, CallbackInfoReturnable<Double> cir) {
        cir.cancel();
        cir.setReturnValue(value);
    }
}