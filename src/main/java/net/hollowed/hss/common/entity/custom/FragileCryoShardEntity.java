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

import java.util.Random;

public class FragileCryoShardEntity extends ItemProjectileEntity {

	public FragileCryoShardEntity(EntityType<? extends PersistentProjectileEntity> entityType, World world) {
		super(entityType, world, ModItems.CRYO_SHARD.getDefaultStack());
	}

	public FragileCryoShardEntity(World world, LivingEntity user, ItemStack stack) {
		super(ModEntities.FRAGILE_CRYO_SHARD, user, world, stack);
		this.stack = stack.copy();
	}

	@Override
	protected SoundEvent getHitSound() {
		return SoundEvents.INTENTIONALLY_EMPTY;
	}

	protected void onBlockHit(BlockHitResult blockHitResult) {
		super.onBlockHit(blockHitResult);
		CommandRunner.runCommandAsEntity(this, "particle hss:cryo_shard ~ ~0.4 ~ 0 0 0 0 3 force");
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
		CommandRunner.runCommandAsEntity(this, "particle hss:cryo_shard ~ ~0.4 ~ 0 0 0 0 3 force");
		this.setDamage(1);
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
			long l = (long)this.random.nextInt(i / 2 + 2);
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

	private CryoShardFlying soundInstance;

	@Override
	public void tick() {
		super.tick();

		// Check if the projectile is on fire and discard it if so
		if (this.isOnFire()) {
			stopSound();
			this.discard();
			return;
		}

		boolean bl = this.isNoClip();
		Vec3d vec3d = this.getVelocity();

		// Initialize yaw and pitch based on velocity
		if (this.prevPitch == 0.0F && this.prevYaw == 0.0F) {
			double d = vec3d.horizontalLength();
			this.setYaw((float)(MathHelper.atan2(vec3d.x, vec3d.z) * (180 / Math.PI)));
			this.setPitch((float)(MathHelper.atan2(vec3d.y, d) * (180 / Math.PI)));
			this.prevYaw = this.getYaw();
			this.prevPitch = this.getPitch();
		}

		BlockPos blockPos = this.getBlockPos();
		BlockState blockState = this.getWorld().getBlockState(blockPos);

		// Check if the projectile is colliding with a block
		if (!blockState.isAir() && !bl) {
			VoxelShape voxelShape = blockState.getCollisionShape(this.getWorld(), blockPos);
			if (!voxelShape.isEmpty()) {
				Vec3d vec3d2 = this.getPos();

				for (Box box : voxelShape.getBoundingBoxes()) {
					if (box.offset(blockPos).contains(vec3d2)) {
						this.inGround = true;
						stopSound();
						return;
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

		// Schedule the entity to be discarded after 400 ticks
		DelayHandler.schedule(this.getWorld(), 400, () -> {
			stopSound();
			this.discard();
		});
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
}