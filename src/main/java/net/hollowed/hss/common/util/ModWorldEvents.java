package net.hollowed.hss.common.util;

import net.hollowed.hss.common.worldEvents.TestEvent;
import net.minecraft.util.Identifier;
import team.lodestar.lodestone.systems.worldevent.WorldEventType;

import static team.lodestar.lodestone.registry.common.LodestoneWorldEventTypeRegistry.registerEventType;

public class ModWorldEvents {
    public static WorldEventType TEST_EVENT = registerEventType(new WorldEventType(new Identifier("hss", "test"), TestEvent::new, true));
}
