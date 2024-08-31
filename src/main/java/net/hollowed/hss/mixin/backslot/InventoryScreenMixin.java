package net.hollowed.hss.mixin.backslot;

import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.util.Identifier;
import net.minecraft.client.gui.DrawContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InventoryScreen.class)
public abstract class InventoryScreenMixin {

    @Unique
    private static final Identifier BACK_SLOT_TEXTURE = new Identifier("hss", "textures/gui/back_slot.png");

    @Inject(method = "drawBackground", at = @At("TAIL"))
    private void drawBackSlot(DrawContext context, float delta, int mouseX, int mouseY, CallbackInfo ci) {
        InventoryScreen screen = (InventoryScreen) (Object) this;
        int x = screen.x + 0; // Adjust the x position as needed
        int y = screen.y + 0; // Adjust the y position as needed

        // Bind the custom texture for the back slot
        context.getMatrices().push();
        context.drawTexture(BACK_SLOT_TEXTURE, x, y, 0, 0, 18, 18);
        context.getMatrices().pop();
    }
}
