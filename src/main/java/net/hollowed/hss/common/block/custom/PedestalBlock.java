package net.hollowed.hss.common.block.custom;

import net.hollowed.hss.common.block.ModBlockEntities;
import net.hollowed.hss.common.block.entities.PedestalBlockEntity;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.ai.control.MoveControl;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.stream.Stream;

public class PedestalBlock extends Block implements BlockEntityProvider {

    public static final VoxelShape SHAPE = Stream.of(
            Block.createCuboidShape(1, 0, 1, 15, 3, 15),
            Block.createCuboidShape(3, 3, 3, 13, 13, 13),
            Block.createCuboidShape(1, 13, 1, 15, 16, 15)
    ).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, BooleanBiFunction.OR)).get();

    public PedestalBlock(Settings settings) {
        super(settings);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new PedestalBlockEntity(pos, state);
    }

    @Override
    public @NotNull ActionResult onUse(@NotNull BlockState state, World world, @NotNull BlockPos pos, @NotNull PlayerEntity player, @NotNull Hand hand, @NotNull BlockHitResult hit) {
        if (world.isClient) {
            world.updateListeners(pos, state, state, Block.NOTIFY_ALL);
            return ActionResult.SUCCESS;
        }

        BlockEntity entity = world.getBlockEntity(pos);
        if (entity instanceof PedestalBlockEntity pedestalEntity) {
            ItemStack currentPedestalItem = pedestalEntity.getStack(0);
            ItemStack handItem = player.getStackInHand(hand);

            // Remove current item from pedestal
            ItemStack playerItem = currentPedestalItem.copy();
            if (handItem.isEmpty() || handItem.getCount() == 1) {
                player.setStackInHand(hand, playerItem);
            } else {
                dropItem(playerItem, player);
            }
            pedestalEntity.setStack(0, ItemStack.EMPTY);

            // Place new item on pedestal
            currentPedestalItem = handItem.copy();
            if (!currentPedestalItem.isEmpty()) {
                currentPedestalItem.setCount(1);
                pedestalEntity.setStack(0, currentPedestalItem);
                handItem.decrement(1);
            }

            world.updateListeners(pos, state, state, Block.NOTIFY_ALL);
        }
        world.updateListeners(pos, state, state, Block.NOTIFY_ALL);
        return ActionResult.SUCCESS;
    }

    private void dropItem(ItemStack itemstack, PlayerEntity owner) {
        if (owner instanceof ServerPlayerEntity serverPlayer) {
            ItemEntity itementity = serverPlayer.dropItem(itemstack, false);
            if (itementity != null) {
                itementity.setPickupDelay(0);
                itementity.setOwner(serverPlayer.getUuid());
            }
        }
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (state.getBlock() != newState.getBlock()) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof PedestalBlockEntity) {
                ((PedestalBlockEntity) blockEntity).drops();
            }
        }
        super.onStateReplaced(state, world, pos, newState, moved);
    }
}
