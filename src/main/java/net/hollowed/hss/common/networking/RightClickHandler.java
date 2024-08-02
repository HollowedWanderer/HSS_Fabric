package net.hollowed.hss.common.networking;

import net.hollowed.hss.common.networking.packets.AttackPacket;
import net.hollowed.hss.common.networking.packets.GroundAttackPacket;
import net.hollowed.hss.common.networking.packets.HeavyAttackPacket;
import net.hollowed.hss.common.networking.packets.HeavyGroundAttackPacket;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class RightClickHandler {
    public static final MinecraftClient client = MinecraftClient.getInstance();
    private static boolean keyProcessed = false;
    private static long lastItemUseTime = 0;
    private static final long COOLDOWN_PERIOD = 200;

    public static void checkRightClickOnTopFace() {
        if (client.world == null || client.player == null) {
            return;
        }

        long currentTime = System.currentTimeMillis();
        if (client.options.useKey.isPressed() && client.player.getMainHandStack() != ItemStack.EMPTY) {
            lastItemUseTime = System.currentTimeMillis();
        }

        if (client.options.useKey.isPressed() && !client.player.isUsingItem() && (currentTime - lastItemUseTime) > COOLDOWN_PERIOD) {
            HitResult hitResult = client.crosshairTarget;

            assert hitResult != null;
            if (hitResult.getType() == HitResult.Type.BLOCK) {
                BlockHitResult blockHitResult = (BlockHitResult) hitResult;
                Direction face = blockHitResult.getSide();

                if (face == Direction.UP && isInteractingWithUsableBlock(blockHitResult)) {
                    GroundAttackPacket.send();
                    HeavyGroundAttackPacket.send();
                } else if (face != Direction.UP && isInteractingWithUsableBlock(blockHitResult)) {
                    AttackPacket.send();
                    HeavyAttackPacket.send();
                }
            }
        }
    }

    public static void checkRightClickInAir() {
        if (client.world == null || client.player == null) {
            return;
        }

        long currentTime = System.currentTimeMillis();

        if (client.options.useKey.isPressed() && client.player.getMainHandStack() != ItemStack.EMPTY) {
            lastItemUseTime = System.currentTimeMillis();
        }

        if (client.options.useKey.isPressed() && !keyProcessed && !client.player.isUsingItem() && (currentTime - lastItemUseTime) > COOLDOWN_PERIOD) {
            HitResult hitResult = client.crosshairTarget;

            if (hitResult == null || hitResult.getType() == HitResult.Type.MISS) {
                AttackPacket.send();
                HeavyAttackPacket.send();
                keyProcessed = true;
            }
        } else if (!client.options.useKey.isPressed()) {
            keyProcessed = false;
        }
    }

    public static boolean isInteractingWithUsableBlock(BlockHitResult blockHitResult) {
        BlockPos blockPos = blockHitResult.getBlockPos();
        assert client.world != null;
        BlockState blockState = client.world.getBlockState(blockPos);
        ClientPlayerEntity player = client.player;

        if (blockState.hasBlockEntity()) {
            return false;
        }

        ActionResult result = blockState.onUse(client.world, player, Hand.MAIN_HAND, blockHitResult);

        return result == ActionResult.PASS;
    }
}
