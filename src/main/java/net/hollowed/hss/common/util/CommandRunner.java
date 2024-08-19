package net.hollowed.hss.common.util;

import net.minecraft.entity.Entity;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;

public class CommandRunner {

    public static void runCommand(ServerWorld world, String command) {
        if (!world.isClient) {
            ServerCommandSource commandSource = world.getServer().getCommandSource().withSilent();
            world.getServer().getCommandManager().executeWithPrefix(commandSource, command);
        }
    }

    public static void runCommandAsEntity(Entity entity, String command) {
        if (!entity.getWorld().isClient() && entity.getWorld() instanceof ServerWorld serverWorld) {
            ServerCommandSource commandSource = new ServerCommandSource(
                    entity,                      // The entity running the command
                    new Vec3d(entity.getX(), entity.getY(), entity.getZ()), // Position
                    entity.getRotationClient(),  // Rotation
                    serverWorld,                 // Server world
                    4,                           // Permission level
                    entity.getName().getString(), // Entity name
                    entity.getDisplayName(),     // Display name
                    serverWorld.getServer(),     // Server instance
                    entity                       // Entity
            ).withSilent();

            serverWorld.getServer().getCommandManager().executeWithPrefix(commandSource, command);
        }
    }
}

