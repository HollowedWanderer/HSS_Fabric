package net.hollowed.hss.common.item.custom;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.jamieswhiteshirt.reachentityattributes.ReachEntityAttributes;
import net.hollowed.hss.HollowedsSwordsSorcery;
import net.hollowed.hss.common.entity.custom.FragileCryoShardEntity;
import net.hollowed.hss.common.entity.custom.SpiralFragileCryoShardEntity;
import net.hollowed.hss.common.networking.DelayHandler;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.render.entity.feature.CreeperChargeFeatureRenderer;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.*;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class HollowedBladeItem extends SwordItem {
    private static final UUID REACH_UUID = UUID.fromString("d9e5dbe3-2b7e-4d9b-9f55-4bafab8c42d4");
    private static final double REACH_AMOUNT = 0.75;
    private static final double DAMAGE_RADIUS = 1.5;
    private static final int DAMAGE_AMOUNT = 8;

    public HollowedBladeItem(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Settings settings) {
        super(toolMaterial, attackDamage, attackSpeed, settings);
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        assert attacker instanceof PlayerEntity;
         if (((PlayerEntity) attacker).getItemCooldownManager().isCoolingDown(stack.getItem())) {
            stack.damage(100, ((PlayerEntity) attacker), (e) -> {
                e.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND);
            });
        }
        target.setFrozenTicks(300);
        return super.postHit(stack, target, attacker);
    }

    public void shatter(ItemStack stack, World world) {
        if (!world.isClient) {
            stack.getOrCreateNbt().putBoolean("Shattered", true);
        }
    }

    public void repair(ItemStack stack, World world) {
        if (!world.isClient) {
            stack.getOrCreateNbt().putBoolean("Shattered", false);
            stack.getOrCreateNbt().putBoolean("Dashing", false);
            stack.getOrCreateNbt().putBoolean("DashCheck", false);
        }
    }

    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        if (!((double) getPullProgress(this.getMaxUseTime(stack) - remainingUseTicks) <= 0.35) && user instanceof PlayerEntity) {
            if (!world.isClient && user.isSneaking() && !(EnchantmentHelper.getLevel(HollowedsSwordsSorcery.MAELSTROM, stack) > 0) && !(EnchantmentHelper.getLevel(HollowedsSwordsSorcery.FROZEN_GALE, stack) > 0)) {
                if (!((PlayerEntity) user).getItemCooldownManager().isCoolingDown(stack.getItem())) {
                    shatter(stack, user.getWorld());
                    ((PlayerEntity) user).getItemCooldownManager().set(stack.getItem(), 300);
                }

                // Number of cryo shards
                int numberOfProjectiles = 48;

                double goldenRatio = (1 + Math.sqrt(5)) / 2;
                double angleIncrement = Math.PI * 2 * goldenRatio;

                for (int i = 0; i < numberOfProjectiles; i++) {
                    double t = (double)i / (numberOfProjectiles - 1);
                    double inclination = Math.acos(1 - 2 * t); // Phi (Vertical angle)
                    double azimuth = angleIncrement * i; // Theta (Horizontal angle)

                    double xVelocity = Math.sin(inclination) * Math.cos(azimuth);
                    double yVelocity = Math.cos(inclination);
                    double zVelocity = Math.sin(inclination) * Math.sin(azimuth);

                    double speed = 1; // speed of cryo shards
                    xVelocity *= speed;
                    yVelocity *= speed;
                    zVelocity *= speed;

                    // Skip the projectile if it's within a small cone directly above
                    double minConeAngle = Math.toRadians(15); // Minimum angle from the top (in radians)
                    if (inclination < minConeAngle || inclination > (Math.PI - minConeAngle)) {
                        continue;
                    }

                    FragileCryoShardEntity entity = new FragileCryoShardEntity(world, user, stack.copyWithCount(1));

                    // Set the velocity with the calculated x, y, and z components
                    entity.setVelocity(xVelocity, yVelocity, zVelocity);
                    entity.pickupType = PersistentProjectileEntity.PickupPermission.CREATIVE_ONLY;

                    world.spawnEntity(entity);
                }

                world.playSoundFromEntity(null, user, SoundEvents.ENTITY_PLAYER_HURT_FREEZE, SoundCategory.NEUTRAL, 0.5F, 1F / (world.getRandom().nextFloat() * 0.4F + 0.8F));
                world.playSoundFromEntity(null, user, SoundEvents.BLOCK_GLASS_BREAK, SoundCategory.NEUTRAL, 0.5F, 0.7F / (world.getRandom().nextFloat() * 0.4F + 0.8F));
            } else if (!world.isClient && user.isSneaking() && EnchantmentHelper.getLevel(HollowedsSwordsSorcery.MAELSTROM, stack) > 0) {
                int additionalProjectiles = 48; // Number of additional projectiles to launch forward
                Random random = new Random();

                for (int i = 0; i < additionalProjectiles; i++) {
                    SpiralFragileCryoShardEntity forwardEntity = new SpiralFragileCryoShardEntity(world, user, stack.copyWithCount(1));

                    // Base direction for the projectiles (straight forward)
                    Vec3d direction = user.getRotationVec(1.0F);

                    // Add some randomness to the direction for the spread
                    double spread = 0.1; // Adjust spread factor as needed
                    double offsetX = (random.nextDouble() - 0.5) * spread;
                    double offsetY = (random.nextDouble() - 0.5) * spread;
                    double offsetZ = (random.nextDouble() - 0.5) * spread;

                    Vec3d velocity = direction.add(offsetX, offsetY, offsetZ).normalize().multiply(1.5); // Adjust speed as needed

                    forwardEntity.setVelocity(velocity);
                    forwardEntity.pickupType = PersistentProjectileEntity.PickupPermission.CREATIVE_ONLY;
                    DelayHandler.schedule(world, i * 2, () -> world.spawnEntity(forwardEntity));
                    user.setFrozenTicks(300);
                }

                shatter(stack, user.getWorld());
                ((PlayerEntity) user).getItemCooldownManager().set(stack.getItem(), 400);
                world.playSoundFromEntity(null, user, SoundEvents.ENTITY_PLAYER_HURT_FREEZE, SoundCategory.NEUTRAL, 0.5F, 1F / (world.getRandom().nextFloat() * 0.4F + 0.8F));
                world.playSoundFromEntity(null, user, SoundEvents.BLOCK_GLASS_BREAK, SoundCategory.NEUTRAL, 0.5F, 0.7F / (world.getRandom().nextFloat() * 0.4F + 0.8F));
            } else if (!world.isClient && user.isSneaking() && EnchantmentHelper.getLevel(HollowedsSwordsSorcery.FROZEN_GALE, stack) > 0) {
                Vec3d lookDirection = user.getRotationVec(1.0f).normalize();
                double dashSpeed = 2d;
                user.addVelocity(lookDirection.x * dashSpeed, lookDirection.y * dashSpeed, lookDirection.z * dashSpeed);
                user.velocityModified = true;
                ((PlayerEntity) user).useRiptide(20);
                world.playSoundFromEntity(null, user, SoundEvents.ENTITY_PLAYER_HURT_FREEZE, SoundCategory.NEUTRAL, 0.5F, 1F / (world.getRandom().nextFloat() * 0.4F + 0.8F));
                world.playSoundFromEntity(null, user, SoundEvents.ITEM_TRIDENT_RIPTIDE_3, SoundCategory.NEUTRAL, 0.5F, 0.9F);
                stack.getOrCreateNbt().putBoolean("Dashing", true);
                stack.getOrCreateNbt().putBoolean("DashCheck", true);
                DelayHandler.schedule(world, 15, () -> {
                    stack.getOrCreateNbt().putBoolean("Dashing", false);
                });
                ((PlayerEntity) user).getItemCooldownManager().set(this, 80);
            }
        }
    }

    public static float getPullProgress(int useTicks) {
        float f = (float)useTicks / 20.0F;
        f = (f * f + f * 2.0F) / 3.0F;
        if (f > 1.0F) {
            f = 1.0F;
        }

        return f;
    }

    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        if (user.isSneaking()) {
            world.playSoundFromEntity(null, user, SoundEvents.BLOCK_AMETHYST_BLOCK_RESONATE, SoundCategory.NEUTRAL, 0.5F, 0.7F);
            user.setCurrentHand(hand);
            return TypedActionResult.success(itemStack);
        }
        return TypedActionResult.fail(itemStack);
    }

    public int getMaxUseTime(ItemStack stack) {
        return 72000;
    }

    public UseAction getUseAction(ItemStack stack) {
        return UseAction.BOW;
    }

    @Override
    public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
        if (!world.isClient) {
            int useDuration = getMaxUseTime(stack) - remainingUseTicks;

            if (useDuration == 8) {
                world.playSoundFromEntity(null, user, SoundEvents.BLOCK_AMETHYST_BLOCK_RESONATE, SoundCategory.NEUTRAL, 0.5F, 1.1F);
            }
        }
        super.usageTick(world, user, stack, remainingUseTicks);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        assert entity instanceof PlayerEntity;
        if (!((PlayerEntity) entity).getItemCooldownManager().isCoolingDown(stack.getItem())) {
            repair(stack, world);
        } else {
            if (((PlayerEntity) entity).getItemCooldownManager().isCoolingDown(stack.getItem()) && ((PlayerEntity) entity).getMainHandStack() == stack) {
                assert stack.getNbt() != null;
                if (!stack.getNbt().getBoolean("DashCheck")) {
                    ((PlayerEntity) entity).addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 1, 0, true, false));
                }
            }
        }

        if (stack.hasNbt()) {
            assert stack.getNbt() != null;
            if (stack.getNbt().getBoolean("Dashing")) {
                applyAreaDamage((PlayerEntity) entity, world);
            }
        }
    }

    private void applyAreaDamage(PlayerEntity player, World world) {
        // Define the area around the player
        Box damageArea = new Box(
                player.getX() - DAMAGE_RADIUS, player.getY() - DAMAGE_RADIUS, player.getZ() - DAMAGE_RADIUS,
                player.getX() + DAMAGE_RADIUS, player.getY() + DAMAGE_RADIUS, player.getZ() + DAMAGE_RADIUS
        );

        // Find all entities within the area that aren't the player
        List<Entity> entities = world.getOtherEntities(player, damageArea,
                entity -> entity.isAlive() && !(entity instanceof PlayerEntity)
        );
        // Apply damage to each entity found
        for (Entity entity : entities) {
            entity.damage(entity.getDamageSources().trident(player, player), DAMAGE_AMOUNT);
            entity.setFrozenTicks(300);
        }
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        if (Screen.hasShiftDown()) {
            Text text1 = Text.translatable("item.hss.greatsword.tooltip.shift.line1").formatted(Formatting.GRAY);
            Text text2 = Text.translatable("item.hss.greatsword.tooltip.shift.line2").formatted(Formatting.GRAY);
            Text text3a = Text.translatable("item.hss.greatsword.tooltip.shift.line3a").formatted(Formatting.GRAY);
            Text text3b = Text.translatable("item.hss.greatsword.tooltip.shift.line3b").formatted(Formatting.GRAY);
            Text text4 = Text.translatable("item.hss.greatsword.tooltip.shift.line4").formatted(Formatting.GRAY);
            Text text5 = Text.translatable("item.hss.greatsword.tooltip.shift.line5").formatted(Formatting.GRAY);

            Text textShift = Text.translatable("item.hss.greatsword.tooltip.shift.shift").formatted(Formatting.GOLD);

            Text combinedText = text3a.copy().append(textShift).append(text3b);

            tooltip.add(text1);
            tooltip.add(text2);
            tooltip.add(combinedText);
            tooltip.add(text4);
            tooltip.add(text5);
        } else {
            Text texta = Text.translatable("item.hss.greatsword.tooltipa").formatted(Formatting.GRAY);
            Text textb = Text.translatable("item.hss.greatsword.tooltipb").formatted(Formatting.GRAY);
            Text textShift = Text.translatable("item.hss.greatsword.tooltip.shift").formatted(Formatting.GOLD);

            //The powers of combination
            Text combinedText = texta.copy().append(textShift).append(textb);

            tooltip.add(combinedText);
        }
    }

    @Override
    public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(EquipmentSlot slot) {
        if (slot == EquipmentSlot.MAINHAND) {
            Multimap<EntityAttribute, EntityAttributeModifier> attributes = super.getAttributeModifiers(slot);
            ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> builder = ImmutableMultimap.builder();
            builder.putAll(attributes);
            builder.put(ReachEntityAttributes.ATTACK_RANGE, new EntityAttributeModifier(REACH_UUID, "Attack Range", REACH_AMOUNT, EntityAttributeModifier.Operation.ADDITION));
            return builder.build();
        }
        return super.getAttributeModifiers(slot);
    }
}
