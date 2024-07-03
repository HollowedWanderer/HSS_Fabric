package net.hollowed.hss;

import net.fabricmc.api.ModInitializer;
import net.hollowed.hss.common.block.ModBlockEntities;
import net.hollowed.hss.common.block.ModBlocks;
import net.hollowed.hss.common.item.ModItemGroups;
import net.hollowed.hss.common.item.ModItems;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HollowedsSwordsSorcery implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.

	public static String MOD_ID = "hss";

    public static final Logger LOGGER = LoggerFactory.getLogger("hss");

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.



		LOGGER.info("Hello Fabric world!");

		ModBlockEntities.registerBlockEntities();
		ModBlocks.registerModBlocks();
		ModItems.registerModItems();
		ModItemGroups.registerItemGroups();
	}
}