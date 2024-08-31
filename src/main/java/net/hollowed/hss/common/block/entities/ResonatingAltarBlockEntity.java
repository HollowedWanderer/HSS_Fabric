package net.hollowed.hss.common.block.entities;

import net.hollowed.hss.common.block.ModBlockEntities;
import net.hollowed.hss.common.item.custom.CharmItem;
import net.hollowed.hss.common.networking.DelayHandler;
import net.hollowed.hss.common.networking.packets.*;
import net.hollowed.hss.common.sound.ModSounds;
import net.hollowed.hss.common.sound.ThaumiteLaser;
import net.hollowed.hss.common.sound.ThaumiteScreenShake;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import static net.hollowed.hss.ModComponents.BOUND;

public class ResonatingAltarBlockEntity extends BlockEntity implements ImplementedInventory {

    private final DefaultedList<ItemStack> items = DefaultedList.ofSize(1, ItemStack.EMPTY);

    public static final float ROTATION_INCREMENT = 0.0000007f;
    public static final float HEIGHT_INCREMENT = 0.001f;

    public float rotationSpeed = 0.05F; // Initial rotation speed
    public float yOffset = 0F; // Initial vertical offset

    public ResonatingAltarBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.RESONATING_ALTAR_BLOCK_ENTITY, pos, state);
    }

    public void drops() {
        if (world != null) {
            for (ItemStack stack : items) {
                if (!stack.isEmpty()) {
                    ItemEntity itemEntity = new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), stack);
                    world.spawnEntity(itemEntity);
                }
            }
        }
    }

    @Override
    public void markDirty() {
        super.markDirty();
        if (world != null) {
            world.updateListeners(pos, getCachedState(), getCachedState(), Block.NOTIFY_ALL);
        }
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        Inventories.writeNbt(nbt, items);
        super.writeNbt(nbt);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        Inventories.readNbt(nbt, items);
    }

    private void playSound(World world, BlockPos pos, SoundEvent event, float pitch) {
        // Play a placeholder sound with adjustable pitch
        world.playSound(null, pos, event, SoundCategory.BLOCKS, 1.5F, pitch);
    }

    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    public static void tick(World world, BlockPos pos, BlockState state, ResonatingAltarBlockEntity blockEntity) {
        if (world.isClient()) {
            return;
        }

        if (!blockEntity.getStack(0).isEmpty()) {
            blockEntity.yOffset += HEIGHT_INCREMENT; // Update the yOffset
            blockEntity.rotationSpeed += ROTATION_INCREMENT;

            double radius = 0.5; // Distance from the center
            double angle = world.getTime() * 0.1; // Adjustable rotation speed

            // Calculate pos1 and pos2 based on the current angle and radius
            double posX1 = pos.getX() + 0.5 + radius * Math.cos(angle);
            double posZ1 = pos.getZ() + 0.5 + radius * Math.sin(angle);
            double posX2 = pos.getX() + 0.5 + radius * Math.cos(angle + Math.PI);
            double posZ2 = pos.getZ() + 0.5 + radius * Math.sin(angle + Math.PI);

            Vec3d pos1 = new Vec3d(posX1, pos.getY() + 2.5 + (blockEntity.yOffset * 8.5), posZ1);
            Vec3d pos2 = new Vec3d(posX2, pos.getY() + 2.5 + (blockEntity.yOffset * 8.5), posZ2);

            AltarOrbitParticlePacket.send(world, pos1, pos2);
        } else {
            blockEntity.yOffset = 0.0F; // Reset yOffset if empty
            blockEntity.rotationSpeed = 0.5F;
            blockEntity.stopLaserSound();
            blockEntity.stopShakeSound();
        }

        if (blockEntity.yOffset >= 0.05 && blockEntity.yOffset < 0.052) {
            for (int i = 0; i < 16; i++) {
                DelayHandler.schedule(world, i * 2, () -> AltarLaserParticlePacket.send(new Vec3d(pos.getX() + 0.5, pos.getY() + 0.8, pos.getZ() + 0.5), world));
            }
        }

        if (blockEntity.yOffset >= 0.12 && blockEntity.yOffset < 0.121) {
            AltarPlaceParticlePacket.send(new Vec3d(pos.getX() + 0.5, pos.getY() + 2.5 + (blockEntity.yOffset * 8.5), pos.getZ() + 0.5), world, false);
            blockEntity.playSound(world, pos, ModSounds.ALTAR_PLACE_ITEM, 0.8f);
        }
        if (blockEntity.yOffset >= 0.17 && blockEntity.yOffset < 0.171) {
            AltarPlaceParticlePacket.send(new Vec3d(pos.getX() + 0.5, pos.getY() + 2.5 + (blockEntity.yOffset * 8.5), pos.getZ() + 0.5), world, false);
            blockEntity.playSound(world, pos, ModSounds.ALTAR_PLACE_ITEM, 1f);
        }
        if (blockEntity.yOffset >= 0.21 && blockEntity.yOffset < 0.211) {
            AltarPlaceParticlePacket.send(new Vec3d(pos.getX() + 0.5, pos.getY() + 2.5 + (blockEntity.yOffset * 8.5), pos.getZ() + 0.5), world, false);
            blockEntity.playSound(world, pos, ModSounds.ALTAR_PLACE_ITEM, 1.3f);
        }

        if (blockEntity.yOffset >= 0.26 && blockEntity.yOffset < 0.261) {
            blockEntity.playSound(world, pos, ModSounds.THAUMITE_EXPLOSION, 1f);
            AltarChargeParticlePacket.send(new Vec3d(pos.getX() + 0.5, pos.getY() + 2.5 + (blockEntity.yOffset * 8.5), pos.getZ() + 0.5), world);
        }

        if (blockEntity.yOffset >= 0.28F) {
            blockEntity.spawnItemEntity(world, pos);
            blockEntity.yOffset = 0.0F; // Reset the yOffset after spawning the entity
            blockEntity.playSound(world, pos, SoundEvents.ENTITY_WARDEN_SONIC_BOOM, 0.7f);
            blockEntity.stopLaserSound();
            blockEntity.stopShakeSound();
        }

        markDirty(world, pos, state);
    }

    private void spawnItemEntity(World world, BlockPos pos) {
        if (!items.get(0).isEmpty()) {
            // Spawn the item entity 4 blocks above the block entity's position
            BlockPos spawnPos = pos.up(4); // 4 blocks above the current position
            ItemEntity itemEntity = new ItemEntity(world, spawnPos.getX() + 0.5, spawnPos.getY(), spawnPos.getZ() + 0.5, items.get(0).copy());
            if (getStack(0).getItem() instanceof CharmItem) {
                world.spawnEntity(itemEntity);
            }
            items.set(0, ItemStack.EMPTY); // Clear the item from the slot
            AltarUpdatePacket.sendUpdatePacket((ServerWorld) world, pos, getStack(0));
            AltarExplodeParticlePacket.send(world, new Vec3d(pos.getX() + 0.5, pos.getY() + 2.3 + yOffset * 8.5, pos.getZ() + 0.5));
            markDirty(); // Ensure the block entity is marked dirty after clearing the stack

            // Knockback radius and strength
            double radius = 10.0; // Radius in blocks
            double maxStrength = 5; // Maximum knockback strength

            // Get players within the radius and apply knockback
            for (LivingEntity entity : world.getEntitiesByClass(LivingEntity.class, new net.minecraft.util.math.Box(pos).expand(radius), e -> true)) {
                double distance = entity.getPos().distanceTo(Vec3d.ofCenter(new Vec3i(pos.getX(), pos.getY() + 2, pos.getZ())));
                if (distance <= radius) {
                    // Calculate knockback strength based on distance
                    double strength = maxStrength * (1 - (distance / radius));

                    // Calculate the direction from the block to the player
                    Vec3d direction = entity.getPos().subtract(Vec3d.ofCenter(pos)).normalize();

                    // Apply knockback to the player
                    entity.addVelocity(direction.x * strength, 0.5 * strength, direction.z * strength);
                    entity.velocityModified = true;
                }
            }

            // Update BOUND based on the item type
            PlayerEntity player = world.getClosestPlayer(pos.getX(), pos.getY(), pos.getZ(), 10, false); // Find the closest player within 10 blocks
            if (player != null) {
                ItemStack stack = itemEntity.getStack();
                if (stack.getItem() instanceof CharmItem) {
                    BOUND.get(player).setValue(((CharmItem) stack.getItem()).charmType);
                    Text cooldownMessage = Text.literal("Attuned to " + ((CharmItem) stack.getItem()).charmType + ".")
                            .formatted(Formatting.DARK_PURPLE, Formatting.ITALIC, Formatting.BOLD);
                    player.sendMessage(cooldownMessage, true);
                } else {
                    BOUND.get(player).setValue("");
                    Text cooldownMessage = Text.literal("The resonance ceases...")
                            .formatted(Formatting.DARK_PURPLE, Formatting.ITALIC, Formatting.BOLD);
                    player.sendMessage(cooldownMessage, true);
                }
            }
        }
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        return createNbt();
    }

    private ThaumiteLaser laserSoundInstance;

    private ThaumiteScreenShake shakeSoundInstance;

    @Override
    public DefaultedList<ItemStack> getItems() {
        return items;
    }

    public void setStack(int slot, ItemStack stack, PlayerEntity player) {
        items.set(slot, stack);
        markDirty(); // Mark the block entity as dirty to save the new state

        if (world != null && !world.isClient) {
            world.updateListeners(this.pos, this.getCachedState(), this.getCachedState(), Block.NOTIFY_ALL);

            // Reset rotation speed and Y offset
            rotationSpeed = 0.005F;
            yOffset = 0F;

            if (laserSoundInstance == null) {
                startLaserSound();
            } else {
                // Update the position of the sound instance
                laserSoundInstance.setX(this.getPos().getX());
                laserSoundInstance.setY(this.getPos().getY());
                laserSoundInstance.setZ(this.getPos().getZ());
            }

            if (shakeSoundInstance == null) {
                startShakeSound();
            } else {
                // Update the position of the sound instance
                shakeSoundInstance.setX(this.getPos().getX());
                shakeSoundInstance.setY(this.getPos().getY());
                shakeSoundInstance.setZ(this.getPos().getZ());
            }
        }
    }

    private void startLaserSound() {
        MinecraftClient client = MinecraftClient.getInstance();
        SoundManager soundManager = client.getSoundManager();

        // Ensure we're not modifying the sound list during iteration
        client.execute(() -> {
            ThaumiteLaser instance = new ThaumiteLaser(this);
            soundManager.play(instance);
            laserSoundInstance = instance;
        });
    }

    public void stopLaserSound() {
        if (laserSoundInstance != null) {
            MinecraftClient.getInstance().execute(() -> {
                laserSoundInstance.done();
                laserSoundInstance = null;
            });
        }
    }

    private void startShakeSound() {
        MinecraftClient client = MinecraftClient.getInstance();
        SoundManager soundManager = client.getSoundManager();

        client.execute(() -> {
            ThaumiteScreenShake instance = new ThaumiteScreenShake(this);
            soundManager.play(instance);
            shakeSoundInstance = instance;
        });
    }

    public void stopShakeSound() {
        if (shakeSoundInstance != null) {
            MinecraftClient.getInstance().execute(() -> {
                shakeSoundInstance.done();
                shakeSoundInstance = null;
            });
        }
    }


    public void removeStack(int slot, ItemStack stack) {
        items.set(slot, stack);
        markDirty(); // Mark the block entity as dirty to save the new state
        if (world != null && !world.isClient) {
            world.updateListeners(this.pos, this.getCachedState(), this.getCachedState(), Block.NOTIFY_ALL);
            rotationSpeed = 0.005F;
            yOffset = 0F;
        }
    }
}
