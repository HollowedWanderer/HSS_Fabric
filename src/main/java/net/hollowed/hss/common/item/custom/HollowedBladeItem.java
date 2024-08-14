package net.hollowed.hss.common.item.custom;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.jamieswhiteshirt.reachentityattributes.ReachEntityAttributes;
import net.hollowed.hss.common.entity.custom.CryoShardEntity;
import net.hollowed.hss.common.entity.custom.FragileCryoShardEntity;
import net.hollowed.hss.common.item.ModItems;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.damage.DamageSources;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.*;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public class HollowedBladeItem extends SwordItem {
    private static final UUID REACH_UUID = UUID.fromString("d9e5dbe3-2b7e-4d9b-9f55-4bafab8c42d4");
    private static final double REACH_AMOUNT = 1.5;

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
        }
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        if (!world.isClient && user.isSneaking()) {
            if (!user.getItemCooldownManager().isCoolingDown(itemStack.getItem())) {
                shatter(itemStack, user.getWorld());
                user.getItemCooldownManager().set(itemStack.getItem(), 300);
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

                FragileCryoShardEntity entity = new FragileCryoShardEntity(world, user, itemStack.copyWithCount(1));

                // Set the velocity with the calculated x, y, and z components
                entity.setVelocity(xVelocity, yVelocity, zVelocity);

                entity.pickupType = PersistentProjectileEntity.PickupPermission.CREATIVE_ONLY;

                world.spawnEntity(entity);
            }

            world.playSoundFromEntity(null, user, SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, SoundCategory.NEUTRAL, 0.5F, 0.4F / (world.getRandom().nextFloat() * 0.4F + 0.8F));
        } else {
            return TypedActionResult.fail(itemStack);
        }

        return TypedActionResult.success(itemStack, world.isClient());
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        assert entity instanceof PlayerEntity;
        if (!((PlayerEntity) entity).getItemCooldownManager().isCoolingDown(stack.getItem())) {
            repair(stack, world);
        } else if (((PlayerEntity) entity).getItemCooldownManager().isCoolingDown(stack.getItem()) && ((PlayerEntity) entity).getMainHandStack() == stack) {
            ((PlayerEntity) entity).addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 1, 0, true, false));
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
