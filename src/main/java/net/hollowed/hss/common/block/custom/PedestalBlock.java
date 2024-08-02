package net.hollowed.hss.common.block.custom;

import net.hollowed.hss.common.networking.packets.DustParticlePacket;
import net.hollowed.hss.common.networking.packets.PedestalUpdatePacket;
import net.hollowed.hss.common.block.ModBlockEntities;
import net.hollowed.hss.common.block.entities.PedestalBlockEntity;
import net.hollowed.hss.common.networking.packets.RingParticlePacket;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.stream.Stream;

public class PedestalBlock extends BlockWithEntity implements BlockEntityProvider {

    public static final BooleanProperty HELD_ITEM = BooleanProperty.of("held_item");

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return checkType(type, ModBlockEntities.PEDESTAL_BLOCK_ENTITY, (world1, pos, state1, be) -> PedestalBlockEntity.tick(world1, pos, state1));
    }

    public static final VoxelShape SHAPE = Stream.of(
            Block.createCuboidShape(1, 0, 1, 15, 3, 15),
            Block.createCuboidShape(3, 3, 3, 13, 13, 13),
            Block.createCuboidShape(1, 13, 1, 15, 16, 15)
    ).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, BooleanBiFunction.OR)).get();

    public PedestalBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(HELD_ITEM, false));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(HELD_ITEM);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(HELD_ITEM, false);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
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
        BlockEntity entity = world.getBlockEntity(pos);
        if (entity instanceof PedestalBlockEntity pedestalEntity) {
            ItemStack currentPedestalItem = pedestalEntity.getStack(0);
            ItemStack handItem = player.getStackInHand(hand);

            if (!handItem.isEmpty() && handItem.getCount() > 1 && !currentPedestalItem.isEmpty()) {
                return ActionResult.FAIL;
            }

            boolean itemChanged = false;

            // Remove current item from pedestal
            if (!currentPedestalItem.isEmpty()) {
                player.setStackInHand(hand, currentPedestalItem);
                pedestalEntity.setStack(0, ItemStack.EMPTY);
                itemChanged = true;
            }

            // Place new item on pedestal
            if (!handItem.isEmpty()) {
                ItemStack newItem = handItem.copy();
                newItem.setCount(1);
                pedestalEntity.setStack(0, newItem);
                handItem.decrement(1);
                itemChanged = true;
            }

            if (itemChanged && !world.isClient) {
                pedestalEntity.markDirty();
                playSound(world, pos, SoundEvents.BLOCK_SUSPICIOUS_SAND_PLACE, 1f);
                playSound(world, pos, SoundEvents.BLOCK_LODESTONE_PLACE, 1f);
                ((ServerWorld) world).getChunkManager().markForUpdate(pos);
                world.setBlockState(pos, state.with(PedestalBlock.HELD_ITEM, !pedestalEntity.getStack(0).isEmpty()), Block.NOTIFY_ALL);
                world.updateNeighborsAlways(pos, state.getBlock());

                // Send update packet to clients
                PedestalUpdatePacket.sendUpdatePacket((ServerWorld) world, pos, pedestalEntity.getStack(0));
                //if ("HollowedWanderer".equals(player.getName().getString())) {
                    Vec3d position = new Vec3d(pos.getX() + 0.5, pos.getY() + 1.3, pos.getZ() + 0.5);
                    for (int i = 0; i < 6; i++) {
                        DustParticlePacket.send(position, world);
                    }
                RingParticlePacket.send(position, world);
                //}
            }

            return ActionResult.SUCCESS;
        }

        return ActionResult.PASS;
    }


    private void playSound(World world, BlockPos pos, SoundEvent event, float pitch) {
        // Play a placeholder sound with adjustable pitch
        world.playSound(null, pos, event, SoundCategory.BLOCKS, 0.75F, pitch);
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

    @Override
    public boolean hasComparatorOutput(BlockState state) {
        return true;
    }

    @Override
    public int getComparatorOutput(BlockState state, World world, BlockPos pos) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof PedestalBlockEntity) {
            ItemStack stack = ((PedestalBlockEntity) blockEntity).getStack(0);
            return !stack.isEmpty() ? 1 : 0;
        }
        return 0;
    }

    private void dropItem(ItemStack itemStack, PlayerEntity owner) {
        if (owner instanceof ServerPlayerEntity serverPlayer) {
            ItemEntity itemEntity = serverPlayer.dropItem(itemStack, false);
            if (itemEntity != null) {
                itemEntity.setPickupDelay(0);
                itemEntity.setOwner(serverPlayer.getUuid());
            }
        }
    }
}
