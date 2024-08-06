package net.hollowed.hss.common;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.hollowed.hss.HollowedsSwordsSorcery;
import net.hollowed.hss.common.block.ModBlocks;
import net.hollowed.hss.common.item.ModItems;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ModItemGroups {
    private static final ItemGroup GEM_GROUP = FabricItemGroup.builder()
            .icon(() -> new ItemStack(ModItems.AMALGAMATE))
            .displayName(Text.translatable("itemGroup.hss.hss"))
            .entries((context, entries) -> {
                entries.add(ModBlocks.PEDESTAL);
                entries.add(ModBlocks.ROSE);
                entries.add(ModBlocks.WHITE_ROSE);
                entries.add(ModBlocks.WHITE_ROSE_BUSH);
                entries.add(ModItems.FRAGILITY_CORE);
                entries.add(ModItems.FORTITUDE_CORE);
                entries.add(ModItems.SHADOWS_AXIS);
                entries.add(ModItems.AMALGAMATE);
                entries.add(ModItems.FLAMING_PENDANT);
                entries.add(ModItems.PERMAFROST_BAND);
                entries.add(ModBlocks.THAUMITE_BLOCK);
                entries.add(ModBlocks.THAUMITE_GLASS);
            })
            .build();


    public static void registerItemGroups() {
        Registry.register(Registries.ITEM_GROUP, Identifier.of("hss", "hss"), GEM_GROUP);
        HollowedsSwordsSorcery.LOGGER.info("Registering Item Groups for " + HollowedsSwordsSorcery.MOD_ID);
    }
}
