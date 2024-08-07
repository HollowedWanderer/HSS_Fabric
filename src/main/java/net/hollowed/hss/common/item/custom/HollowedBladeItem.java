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
            tooltip.add(getFormattedText("item.hss.greatsword.tooltip.shift.line1"));
            tooltip.add(getFormattedText("item.hss.greatsword.tooltip.shift.line2"));
            tooltip.add(getFormattedText("item.hss.greatsword.tooltip.shift.line3", true));
            tooltip.add(getFormattedText("item.hss.greatsword.tooltip.shift.line4"));
            tooltip.add(getFormattedText("item.hss.greatsword.tooltip.shift.line5"));
        } else {
            tooltip.add(getFormattedText1("item.hss.greatsword.tooltip", true));
        }
    }

    private MutableText getFormattedText(String key) {
        return getFormattedText(key, false);
    }

    private MutableText getFormattedText(String key, boolean highlightRightClick) {
        MutableText text = Text.translatable(key).formatted(Formatting.GRAY);
        if (highlightRightClick) {
            text = formatRightClickText(text);
        }
        return text;
    }

    private MutableText getFormattedText1(String key, boolean highlightShift) {
        MutableText text = Text.translatable(key).formatted(Formatting.GRAY);
        if (highlightShift) {
            text = formatShiftText(text);
        }
        return text;
    }

    private MutableText formatRightClickText(MutableText text) {
        String[] parts = text.getString().split("\\[Shift \\+ Right-click\\]");
        MutableText formattedText = Text.literal(parts[0]).formatted(Formatting.GRAY);
        formattedText.append(Text.literal("[Shift + Right-click]").formatted(Formatting.GOLD));
        if (parts.length > 1) {
            formattedText.append(Text.literal(parts[1]).formatted(Formatting.GRAY));
        }
        return formattedText;
    }

    private MutableText formatShiftText(MutableText text) {
        String[] parts = text.getString().split("\\[Shift\\]");
        MutableText formattedText = Text.literal(parts[0]).formatted(Formatting.GRAY);
        formattedText.append(Text.literal("[Shift]").formatted(Formatting.GOLD));
        if (parts.length > 1) {
            formattedText.append(Text.literal(parts[1]).formatted(Formatting.GRAY));
        }
        return formattedText;
    }
}
