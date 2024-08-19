package net.hollowed.hss.common.enchantments.custom;

import net.hollowed.hss.common.item.custom.HollowedBladeItem;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.enchantment.InfinityEnchantment;
import net.minecraft.enchantment.MendingEnchantment;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;

public class FrozenGaleEnchantment extends Enchantment {
    public FrozenGaleEnchantment() {
        super(Rarity.UNCOMMON, EnchantmentTarget.BREAKABLE, new EquipmentSlot[]{EquipmentSlot.MAINHAND});
    }

    @Override
    public boolean isAcceptableItem(ItemStack stack) {
        return stack.getItem() instanceof HollowedBladeItem;
    }

    @Override
    public int getMinPower(int level) {
        return 1;
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }

    public boolean canAccept(Enchantment other) {
        return !(other instanceof MaelstromEnchantment) && super.canAccept(other);
    }
}
