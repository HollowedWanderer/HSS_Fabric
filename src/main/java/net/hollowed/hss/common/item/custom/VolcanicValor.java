package net.hollowed.hss.common.item.custom;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;

public class VolcanicValor extends SwordItem {
    public int charge = 0;
    public static final int MAX_CHARGE = 5;

    public VolcanicValor(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Settings settings) {
        super(toolMaterial, attackDamage, attackSpeed, settings);
    }


    @Override
    public int getItemBarStep(ItemStack stack) {
        return (int) Math.floor((double) charge / MAX_CHARGE * 13); // 13 steps for the bar
    }

    @Override
    public int getItemBarColor(ItemStack stack) {
        if (charge == 2) {
            return 0x87a211;
        } else if (charge == 3) {
            return 0xc1a620;
        } else if (charge == 4) {
            return 0xdc701b;
        } else if (charge == 5) {
            return 0xfb1919;
        }
        return 0x076b00;
    }

    @Override
    public boolean isItemBarVisible(ItemStack stack) {
        return charge > 0;
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.BLOCK;
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return 72000;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        user.setCurrentHand(hand);
        return TypedActionResult.consume(itemStack);
    }

    @Override
    public boolean canRepair(ItemStack stack, ItemStack ingredient) {
        return ingredient.isIn(ItemTags.PLANKS) || super.canRepair(stack, ingredient);
    }
}