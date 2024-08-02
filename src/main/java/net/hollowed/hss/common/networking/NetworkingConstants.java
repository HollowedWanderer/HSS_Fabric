package net.hollowed.hss.common.networking;

import net.minecraft.util.Identifier;

import static net.hollowed.hss.HollowedsSwordsSorcery.MOD_ID;

public class NetworkingConstants {
    public static final Identifier PEDESTAL_UPDATE_PACKET_ID = new Identifier(MOD_ID, "pedestal_update");

    public static final Identifier DUST_PARTICLE_PACKET_ID = new Identifier(MOD_ID, "particle_spawn");

    public static final Identifier RING_PARTICLE_PACKET_ID = new Identifier(MOD_ID, "ring_particle_spawn");

    public static final Identifier UTILITY_MOVE_PACKET_ID = new Identifier(MOD_ID, "utility_packet");

    public static final Identifier GROUND_ATTACK_PACKET_ID = new Identifier(MOD_ID, "ground_packet");

    public static final Identifier HEAVY_GROUND_ATTACK_PACKET_ID = new Identifier(MOD_ID, "heavy_ground_packet");

    public static final Identifier MOVEMENT_ABILITY_PACKET_ID = new Identifier(MOD_ID, "movement_packet");

    public static final Identifier ATTACK_PACKET_ID = new Identifier(MOD_ID, "attack_packet");

    public static final Identifier HEAVY_ATTACK_PACKET_ID = new Identifier(MOD_ID, "heavy_attack_packet");

    public static final Identifier GRAB_PACKET_ID = new Identifier(MOD_ID, "grab_packet");

    public static final Identifier MELEE_PACKET_ID = new Identifier(MOD_ID, "melee_packet");
}