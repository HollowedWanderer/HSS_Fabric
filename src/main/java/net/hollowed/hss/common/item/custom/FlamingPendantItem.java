package net.hollowed.hss.common.item.custom;

import net.hollowed.hss.common.client.particles.ModScreenParticles;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import team.lodestar.lodestone.handlers.screenparticle.ParticleEmitterHandler;
import team.lodestar.lodestone.registry.common.particle.LodestoneScreenParticleRegistry;
import team.lodestar.lodestone.systems.easing.Easing;
import team.lodestar.lodestone.systems.particle.builder.ScreenParticleBuilder;
import team.lodestar.lodestone.systems.particle.data.GenericParticleData;
import team.lodestar.lodestone.systems.particle.data.color.ColorParticleData;
import team.lodestar.lodestone.systems.particle.data.spin.SpinParticleData;
import team.lodestar.lodestone.systems.particle.screen.ScreenParticleHolder;

import java.awt.*;
import java.util.Random;

public class FlamingPendantItem extends CharmItem implements ParticleEmitterHandler.ItemParticleSupplier{
    public FlamingPendantItem(Settings settings, String charmType, Class<? extends Ability> rightClickAir, Class<? extends Ability> rightClickGround, Class<? extends Ability> hitEntityHand, Class<? extends Ability> hitEntityWeapon, Class<? extends Ability> leftClickAir, Class<? extends Ability> doubleTapControl, Class<? extends Ability> pressShiftInAir, Class<? extends Ability> leftClickGround) {
        super(settings, charmType, rightClickAir, rightClickGround, hitEntityHand, hitEntityWeapon, leftClickAir, doubleTapControl, pressShiftInAir, leftClickGround);
    }

    Color startingColor = new Color(255, 166, 75);
    Color endingColor = new Color(85, 0, 23);

    @Override
    public void spawnLateParticles(ScreenParticleHolder target, World level, float partialTick, ItemStack stack, float x, float y) {
        Random random = new Random();
        float offsetX = generateRandomOffset(random);
        float offsetY = generateRandomOffset(random);

        ScreenParticleBuilder.create(LodestoneScreenParticleRegistry.TWINKLE, target)
                .setColorData(ColorParticleData.create(startingColor, endingColor).setCoefficient(1.4f).setEasing(Easing.BOUNCE_IN_OUT).build())
                .setTransparencyData(GenericParticleData.create((float) ((random.nextFloat() * 0.30) + 0.15), 0.1f).build())
                .setScaleData(GenericParticleData.create(0.55f, 0).build())
                .setLifetime(5)
                .spawnOnStack(-1.5 + offsetX, 1.5 + offsetY);

        if (random.nextInt(10) < 8) {
            ScreenParticleBuilder.create(ModScreenParticles.SQUARE, target)
                    .setColorData(ColorParticleData.create(startingColor, endingColor).setCoefficient(1.4f).setEasing(Easing.BOUNCE_IN_OUT).build())
                    .setSpinData(SpinParticleData.create(0.15f, 0.3f).setSpinOffset((level.getTime() * 0.15f) % 6.28f).setEasing(Easing.LINEAR).build())
                    .setTransparencyData(GenericParticleData.create((float) ((random.nextFloat() * 0.35) + 0.55), 0.1f).build())
                    .setScaleData(GenericParticleData.create(0.2f, 0).build())
                    .setLifetime(30)
                    .setRandomMotion(1)
                    .setGravityStrength(4f)
                    .spawn(x - 1.5 + offsetX, y + 1.5 + offsetY);
        }
    }

    private float generateRandomOffset(Random random) {
        float offset;
        offset = (float) ((random.nextFloat() * 1) - 0.5); // Random float between -0.5 and 0.5
        return offset;
    }
}
