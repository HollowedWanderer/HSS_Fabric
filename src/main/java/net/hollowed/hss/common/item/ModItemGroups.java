package net.hollowed.hss.common.item;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.hollowed.hss.HollowedsSwordsSorcery;
import net.hollowed.hss.common.block.ModBlocks;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ModItemGroups {
    private static final ItemGroup GEM_GROUP = FabricItemGroup.builder()
            .icon(() -> new ItemStack(ModItems.SOUL_GEM))
            .displayName(Text.translatable("itemGroup.hss.gems"))
            .entries((context, entries) -> {
                entries.add(ModItems.SOUL_GEM);
                entries.add(ModBlocks.SOUL_GEM_BLOCK);
            })
            .build();


    public static void registerItemGroups() {
        Registry.register(Registries.ITEM_GROUP, Identifier.of("hss", "gems"), GEM_GROUP);
        HollowedsSwordsSorcery.LOGGER.info("Registering Item Groups for " + HollowedsSwordsSorcery.MOD_ID);
    }
}
