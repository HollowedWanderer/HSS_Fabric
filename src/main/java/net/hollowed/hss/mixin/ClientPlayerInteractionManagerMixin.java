package net.hollowed.hss.mixin;

import net.hollowed.hss.common.networking.RightClickHandler;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.item.BlockItem;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientPlayerInteractionManager.class)
public class ClientPlayerInteractionManagerMixin {

    @Inject(method = "interactBlock", at = @At("HEAD"))
    private void onInteractBlock(CallbackInfoReturnable<Boolean> cir) {
        if (RightClickHandler.client == null || RightClickHandler.client.player == null) {
            return;
        }

        HitResult hitResult = RightClickHandler.client.crosshairTarget;

        assert hitResult != null;
        if (hitResult.getType() == HitResult.Type.BLOCK) {
            BlockHitResult blockHitResult = (BlockHitResult) hitResult;

            // Check if the item in the player's hand is a block
            boolean isPlacingBlock = RightClickHandler.client.player.getMainHandStack().getItem() instanceof BlockItem ||
                    RightClickHandler.client.player.getOffHandStack().getItem() instanceof BlockItem;

            if (!isPlacingBlock && RightClickHandler.isInteractingWithUsableBlock(blockHitResult)) {
                RightClickHandler.checkRightClickOnTopFace();
            }
        }
    }
}
