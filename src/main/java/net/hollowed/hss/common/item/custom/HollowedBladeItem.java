package net.hollowed.hss.common.item.custom;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class HollowedBladeItem extends SwordItem {

    public HollowedBladeItem(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Settings settings) {
        super(toolMaterial, attackDamage, attackSpeed, settings);
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

            // Combine texts
            Text combinedText = texta.copy().append(textShift).append(textb);

            tooltip.add(combinedText);
        }
    }
}
