package net.hollowed.hss.common.block;

import net.hollowed.hss.HollowedsSwordsSorcery;
import net.hollowed.hss.common.block.custom.PedestalBlock;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

public class ModBlocks {
    public static final Block SOUL_GEM_BLOCK = registerBlock("soul_gem_block",
            new Block(AbstractBlock.Settings.create().sounds(BlockSoundGroup.AMETHYST_BLOCK).strength(1F, 1F)));

    public static final Block PEDESTAL = registerBlock("pedestal",
            new PedestalBlock(AbstractBlock.Settings.create().sounds(BlockSoundGroup.LODESTONE).strength(1F, 1F).nonOpaque()));


    private static Block registerBlock(String name, Block block) {
        registerBlockItem(name, block);
        return Registry.register(Registries.BLOCK, Identifier.of(HollowedsSwordsSorcery.MOD_ID, name), block);
    }

    private static void registerBlockItem(String name, Block block) {
        Registry.register(Registries.ITEM, Identifier.of(HollowedsSwordsSorcery.MOD_ID, name),
                new BlockItem(block, new Item.Settings()));
    }

    public static void registerModBlocks() {
        HollowedsSwordsSorcery.LOGGER.info("Registering ModBlocks for " + HollowedsSwordsSorcery.MOD_ID);
    }
}
