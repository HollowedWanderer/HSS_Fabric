package net.hollowed.hss.common.worldEvents;

import net.hollowed.hss.common.util.ModWorldEvents;
import team.lodestar.lodestone.systems.worldevent.WorldEventInstance;

public class TestEvent extends WorldEventInstance {
    public TestEvent() {
        super(ModWorldEvents.TEST_EVENT);
    }
}
