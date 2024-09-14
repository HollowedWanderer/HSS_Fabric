package net.hollowed.hss.common.networking;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class ModKeyBindings {

    //Keybindings
    public static KeyBinding utilityMoveBinding;
    public static KeyBinding movementAbilityBinding;
    public static KeyBinding backSlotBinding;

    public static void registerKeyBindings() {
        utilityMoveBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.hss.utilityMove",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_LEFT_ALT,
                "category.hss.keybinds"
        ));
        movementAbilityBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.hss.movementAbility",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_X,
                "category.hss.keybinds"
        ));
        backSlotBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.hss.backSlot",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_R,
                "category.hss.keybinds"
        ));
    }

    public static void initialize() {
        registerKeyBindings();
    }
}
