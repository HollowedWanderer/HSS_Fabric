package net.hollowed.hss.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.projectile.ProjectileEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class ProjectileBypassMixin {

    @Shadow protected float lastDamageTaken;

    @Inject(method = "damage", at = @At("HEAD"))
    private void bypassImmunityFrames(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (source.getSource() instanceof ProjectileEntity) {
            ProjectileBypassMixin entity = this;
            entity.lastDamageTaken = 0;
        }
    }
}