package net.hollowed.hss.common.entity.custom;

import net.hollowed.hss.common.entity.ModEntities;
import net.hollowed.hss.common.item.ModItems;
import net.hollowed.hss.common.networking.DelayHandler;
import net.hollowed.hss.common.sound.CryoShardFlying;
import net.hollowed.hss.common.util.CommandRunner;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.GameStateChangeS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Random;

public class SpiralFragileCryoShardEntity extends ItemProjectileEntity {

	public SpiralFragileCryoShardEntity(EntityType<? extends PersistentProjectileEntity> entityType, World world) {
		super(entityType, world, ModItems.CRYO_SHARD.getDefaultStack());
	}

	public SpiralFragileCryoShardEntity(World world, LivingEntity user, ItemStack stack) {
		super(ModEntities.SPIRAL_FRAGILE_CRYO_SHARD, user, world, stack);
		this.stack = stack.copy();
	}

	@Override
	protected SoundEvent getHitSound() {
		return SoundEvents.INTENTIONALLY_EMPTY;
	}

	protected void onBlockHit(BlockHitResult blockHitResult) {
		super.onBlockHit(blockHitResult);
		CommandRunner.runCommandAsEntity(this, "particle hss:cryo_shard ~ ~0.4 ~ 0 0 0 0 3 normal");
		this.getWorld().playSound(
				null,
				this.getX(),
				this.getY(),
				this.getZ(),
				SoundEvents.BLOCK_GLASS_BREAK,
				SoundCategory.PLAYERS,
				0.5F,
				1F / (this.getWorld().getRandom().nextFloat() * 0.4F + 0.8F)
		);
		this.discard();
	}

	protected void onEntityHit(EntityHitResult entityHitResult) {
		super.onEntityHit(entityHitResult);
		CommandRunner.runCommandAsEntity(this, "particle hss:cryo_shard ~ ~0.4 ~ 0 0 0 0 3 normal");
		this.setDamage(7);
		Entity entity = entityHitResult.getEntity();
		if (entity instanceof PlayerEntity && !((PlayerEntity) entity).isBlocking() && !((PlayerEntity) entity).isCreative()) {
			entity.setFrozenTicks(300);
		} else if (!(entity instanceof PlayerEntity)) {
			entity.setFrozenTicks(300);
		}
		this.getWorld().playSound(
				null,
				this.getX(),
				this.getY(),
				this.getZ(),
				SoundEvents.BLOCK_GLASS_BREAK,
				SoundCategory.PLAYERS,
				0.5F,
				1.1F / (this.getWorld().getRandom().nextFloat() * 0.4F + 0.8F)
		);
		this.getWorld().playSound(this, this.getBlockPos(), SoundEvents.BLOCK_GLASS_BREAK, SoundCategory.BLOCKS, 0.5F, 1.6F / (this.getWorld().getRandom().nextFloat() * 0.4F + 0.8F));
		float f = (float)this.getVelocity().length();
		int i = MathHelper.ceil(MathHelper.clamp((double)f * this.getDamage(), 0.0, 2.147483647E9));

		if (this.isCritical()) {
			long l = this.random.nextInt(i / 2 + 2);
			i = (int)Math.min(l + (long)i, 2147483647L);
		}

		Entity entity2 = this.getOwner();
		DamageSource damageSource;
		if (entity2 == null) {
			damageSource = this.getDamageSources().trident(this, this);
		} else {
			damageSource = this.getDamageSources().trident(this, entity2);
			if (entity2 instanceof LivingEntity) {
				((LivingEntity)entity2).onAttacking(entity);
			}
		}

		boolean bl = entity.getType() == EntityType.ENDERMAN;
		int j = entity.getFireTicks();
		if (this.isOnFire() && !bl) {
			entity.setOnFireFor(5);
		}

		if (entity.damage(damageSource, (float)i)) {
			if (bl) {
				return;
			}

			if (entity instanceof LivingEntity livingEntity) {
                if (!this.getWorld().isClient && this.getPierceLevel() <= 0) {
					livingEntity.setStuckArrowCount(livingEntity.getStuckArrowCount() + 1);
				}

				if (this.getPunch() > 0) {
					double d = Math.max(0.0, 1.0 - livingEntity.getAttributeValue(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE));
					Vec3d vec3d = this.getVelocity().multiply(1.0, 0.0, 1.0).normalize().multiply((double) this.getPunch() * 0.6 * d);
					if (vec3d.lengthSquared() > 0.0) {
						livingEntity.addVelocity(vec3d.x, 0.1, vec3d.z);
					}
				}

				if (!this.getWorld().isClient && entity2 instanceof LivingEntity) {
					EnchantmentHelper.onUserDamaged(livingEntity, entity2);
					EnchantmentHelper.onTargetDamaged((LivingEntity)entity2, livingEntity);
				}

				this.onHit(livingEntity);
				if (livingEntity != entity2 && livingEntity instanceof PlayerEntity && entity2 instanceof ServerPlayerEntity && !this.isSilent()) {
					((ServerPlayerEntity)entity2).networkHandler.sendPacket(new GameStateChangeS2CPacket(GameStateChangeS2CPacket.PROJECTILE_HIT_PLAYER, 0.0F));
				}
			}

			this.playSound(this.getSound(), 1.0F, 1.2F / (this.random.nextFloat() * 0.2F + 0.9F));
			if (this.getPierceLevel() <= 0) {
				this.discard();
			}
		} else {
			entity.setFireTicks(j);
			this.setVelocity(this.getVelocity().multiply(-0.1));
			this.setYaw(this.getYaw() + 180.0F);
			this.prevYaw += 180.0F;
			if (!this.getWorld().isClient && this.getVelocity().lengthSquared() < 1.0E-7) {
				if (this.pickupType == PickupPermission.ALLOWED) {
					this.dropStack(this.asItemStack(), 0.1F);
				}

				this.discard();
			}
		}
	}

	public void tick() {
		// Schedule the projectile to be discarded after a certain time
		DelayHandler.schedule(this.getWorld(), 300, this::discard);
		if (new Random().nextInt(25) > 23) {
			CommandRunner.runCommandAsEntity(this, "particle minecraft:snowflake ~ ~ ~ 0 0 0 0.01 2 force");
		}
		super.tick();

		// Discard the projectile if it's on fire
		if (this.isOnFire()) {
			this.discard();
		}

		// Get the owner entity (player)
		Entity owner = this.getOwner();
		if (owner != null) {
			// Define the orbit radius and minimum distance
			final Vec3d desiredPosition = getVec3d(owner);

			// Stabilize the vertical position with a smoother transition and random motion
			double stabilizedY = MathHelper.clamp(this.getY(), owner.getEyeY() - 0.5, owner.getEyeY() + 0.5);
			stabilizedY += (this.random.nextGaussian() * 0.1);

			// Calculate the velocity to move toward the interpolated position with easing
			Vec3d currentPosition = this.getPos();
			Vec3d direction = desiredPosition.subtract(currentPosition).normalize();
			double velocityMagnitude = 0.05; // Reduced magnitude for smoother motion
			Vec3d interpolatedVelocity = direction.multiply(velocityMagnitude).lerp(this.getVelocity(), 0.5); // Interpolation for smoothness

			// Add gentle random motion for a natural, chaotic effect
			interpolatedVelocity = interpolatedVelocity.add(
					(this.random.nextGaussian() * 0.075),
					(this.random.nextGaussian() * 0.04),
					(this.random.nextGaussian() * 0.075)
			);

			// Set the projectile's velocity and position smoothly
			this.setVelocity(interpolatedVelocity);
			this.setPosition(desiredPosition.x, stabilizedY, desiredPosition.z);

			// Calculate the yaw and pitch based on the new velocity vector
			double horizontalMagnitude = Math.sqrt(interpolatedVelocity.x * interpolatedVelocity.x + interpolatedVelocity.z * interpolatedVelocity.z);
			float targetYaw = (float) (Math.atan2(-interpolatedVelocity.z, interpolatedVelocity.x) * (180F / Math.PI)) + 90F;
			float targetPitch = (float) (Math.atan2(interpolatedVelocity.y, horizontalMagnitude) * (180F / Math.PI));

			// Apply the calculated yaw and pitch directly
			this.setYaw(MathHelper.lerp(0.2F, this.getYaw(), targetYaw)); // Smooth transition for yaw
			this.setPitch(MathHelper.lerp(0.2F, this.getPitch(), targetPitch)); // Smooth transition for pitch
		}

		// Check for collision with entities
		Box hitBox = this.getBoundingBox().expand(0.3);
		List<Entity> collidedEntities = this.getWorld().getOtherEntities(this, hitBox, entity -> !entity.isSpectator() && entity.isCollidable());

		if (!collidedEntities.isEmpty()) {
			for (Entity entity : collidedEntities) {
				this.onEntityHit(entity);
			}
			this.discard();
		}

		// Additional logic remains the same
		boolean bl = this.isNoClip();
		Vec3d vec3d = this.getVelocity();
		if (this.prevPitch == 0.0F && this.prevYaw == 0.0F) {
			double d = vec3d.horizontalLength();
			this.setYaw((float) (MathHelper.atan2(vec3d.x, vec3d.z) * 57.2957763671875));
			this.setPitch((float) (MathHelper.atan2(vec3d.y, d) * 57.2957763671875));
			this.prevYaw = this.getYaw();
			this.prevPitch = this.getPitch();
		}

		BlockPos blockPos = this.getBlockPos();
		BlockState blockState = this.getWorld().getBlockState(blockPos);
		Vec3d vec3d2;
		if (!blockState.isAir() && !bl) {
			VoxelShape voxelShape = blockState.getCollisionShape(this.getWorld(), blockPos);
			if (!voxelShape.isEmpty()) {
				vec3d2 = this.getPos();
				for (Box box : voxelShape.getBoundingBoxes()) {
					if (box.offset(blockPos).contains(vec3d2)) {
						this.inGround = true;
						break;
					}
				}
			}
		}

		// Manage sound instance
		if (this.getWorld().isClient()) {
			if (soundInstance == null) {
				startSound();
			} else {
				// Update the position of the sound instance
				soundInstance.setX(this.getX());
				soundInstance.setY(this.getY());
				soundInstance.setZ(this.getZ());
			}
		}
	}

	private void startSound() {
		soundInstance = new CryoShardFlying(this);
		SoundManager soundManager = MinecraftClient.getInstance().getSoundManager();
		if (new Random().nextInt(50) > 44) {
			soundManager.play(soundInstance);
		}
	}

	private void stopSound() {
		if (soundInstance != null) {
			soundInstance.done(); // Mark sound as done
			soundInstance = null;
		}
	}

	private CryoShardFlying soundInstance;

	private @NotNull Vec3d getVec3d(Entity owner) {
		double orbitRadius = 2;

		// Orbit speed (angular velocity)
		double orbitSpeed = 0.15F;

		// Use world time to calculate a consistent angle
		double angle = (this.age + this.getWorld().getTime()) * orbitSpeed;

		// Get the current owner position
		Vec3d ownerPos = owner.getPos();

		// Calculate the new orbit position using the angle
		double offsetX = Math.cos(angle) * orbitRadius;
		double offsetZ = Math.sin(angle) * orbitRadius;
		return new Vec3d(ownerPos.x + offsetX, owner.getEyeY(), ownerPos.z + offsetZ);
	}

	// Method to handle entity hit logic
	protected void onEntityHit(Entity entity) {
		entity.damage(this.getDamageSources().trident(this, this), 9.0F);
		entity.setFrozenTicks(300);
	}
}