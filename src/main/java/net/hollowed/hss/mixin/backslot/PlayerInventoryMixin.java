package net.hollowed.hss.mixin.backslot;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerInventory.class)
public class PlayerInventoryMixin {

    @Inject(method = "markDirty", at = @At("HEAD"))
    private void onMarkDirty(CallbackInfo ci) {
        PlayerEntity player = ((PlayerInventory) (Object) this).player;
        if (player.currentScreenHandler != null) {
            player.currentScreenHandler.sendContentUpdates();
        }
    }
}
