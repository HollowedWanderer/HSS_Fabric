package net.hollowed.hss.common.block;

import net.hollowed.hss.HollowedsSwordsSorcery;
import net.hollowed.hss.common.block.custom.PedestalBlock;
import net.hollowed.hss.common.block.custom.ResonatingAltarBlock;
import net.hollowed.hss.common.sound.ModBlockSoundGroup;
import net.minecraft.block.*;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.resource.featuretoggle.FeatureFlag;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

public class ModBlocks {

    //public static final Block SOUL_GEM_BLOCK = registerBlock("soul_gem_block",
    //        new Block(AbstractBlock.Settings.create().sounds(BlockSoundGroup.AMETHYST_BLOCK).strength(1F, 1F)));

    public static final Block ROSE = registerBlock("rose",
            new FlowerBlock(StatusEffects.FIRE_RESISTANCE, 15, AbstractBlock.Settings.create().sounds(BlockSoundGroup.GRASS).strength(0F, 0F)
                    .breakInstantly().pistonBehavior(PistonBehavior.DESTROY).noCollision()));

    public static final Block WHITE_ROSE_BUSH = registerBlock("white_rose_bush",
            new TallFlowerBlock(AbstractBlock.Settings.create().sounds(BlockSoundGroup.GRASS).strength(0F, 0F)
                    .breakInstantly().pistonBehavior(PistonBehavior.DESTROY).noCollision()));

    public static final Block POTTED_ROSE = registerBlock("potted_rose", createFlowerPotBlock(ROSE));

    public static final Block WHITE_ROSE = registerBlock("white_rose",
            new FlowerBlock(StatusEffects.SLOW_FALLING, 15, AbstractBlock.Settings.create().sounds(BlockSoundGroup.GRASS).strength(0F, 0F)
                    .breakInstantly().pistonBehavior(PistonBehavior.DESTROY).noCollision()));

    public static final Block POTTED_WHITE_ROSE = registerBlock("potted_white_rose", createFlowerPotBlock(WHITE_ROSE));

    public static final Block PEDESTAL = registerBlock("pedestal",
            new PedestalBlock(AbstractBlock.Settings.create().sounds(BlockSoundGroup.LODESTONE).strength(1F, 1F).nonOpaque().requiresTool()));

    public static final Block THAUMITE_BLOCK = registerBlock("thaumite_block",
            new Block(AbstractBlock.Settings.copy(Blocks.AMETHYST_BLOCK).sounds(ModBlockSoundGroup.THAUMITE)));
    public static final Block THAUMITE_GLASS = registerBlock("thaumite_glass",
                    new GlassBlock(AbstractBlock.Settings.copy(Blocks.AMETHYST_BLOCK).sounds(ModBlockSoundGroup.THAUMITE).nonOpaque().blockVision(Blocks::never).solidBlock(Blocks::never)));
    public static final Block RESONATING_ALTAR = registerBlock("resonating_altar",
            new ResonatingAltarBlock(AbstractBlock.Settings.copy(Blocks.AMETHYST_BLOCK).sounds(ModBlockSoundGroup.THAUMITE).nonOpaque().requiresTool()));


    private static Block registerBlock(String name, Block block) {
        registerBlockItem(name, block);
        return Registry.register(Registries.BLOCK, Identifier.of(HollowedsSwordsSorcery.MOD_ID, name), block);
    }

    private static void registerBlockItem(String name, Block block) {
        Registry.register(Registries.ITEM, Identifier.of(HollowedsSwordsSorcery.MOD_ID, name),
                new BlockItem(block, new Item.Settings()));
    }

    public static FlowerPotBlock createFlowerPotBlock(Block flower, FeatureFlag... requiredFeatures) {
        AbstractBlock.Settings settings = AbstractBlock.Settings.create().breakInstantly().nonOpaque().pistonBehavior(PistonBehavior.DESTROY);
        if (requiredFeatures.length > 0) {
            settings = settings.requires(requiredFeatures);
        }

        return new FlowerPotBlock(flower, settings);
    }

    public static void registerModBlocks() {
        HollowedsSwordsSorcery.LOGGER.info("Registering ModBlocks for " + HollowedsSwordsSorcery.MOD_ID);
    }
}
