package net.hollowed.hss.common.sound;

import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;

public class ModSounds {
    public static SoundEvent THAUMITE_BREAK_EVENT = SoundEvent.of(new Identifier("hss:thaumite_break"));
    public static SoundEvent THAUMITE_PLACE_EVENT = SoundEvent.of(new Identifier("hss:thaumite_place"));
    public static SoundEvent ALTAR_PLACE_ITEM = SoundEvent.of(new Identifier("hss:altar_place_item"));
    public static SoundEvent THAUMITE_LASER = SoundEvent.of(new Identifier("hss:thaumite_laser"));
    public static SoundEvent THAUMITE_SCREENSHAKE = SoundEvent.of(new Identifier("hss:thaumite_shake"));
    public static SoundEvent THAUMITE_EXPLOSION = SoundEvent.of(new Identifier("hss:thaumite_explosion"));
    public static SoundEvent SWORD_UNSHEATH = SoundEvent.of(new Identifier("hss:sword_unsheath"));
    public static SoundEvent BYE_BYE_BYE_EVENT = SoundEvent.of(new Identifier("hss:bye_bye_bye"), 25);
}
