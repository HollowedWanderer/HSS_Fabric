package net.hollowed.hss;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.hollowed.hss.common.ModItemGroups;
import net.hollowed.hss.common.block.ModBlockEntities;
import net.hollowed.hss.common.block.ModBlocks;
import net.hollowed.hss.common.block.ModDispenserBehavior;
import net.hollowed.hss.common.client.particles.ModParticles;
import net.hollowed.hss.common.effects.custom.ExpStatusEffect;
import net.hollowed.hss.common.enchantments.custom.FrozenGaleEnchantment;
import net.hollowed.hss.common.enchantments.custom.MaelstromEnchantment;
import net.hollowed.hss.common.event.BlockShieldEvent;
import net.hollowed.hss.common.item.ModItems;
import net.hollowed.hss.common.networking.AttackEntityHandler;
import net.hollowed.hss.common.networking.CharmChecker;
import net.hollowed.hss.common.networking.ClientPacketHandlers;
import net.hollowed.hss.common.networking.DelayHandler;
import net.hollowed.hss.common.networking.packets.*;
import net.hollowed.hss.common.util.ModLootTableModifiers;
import net.hollowed.hss.common.util.ModWorldEvents;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import team.lodestar.lodestone.registry.common.LodestoneWorldEventTypeRegistry;

public class HollowedsSwordsSorcery implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.

	public static String MOD_ID = "hss";

    public static final Logger LOGGER = LoggerFactory.getLogger("hss");

	public static Identifier hssPath(String path) {
		return new Identifier(MOD_ID, path);
	}

	public static Enchantment MAELSTROM = new MaelstromEnchantment();
	public static Enchantment FROZEN_GALE = new FrozenGaleEnchantment();
	public static final StatusEffect EXP = new ExpStatusEffect();

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		LOGGER.info("It's magicking time");

		AttackEntityHandler.register();

		ModParticles.PARTICLES.register();
		ModParticles.registerParticles();
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
		ModLootTableModifiers.modifyLootTables();
		ModDispenserBehavior.registerDefaults();
		BackSlotPacket.register();
		//new CopperConversionHandler();

		ServerTickEvents.END_SERVER_TICK.register(DelayHandler::tick);
		ServerWorldEvents.LOAD.register(this::onWorldLoad);
		BlockShieldEvent.register();

		LodestoneWorldEventTypeRegistry.registerEventType(ModWorldEvents.TEST_EVENT);

		//Registry.register(Registries.STATUS_EFFECT, new Identifier("hss", "xp"), XP);

		Registry.register(Registries.ENCHANTMENT, new Identifier(MOD_ID, "maelstrom"), MAELSTROM);
		Registry.register(Registries.ENCHANTMENT, new Identifier(MOD_ID, "frozen_gale"), FROZEN_GALE);

//		FabricLoader.getInstance().getModContainer(MOD_ID).ifPresent(modContainer -> {
//			ResourceManagerHelper.registerBuiltinResourcePack(new Identifier(MOD_ID, "Amenities"), modContainer, ResourcePackActivationType.NORMAL);
//		});

	}

	private void onWorldLoad(MinecraftServer server, ServerWorld world) {
		//WorldEventHandler.addWorldEvent(world, new TestEvent());
	}
}