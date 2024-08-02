package net.hollowed.hss.common.networking;

import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.hollowed.hss.common.networking.packets.MeleePacket;
import net.hollowed.hss.common.networking.packets.GrabPacket;
import net.minecraft.util.ActionResult;

public class AttackEntityHandler {
    public static void register() {
        AttackEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
            if (world.isClient()) {
                GrabPacket.send();
                MeleePacket.send();
            }
            return ActionResult.PASS;
        });
    }
}
