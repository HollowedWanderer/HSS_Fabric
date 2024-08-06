package net.hollowed.hss.common.item;

import net.hollowed.hss.HollowedsSwordsSorcery;
import net.hollowed.hss.common.item.abilities.*;
import net.hollowed.hss.common.item.custom.CharmItem;
import net.hollowed.hss.common.item.custom.FlamingPendantItem;
import net.hollowed.hss.common.item.custom.GreatswordItem;
import net.hollowed.hss.common.item.custom.PermafrostBandItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModItems {

    public static final Item FRAGILITY_CORE = registerItem("fragility_core", new Item(new Item.Settings()));
    public static final Item FORTITUDE_CORE = registerItem("fortitude_core", new Item(new Item.Settings()));
    public static final Item SHADOWS_AXIS = registerItem("shadows_axis", new Item(new Item.Settings()));
    public static final Item AMALGAMATE = registerItem("amalgamate", new Item(new Item.Settings()));

    public static final Item HOLLOWED_BLADE = registerItem("hollowed_blade", new GreatswordItem(new Item.Settings()));

    public static final Item FLAMING_PENDANT = registerItem("flaming_pendant",
            new FlamingPendantItem(new Item.Settings().maxCount(1), "fire",
                    NormalAttack.class, //Normal attack
                    GroundAttack.class, //Ground attack
                    GrabAttack.class, //Grab attack
                    MeleeAttack.class, //Melee attack
                    HeavyAttack.class, //Heavy attack
                    UtilityMove.class, //Utility move
                    MovementAbility.class, //Movement ability
                    HeavyGroundAttack.class)); //Heavy ground attack

    public static final Item PERMAFROST_BAND = registerItem("permafrost_band",
            new PermafrostBandItem(new Item.Settings().maxCount(1), "ice",
                    IceNormalAttack.class, //Normal attack
                    IceGroundAttack.class, //Ground attack
                    IceGrabAttack.class, //Grab attack
                    IceMeleeAttack.class, //Melee attack
                    IceHeavyAttack.class, //Heavy attack
                    IceUtilityMove.class, //Utility move
                    IceMovementAbility.class, //Movement ability
                    IceHeavyGroundAttack.class)); //Heavy ground attack

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, Identifier.of(HollowedsSwordsSorcery.MOD_ID, name), item);
    }

    public static void registerModItems() {
        HollowedsSwordsSorcery.LOGGER.info("Registering Mod Items for " + HollowedsSwordsSorcery.MOD_ID);
    }
}
