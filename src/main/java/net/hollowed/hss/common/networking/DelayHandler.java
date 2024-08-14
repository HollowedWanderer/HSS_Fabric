package net.hollowed.hss.common.networking;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;

public class DelayHandler {

    private static final Map<MinecraftServer, Map<Integer, Runnable>> tasks = new HashMap<>();

    public static void schedule(World world, int delay, Runnable task) {
        if (world.isClient() || !(world instanceof ServerWorld serverWorld)) {
            return;
        }

        MinecraftServer server = serverWorld.getServer();
        tasks.computeIfAbsent(server, k -> new HashMap<>()).put(server.getTicks() + delay, task);
    }

    public static void tick(MinecraftServer server) {
        Map<Integer, Runnable> serverTasks = tasks.get(server);
        if (serverTasks != null) {
            int currentTick = server.getTicks();
            serverTasks.entrySet().removeIf(entry -> {
                if (entry.getKey() <= currentTick) {
                    entry.getValue().run();
                    return true;
                }
                return false;
            });
        }
    }
}
