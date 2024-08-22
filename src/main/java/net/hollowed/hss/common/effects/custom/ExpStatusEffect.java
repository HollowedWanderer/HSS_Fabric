package net.hollowed.hss.common.effects.custom;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.player.PlayerEntity;

public class ExpStatusEffect extends StatusEffect {
  public ExpStatusEffect() {
    super(
      StatusEffectCategory.BENEFICIAL, // whether beneficial or harmful for entities
      0x98D982); // color in RGB
  }
 
  // This method is called every tick to check whether it should apply the status effect or not
  @Override
  public boolean canApplyUpdateEffect(int duration, int amplifier) {
    // In our case, we just make it return true so that it applies the status effect every tick.
    return true;
  }

  @Override
  public void onRemoved(LivingEntity entity, AttributeContainer attributes, int amplifier) {
      if (entity instanceof PlayerEntity) {
        ((PlayerEntity) entity).addExperience(20);
      }
  }
}