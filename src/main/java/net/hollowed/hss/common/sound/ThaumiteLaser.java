package net.hollowed.hss.common.sound;

import net.hollowed.hss.common.block.entities.ResonatingAltarBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.MovingSoundInstance;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.entity.Entity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.MathHelper;

import java.util.Objects;

public class ThaumiteLaser extends MovingSoundInstance {
    private final ResonatingAltarBlockEntity projectile;

    public ThaumiteLaser(ResonatingAltarBlockEntity projectile) {
        super(ModSounds.THAUMITE_LASER, SoundCategory.PLAYERS, SoundInstance.createRandom());
        this.projectile = projectile;
        this.repeat = false;
        this.repeatDelay = 0;
        this.volume = 1F; // Initial volume
        this.attenuationType = AttenuationType.LINEAR; // Ensure linear attenuation for sound falloff
        this.pitch = 1F;
    }

    @Override
    public void tick() {

        // Update the sound position to follow the projectile
        this.x = projectile.getPos().getX();
        this.y = projectile.getPos().getY();
        this.z = projectile.getPos().getZ();

        // Calculate distance to the player
        assert MinecraftClient.getInstance().player != null;
        float distanceToPlayer = (float) MinecraftClient.getInstance().player.squaredDistanceTo(this.x, this.y, this.z);

        // Transition volume based on distance with a larger range
        float maxDistance = 600.0F; // Increase the max distance for smoother transition
        float minVolumeDistance = 150.0F; // Distance at which the volume is minimal (volume will be close to zero)
        float distanceFactor = MathHelper.clamp((distanceToPlayer - minVolumeDistance) / (maxDistance - minVolumeDistance), 0.0F, 1.0F);

        // Smooth volume using exponential attenuation
        float maxVolume = 1F; // Maximum volume
        this.volume = maxVolume * (1.0F - distanceFactor);

        // Exponential smoothing
        float smoothingFactor = 0.05F; // Adjust to control smoothing
        this.volume = this.volume * (1.0F - smoothingFactor) + (maxVolume * (1.0F - distanceFactor)) * smoothingFactor;
    }

    public void done() {
        this.setDone();
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setZ(double z) {
        this.z = z;
    }
}




