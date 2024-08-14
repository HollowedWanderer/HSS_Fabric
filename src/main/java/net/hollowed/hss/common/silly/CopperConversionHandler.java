package net.hollowed.hss.common.silly;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.registry.Registries;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class CopperConversionHandler {

    private final Random random = Random.create();
    private final List<Block> allBlocks = new ArrayList<>();

    public CopperConversionHandler() {
        // Initialize the list of all blocks
        Registries.BLOCK.forEach(allBlocks::add);

        // Register event listener
        ServerTickEvents.START_SERVER_TICK.register(server -> {
            for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
                convertBlocksAroundPlayer(player);
            }
        });
    }

    private void convertBlocksAroundPlayer(ServerPlayerEntity player) {
        World world = player.getWorld();
        BlockPos playerPos = player.getBlockPos();

        // 3-block radius loop
        BlockPos.iterate(playerPos.add(-9, -9, -9), playerPos.add(9, 9, 9)).forEach(pos -> {
            BlockState blockState = world.getBlockState(pos);
            if (!blockState.isAir()) {
                // Select a random block from the list
                Block randomBlock = getRandomBlock();
                world.setBlockState(pos, randomBlock.getDefaultState());
            }
        });
    }

    private Block getRandomBlock() {
        if (allBlocks.isEmpty()) {
            return Blocks.AIR; // Fallback in case no blocks are found
        }
        return allBlocks.get(random.nextInt(allBlocks.size()));
    }
}
