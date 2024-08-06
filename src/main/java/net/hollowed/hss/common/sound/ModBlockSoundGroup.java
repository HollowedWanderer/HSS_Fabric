package net.hollowed.hss.common.sound;

import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;

public class ModBlockSoundGroup extends BlockSoundGroup {
    public ModBlockSoundGroup(float volume, float pitch, SoundEvent breakSound, SoundEvent stepSound, SoundEvent placeSound, SoundEvent hitSound, SoundEvent fallSound) {
        super(volume, pitch, breakSound, stepSound, placeSound, hitSound, fallSound);
    }

    public static final BlockSoundGroup THAUMITE;

    static {
        THAUMITE = new BlockSoundGroup(1.0F, 1.0F, ModSounds.THAUMITE_BREAK_EVENT, SoundEvents.BLOCK_AMETHYST_BLOCK_STEP, ModSounds.THAUMITE_PLACE_EVENT, SoundEvents.BLOCK_AMETHYST_BLOCK_HIT, SoundEvents.BLOCK_AMETHYST_BLOCK_FALL);
    }
}
