package net.hollowed.hss.common.block.custom;

import net.hollowed.hss.common.block.ModBlockEntities;
import net.hollowed.hss.common.block.entities.ResonatingAltarBlockEntity;
import net.hollowed.hss.common.networking.DelayHandler;
import net.hollowed.hss.common.networking.packets.AltarPlaceParticlePacket;
import net.hollowed.hss.common.networking.packets.AltarUpdatePacket;
import net.hollowed.hss.common.networking.packets.DustParticlePacket;
import net.hollowed.hss.common.networking.packets.RingParticlePacket;
import net.hollowed.hss.common.sound.ModSounds;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.server.command.TitleCommand;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.HashMap;

public class ResonatingAltarBlock extends BlockWithEntity implements Waterloggable, BlockEntityProvider {
    private static final HashMap<BlockPos, Long> cooldowns = new HashMap<>();
    public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;
    public static final DirectionProperty FACING = HorizontalFacingBlock.FACING;
    public static final BooleanProperty HELD_ITEM = BooleanProperty.of("held_item");

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return checkType(type, ModBlockEntities.RESONATING_ALTAR_BLOCK_ENTITY, ResonatingAltarBlockEntity::tick);
    }

    public static final VoxelShape COLLISION_SHAPE = VoxelShapes.union(
            Block.createCuboidShape(3, 0, 5, 13, 8, 11),
            Block.createCuboidShape(0, 8, 3, 16, 10, 13),
            Block.createCuboidShape(2, 10, 5, 14, 12, 11));
    public static final VoxelShape COLLISION_SHAPE_ROTATED = VoxelShapes.union(
            Block.createCuboidShape(5, 0, 3, 11, 8, 13),
            Block.createCuboidShape(3, 8, 0, 13, 10, 16),
            Block.createCuboidShape(5, 10, 2, 11, 12, 14)
    );
    public static final VoxelShape NORTH_SHAPE = VoxelShapes.union(
            Block.createCuboidShape(3, 0, 5, 13, 8, 11),
            Block.createCuboidShape(0, 8, 3, 16, 10, 13),
            Block.createCuboidShape(2, 10, 5, 14, 12, 11),
            Block.createCuboidShape(3, 12, 6, 7, 15, 10),
            Block.createCuboidShape(9, 12, 6, 13, 15, 10),
            Block.createCuboidShape(4, 15, 7, 7, 24, 9),
            Block.createCuboidShape(9, 15, 7, 12, 24, 9));
    public static final VoxelShape EAST_SHAPE = VoxelShapes.union(
            Block.createCuboidShape(5, 0, 3, 11, 8, 13),
            Block.createCuboidShape(3, 8, 0, 13, 10, 16),
            Block.createCuboidShape(5, 10, 2, 11, 12, 14),
            Block.createCuboidShape(6, 12, 3, 10, 15, 7),
            Block.createCuboidShape(6, 12, 9, 10, 15, 13),
            Block.createCuboidShape(7, 15, 4, 9, 24, 7),
            Block.createCuboidShape(7, 15, 9, 9, 24, 12)
    );
    public static final VoxelShape SOUTH_SHAPE = VoxelShapes.union(
            Block.createCuboidShape(3, 0, 5, 13, 8, 11),
            Block.createCuboidShape(0, 8, 3, 16, 10, 13),
            Block.createCuboidShape(2, 10, 5, 14, 12, 11),
            Block.createCuboidShape(3, 12, 6, 7, 15, 10),
            Block.createCuboidShape(9, 12, 6, 13, 15, 10),
            Block.createCuboidShape(4, 15, 7, 7, 24, 9),
            Block.createCuboidShape(9, 15, 7, 12, 24, 9)
    );
    public static final VoxelShape WEST_SHAPE = VoxelShapes.union(
            Block.createCuboidShape(5, 0, 3, 11, 8, 13),
            Block.createCuboidShape(3, 8, 0, 13, 10, 16),
            Block.createCuboidShape(5, 10, 2, 11, 12, 14),
            Block.createCuboidShape(6, 12, 3, 10, 15, 7),
            Block.createCuboidShape(6, 12, 9, 10, 15, 13),
            Block.createCuboidShape(7, 15, 4, 9, 24, 7),
            Block.createCuboidShape(7, 15, 9, 9, 24, 12)
    );

    public ResonatingAltarBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(FACING, Direction.NORTH).with(WATERLOGGED, false).with(HELD_ITEM, false));
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        FluidState fluidState = ctx.getWorld().getFluidState(ctx.getBlockPos());
        boolean waterlogged = fluidState.getFluid() == Fluids.WATER;
        return this.getDefaultState()
                .with(FACING, ctx.getHorizontalPlayerFacing().getOpposite())
                .with(WATERLOGGED, waterlogged)
                .with(HELD_ITEM, false);
    }


    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return switch (state.get(FACING)) {
            case EAST, WEST -> COLLISION_SHAPE_ROTATED;
            default -> COLLISION_SHAPE;
        };
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean notify) {
        if (pos.down().equals(fromPos)) {
            BlockState belowState = world.getBlockState(pos.down());
            if (!belowState.isFullCube(world, pos.down())) {
                world.breakBlock(pos, true);
            }
        }
        super.neighborUpdate(state, world, pos, block, fromPos, notify);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return switch (state.get(FACING)) {
            case SOUTH -> SOUTH_SHAPE;
            case EAST -> EAST_SHAPE;
            case WEST -> WEST_SHAPE;
            default -> NORTH_SHAPE;
        };
    }

    @Override
    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return state.with(FACING, rotation.rotate(state.get(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.rotate(mirror.getRotation(state.get(FACING)));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING, WATERLOGGED, HELD_ITEM);
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        BlockPos belowPos = pos.down();
        BlockState belowState = world.getBlockState(belowPos);
        return belowState.isFullCube(world, belowPos);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (state.get(WATERLOGGED)) {
            world.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
        }

        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new ResonatingAltarBlockEntity(pos, state);
    }

    @Override
    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        BlockEntity entity = world.getBlockEntity(pos);
        if (entity instanceof ResonatingAltarBlockEntity) {
            // Stop the laser sound
            ((ResonatingAltarBlockEntity) entity).stopLaserSound();

            // Stop the shake sound
            ((ResonatingAltarBlockEntity) entity).stopShakeSound();
        }

        // Call the superclass method to handle any other necessary cleanup
        super.onBreak(world, pos, state, player);
    }

    @Override
    public @NotNull ActionResult onUse(@NotNull BlockState state, World world, @NotNull BlockPos pos, @NotNull PlayerEntity player, @NotNull Hand hand, @NotNull BlockHitResult hit) {
        // Cooldown duration in milliseconds (15 seconds)
        long cooldownDuration = 20 * 1000;

        // Check if the block is on cooldown
        long currentTime = System.currentTimeMillis();
        if (cooldowns.containsKey(pos)) {
            long lastUseTime = cooldowns.get(pos);
            if (currentTime - lastUseTime < cooldownDuration) {
                Text cooldownMessage = Text.literal("Altar is on cooldown...")
                        .formatted(Formatting.DARK_PURPLE, Formatting.ITALIC, Formatting.BOLD);
                player.sendMessage(cooldownMessage, true);
                return ActionResult.FAIL;
            }
        }

        // Continue with the normal onUse logic
        BlockEntity entity = world.getBlockEntity(pos);
        if (entity instanceof ResonatingAltarBlockEntity altarEntity) {
            ItemStack currentAltarItem = altarEntity.getStack(0);
            ItemStack handItem = player.getStackInHand(hand);

            if (!handItem.isEmpty() && handItem.getCount() > 1 && !currentAltarItem.isEmpty()) {
                return ActionResult.FAIL;
            }

            boolean itemChanged = false;

            // Place new item on altar
            if (!handItem.isEmpty() && altarEntity.getStack(0).isEmpty()) {
                ItemStack newItem = handItem.copy();
                newItem.setCount(1);
                altarEntity.setStack(0, newItem, player);
                handItem.decrement(1);
                itemChanged = true;
            }

            if (itemChanged && !world.isClient) {
                altarEntity.markDirty();
                playSound(world, pos, ModSounds.ALTAR_PLACE_ITEM, 1f);
                playSound(world, pos, SoundEvents.BLOCK_LODESTONE_PLACE, 1f);
                playSound(world, pos, SoundEvents.BLOCK_AMETHYST_BLOCK_RESONATE, 1.7f);
                ((ServerWorld) world).getChunkManager().markForUpdate(pos);
                world.setBlockState(pos, state.with(ResonatingAltarBlock.HELD_ITEM, !altarEntity.getStack(0).isEmpty()), Block.NOTIFY_ALL);
                world.updateNeighborsAlways(pos, state.getBlock());

                // Send update packet to clients
                AltarUpdatePacket.sendUpdatePacket((ServerWorld) world, pos, altarEntity.getStack(0));
                Vec3d position = new Vec3d(pos.getX() + 0.5, pos.getY() + 2.7, pos.getZ() + 0.5);
                AltarPlaceParticlePacket.send(position, world, true);

                // Record the time when the block was last used
                cooldowns.put(pos, currentTime);
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
            if (blockEntity instanceof ResonatingAltarBlockEntity) {
                ((ResonatingAltarBlockEntity) blockEntity).drops();
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
        if (blockEntity instanceof ResonatingAltarBlockEntity) {
            ItemStack stack = ((ResonatingAltarBlockEntity) blockEntity).getStack(0);
            return !stack.isEmpty() ? 1 : 0;
        }
        return 0;
    }
}
