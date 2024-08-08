package net.hollowed.hss.common.item.custom;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.jamieswhiteshirt.reachentityattributes.ReachEntityAttributes;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
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
        shatter(stack, attacker.getWorld());
        //wiat 40 ticks somehow idk
        repair(stack, attacker.getWorld());
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
