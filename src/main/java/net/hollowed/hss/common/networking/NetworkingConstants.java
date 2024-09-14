package net.hollowed.hss.common.networking;

import net.minecraft.util.Identifier;

import static net.hollowed.hss.HollowedsSwordsSorcery.MOD_ID;

public class NetworkingConstants {
    public static final Identifier PEDESTAL_UPDATE_PACKET_ID = new Identifier(MOD_ID, "pedestal_update");

    public static final Identifier ALTAR_UPDATE_PACKET_ID = new Identifier(MOD_ID, "altar_update");

    public static final Identifier DUST_PARTICLE_PACKET_ID = new Identifier(MOD_ID, "particle_spawn");

    public static final Identifier RING_PARTICLE_PACKET_ID = new Identifier(MOD_ID, "ring_particle_spawn");

    public static final Identifier UTILITY_MOVE_PACKET_ID = new Identifier(MOD_ID, "utility_packet");

    public static final Identifier BACKSLOT_PACKET_ID = new Identifier(MOD_ID, "backslot_packet");

    public static final Identifier GROUND_ATTACK_PACKET_ID = new Identifier(MOD_ID, "ground_packet");

    public static final Identifier HEAVY_GROUND_ATTACK_PACKET_ID = new Identifier(MOD_ID, "heavy_ground_packet");

    public static final Identifier MOVEMENT_ABILITY_PACKET_ID = new Identifier(MOD_ID, "movement_packet");

    public static final Identifier ATTACK_PACKET_ID = new Identifier(MOD_ID, "attack_packet");

    public static final Identifier HEAVY_ATTACK_PACKET_ID = new Identifier(MOD_ID, "heavy_attack_packet");

    public static final Identifier GRAB_PACKET_ID = new Identifier(MOD_ID, "grab_packet");

    public static final Identifier MELEE_PACKET_ID = new Identifier(MOD_ID, "melee_packet");

    public static final Identifier SHATTER_RING_PACKET_ID = new Identifier(MOD_ID, "shatter_ring_packet");

    public static final Identifier FG_RING_PACKET_ID = new Identifier(MOD_ID, "fg_ring_packet");

    public static final Identifier ALTAR_PLACE_PARTICLE_PACKET_ID = new Identifier(MOD_ID, "altar_place_particle_spawn");

    public static final Identifier ALTAR_ORBIT_PACKET_ID = new Identifier(MOD_ID, "altar_orbit_packet");

    public static final Identifier ALTAR_EXPLODE_PACKET_ID = new Identifier(MOD_ID, "altar_explode_packet");

    public static final Identifier ALTAR_CHARGE_PACKET_ID = new Identifier(MOD_ID, "altar_charge_packet");

    public static final Identifier ALTAR_LASER_PACKET_ID = new Identifier(MOD_ID, "altar_laser_packet");
}
