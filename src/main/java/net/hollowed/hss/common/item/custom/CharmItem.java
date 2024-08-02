package net.hollowed.hss.common.item.custom;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.hollowed.hss.common.networking.ModKeyBindings;
import net.hollowed.hss.common.networking.RightClickHandler;
import net.hollowed.hss.common.networking.packets.MovementAbilityPacket;
import net.hollowed.hss.common.networking.packets.UtilityMovePacket;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import static net.hollowed.hss.ModComponents.*;

public class CharmItem extends Item {
    private final Ability attack;
    private final Ability groundAttack;
    private final Ability grabAttack;
    private final Ability meleeAttack;
    private final Ability heavyAttack;
    private final Ability utilityMove;
    private final Ability movementAbility;
    private final Ability heavyGroundAttack;

    public CharmItem(Settings settings,
                     Class<? extends Ability> rightClickAir,
                     Class<? extends Ability> rightClickGround,
                     Class<? extends Ability> hitEntityHand,
                     Class<? extends Ability> hitEntityWeapon,
                     Class<? extends Ability> leftClickAir,
                     Class<? extends Ability> doubleTapControl,
                     Class<? extends Ability> pressShiftInAir,
                     Class<? extends Ability> leftClickGround) {
        super(settings);
        this.attack = createAbility(rightClickAir);
        this.groundAttack = createAbility(rightClickGround);
        this.grabAttack = createAbility(hitEntityHand);
        this.meleeAttack = createAbility(hitEntityWeapon);
        this.heavyAttack = createAbility(leftClickAir);
        this.utilityMove = createAbility(doubleTapControl);
        this.movementAbility = createAbility(pressShiftInAir);
        this.heavyGroundAttack = createAbility(leftClickGround);
    }

    private Ability createAbility(Class<? extends Ability> abilityClass) {
        try {
            return abilityClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Failed to create ability", e);
        }
    }

    private static final int DOUBLE_PRESS_DELAY = 250; // Time in milliseconds to detect double press
    private long lastPressTime = 0;
    private boolean firstPressDetected = false;
    private boolean keyProcessed = false; // To track if the key press has been processed
    private boolean keyProcessed1 = false; // To track if the key press has been processed

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (entity instanceof PlayerEntity player && !player.getItemCooldownManager().isCoolingDown(this)) {
            if (BOUND_INVENTORY.get(player).getValue()) {
                AttackChecker();
                if (GROUND_CHECKER.get(player).getValue() && (player.getMainHandStack().getItem() == ItemStack.EMPTY.getItem()) && !player.isSneaking()) {
                    groundAttack.useAbility(world, player);
                    player.getItemCooldownManager().set(this, 20);
                }
                if (UTILITY_CHECKER.get(player).getValue()) {
                    utilityMove.useAbility(world, player);
                    player.getItemCooldownManager().set(this, 20);
                }
                if (MOVEMENT_CHECKER.get(player).getValue() && !player.isOnGround()) {
                    movementAbility.useAbility(world, player);
                    player.getItemCooldownManager().set(this, 20);
                }
                if (HEAVY_GROUND_CHECKER.get(player).getValue() && (player.getMainHandStack().getItem() == ItemStack.EMPTY.getItem()) && player.isSneaking()) {
                    heavyGroundAttack.useAbility(world, player);
                    player.getItemCooldownManager().set(this, 20);
                }
                if (NORMAL_CHECKER.get(player).getValue() && !player.isSneaking() && player.getMainHandStack().getItem() == ItemStack.EMPTY.getItem()) {
                    attack.useAbility(world, player);
                    player.getItemCooldownManager().set(this, 20);
                }
                if (HEAVY_CHECKER.get(player).getValue() && player.isSneaking() && player.getMainHandStack().getItem() == ItemStack.EMPTY.getItem()) {
                    heavyAttack.useAbility(world, player);
                    player.getItemCooldownManager().set(this, 20);
                }
                if (GRAB_CHECKER.get(player).getValue() && player.getMainHandStack().getItem() == ItemStack.EMPTY.getItem()) {
                    grabAttack.useAbility(world, player);
                    player.getItemCooldownManager().set(this, 20);
                }
                if (MELEE_CHECKER.get(player).getValue() && player.getMainHandStack().getItem() != ItemStack.EMPTY.getItem()) {
                    meleeAttack.useAbility(world, player);
                    player.getItemCooldownManager().set(this, 20);
                }
            }
        }
        super.inventoryTick(stack, world, entity, slot, selected);
    }

    @Environment(EnvType.CLIENT)
    public void AttackChecker () {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (ModKeyBindings.utilityMoveBinding.isPressed() && !keyProcessed) {
                long currentTime = System.currentTimeMillis();
                if (firstPressDetected && (currentTime - lastPressTime) < DOUBLE_PRESS_DELAY) {
                    assert client.player != null;
                    UtilityMovePacket.send();
                    firstPressDetected = false;
                } else {
                    firstPressDetected = true;
                    lastPressTime = currentTime;
                }
                keyProcessed = true;
            } else if (!ModKeyBindings.utilityMoveBinding.isPressed()) {
                keyProcessed = false;
                if (System.currentTimeMillis() - lastPressTime >= DOUBLE_PRESS_DELAY) {
                    firstPressDetected = false;
                }
            }
            if (ModKeyBindings.movementAbilityBinding.isPressed() && !keyProcessed1) {
                assert client.player != null;
                MovementAbilityPacket.send();
                keyProcessed1 = true;
            } else if (!ModKeyBindings.movementAbilityBinding.isPressed()) {
                keyProcessed1 = false;
            }
        });
    }

    public interface Ability {
        void useAbility(World world, PlayerEntity user);
    }
}
