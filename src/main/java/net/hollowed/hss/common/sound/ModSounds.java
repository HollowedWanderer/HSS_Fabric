package net.hollowed.hss.common.sound;

import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class ModSounds {
    public static final Identifier THAUMITE_PLACE_ID = new Identifier("hss:thaumite_place");
    public static final Identifier THAUMITE_BREAK_ID = new Identifier("hss:thaumite_break");
    public static SoundEvent THAUMITE_BREAK_EVENT = SoundEvent.of(THAUMITE_BREAK_ID);
    public static SoundEvent THAUMITE_PLACE_EVENT = SoundEvent.of(THAUMITE_PLACE_ID);
}
