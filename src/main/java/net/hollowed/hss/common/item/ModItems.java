package net.hollowed.hss.common.item;

import net.hollowed.hss.HollowedsSwordsSorcery;
import net.hollowed.hss.common.item.abilities.*;
import net.hollowed.hss.common.item.custom.*;
import net.hollowed.hss.common.sound.ModSounds;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.Util;

import java.util.List;

public class ModItems {

    public static final Item FRAGILITY_CORE = registerItem("fragility_core", new Item(new Item.Settings().maxCount(1)));
    public static final Item FORTITUDE_CORE = registerItem("fortitude_core", new Item(new Item.Settings().maxCount(1)));
    public static final Item SHADOWS_AXIS = registerItem("shadows_axis", new Item(new Item.Settings().maxCount(1)));
    public static final Item AMALGAMATE = registerItem("amalgamate", new Item(new Item.Settings().maxCount(1)));

    public static final Item MUSIC_DISC_THAUMITE = registerItem("music_disc_thaumite", new MusicDiscItem(3, ModSounds.BYE_BYE_BYE_EVENT, new Item.Settings().maxCount(1), 202));

    public static final Item HOLLOWED_BLADE = registerItem("hollowed_blade",
            new HollowedBladeItem(ToolMaterials.NETHERITE, 2, -2.6f, new Item.Settings().fireproof().maxCount(1).rarity(Rarity.RARE)));

    public static final Item HOLLOWED_WRENCH = registerItem("hollowed_wrench",
            new SwordItem(ToolMaterials.NETHERITE, 2, -2.6f, new Item.Settings().fireproof().maxCount(1).rarity(Rarity.RARE)));


    public static final Item YKWIMIO = registerItem("ykwimio",
            new SwordItem(ToolMaterials.NETHERITE, 3, -2.6f, new Item.Settings().fireproof().maxCount(1).rarity(Rarity.UNCOMMON)));

    public static final Item VOLCANIC_VALOR = registerItem("volcanic_valor", new VolcanicValor(ToolMaterials.NETHERITE, 2, -2.4f, new Item.Settings().fireproof().maxCount(1).rarity(Rarity.UNCOMMON)));

    public static final Item CRYO_SHARD = registerItem("cryo_shard", new CryoShardItem(new Item.Settings()));

    public static final Item NETHERONG_UPGRADE_SMITHING_TEMPLATE = registerItem("netherong_upgrade_smithing_template", SmithingTemplateItem.createNetheriteUpgrade());

    public static final Item ENDERQUITE_UPGRADE_SMITHING_TEMPLATE = registerItem("enderquite_upgrade_smithing_template", new SmithingTemplateItem(
            (Text.translatable(Util.createTranslationKey("item", new Identifier("smithing_template.enderquite_upgrade.applies_to")))),
            (Text.translatable(Util.createTranslationKey("item", new Identifier("smithing_template.enderquite_upgrade.ingredients")))),
            (Text.translatable(Util.createTranslationKey("upgrade", new Identifier("enderquite_upgrade")))),
            (Text.translatable(Util.createTranslationKey("item", new Identifier("smithing_template.enderquite_upgrade.base_slot_description")))),
            (Text.translatable(Util.createTranslationKey("item", new Identifier("smithing_template.enderquite_upgrade.additions_slot_description")))),
            (List.of(new Identifier("item/empty_armor_slot_helmet"),
                    new Identifier("item/empty_armor_slot_chestplate"),
                    new Identifier("item/empty_armor_slot_leggings"),
                    new Identifier("item/empty_armor_slot_boots"))),
            (List.of(new Identifier("item/empty_slot_ingot")))));

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
