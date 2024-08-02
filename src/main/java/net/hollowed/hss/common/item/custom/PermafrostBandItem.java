package net.hollowed.hss.common.item.custom;

import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import team.lodestar.lodestone.handlers.screenparticle.ParticleEmitterHandler;
import team.lodestar.lodestone.registry.common.particle.LodestoneScreenParticleRegistry;
import team.lodestar.lodestone.systems.easing.Easing;
import team.lodestar.lodestone.systems.particle.builder.ScreenParticleBuilder;
import team.lodestar.lodestone.systems.particle.data.GenericParticleData;
import team.lodestar.lodestone.systems.particle.data.color.ColorParticleData;
import team.lodestar.lodestone.systems.particle.screen.ScreenParticleHolder;

import java.awt.*;
import java.util.Random;

public class PermafrostBandItem extends CharmItem implements ParticleEmitterHandler.ItemParticleSupplier {
    public PermafrostBandItem(Settings settings, Class<? extends Ability> rightClickAir, Class<? extends Ability> rightClickGround, Class<? extends Ability> hitEntityHand, Class<? extends Ability> hitEntityWeapon, Class<? extends Ability> leftClickAir, Class<? extends Ability> doubleTapControl, Class<? extends Ability> pressShiftInAir, Class<? extends Ability> leftClickGround) {
        super(settings, rightClickAir, rightClickGround, hitEntityHand, hitEntityWeapon, leftClickAir, doubleTapControl, pressShiftInAir, leftClickGround);
    }

    Color startingColor = new Color(126, 182, 255);
    Color endingColor = new Color(22, 16, 54);

    @Override
    public void spawnLateParticles(ScreenParticleHolder target, World level, float partialTick, ItemStack stack, float x, float y) {
            ScreenParticleBuilder.create(LodestoneScreenParticleRegistry.TWINKLE, target)
                    .setColorData(ColorParticleData.create(startingColor, endingColor).setCoefficient(1.4f).setEasing(Easing.BOUNCE_IN_OUT).build())
                    .setTransparencyData(GenericParticleData.create(0.35f, 0.1f).build())
                    .setScaleData(GenericParticleData.create(0.55f, 0).build())
                    .setLifetime(5)
                    .spawnOnStack( +1.5,  -1.5);
    }
}
