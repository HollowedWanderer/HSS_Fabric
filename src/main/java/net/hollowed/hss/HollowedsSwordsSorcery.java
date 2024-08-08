package net.hollowed.hss;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.hollowed.hss.common.ModItemGroups;
import net.hollowed.hss.common.block.ModBlockEntities;
import net.hollowed.hss.common.block.ModBlocks;
import net.hollowed.hss.common.client.particles.ModParticles;
import net.hollowed.hss.common.item.ModItems;
import net.hollowed.hss.common.networking.AttackEntityHandler;
import net.hollowed.hss.common.networking.CharmChecker;
import net.hollowed.hss.common.networking.packets.*;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

public class HollowedsSwordsSorcery implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.

	public static String MOD_ID = "hss";

    public static final Logger LOGGER = LoggerFactory.getLogger("hss");

	public static Identifier hssPath(String path) {
		return new Identifier(MOD_ID, path);
	}

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		LOGGER.info("It's magicking time");

		AttackEntityHandler.register();

		ModParticles.PARTICLES.register();
		ModBlockEntities.registerBlockEntities();
		ModBlocks.registerModBlocks();
		ModItems.registerModItems();
		ModItemGroups.registerItemGroups();
		ClientPacketHandlers.register();
		CharmChecker.initialize();
		UtilityMovePacket.register();
		GroundAttackPacket.register();
		HeavyGroundAttackPacket.register();
		MovementAbilityPacket.register();
		AttackPacket.register();
		HeavyAttackPacket.register();
		GrabPacket.register();
		MeleePacket.register();

		FabricLoader.getInstance().getModContainer(MOD_ID).ifPresent(modContainer -> {
			ResourceManagerHelper.registerBuiltinResourcePack(new Identifier(MOD_ID, "Amenities"), modContainer, ResourcePackActivationType.DEFAULT_ENABLED);
		});
	}
}