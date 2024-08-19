package net.hollowed.hss.common.silly;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;

import java.util.Collection;

public class EndAllmighty extends SwordItem {

    public EndAllmighty(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Settings settings) {
        super(toolMaterial, attackDamage, attackSpeed, settings);


    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        stack.damage(1, attacker, e -> e.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND));
        Collection<StatusEffectInstance> effects = attacker.getStatusEffects();
        for (StatusEffectInstance effect : effects) {
            target.addStatusEffect(effect);
        }
        attacker.clearStatusEffects();
        return true;
    }
}