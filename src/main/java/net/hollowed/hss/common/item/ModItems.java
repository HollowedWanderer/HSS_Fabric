package net.hollowed.hss.common.item;

import net.hollowed.hss.HollowedsSwordsSorcery;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModItems {

    public static final Item SOUL_GEM = registerItem("soul_gem", new Item(new Item.Settings()));

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, Identifier.of(HollowedsSwordsSorcery.MOD_ID, name), item);
    }

    public static void registerModItems() {
        HollowedsSwordsSorcery.LOGGER.info("Registering Mod Items for " + HollowedsSwordsSorcery.MOD_ID);
    }
}
